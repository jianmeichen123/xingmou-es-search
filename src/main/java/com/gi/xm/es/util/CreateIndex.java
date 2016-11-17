package com.gi.xm.es.util;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gi.xm.es.controller.EslController;

public class CreateIndex  
{  
  
	public  static Client client ;
	public static final String INDEX = "xm_es";
	public static final String TYPE = "search";
	public static final String FILEPATH = "C:/Users/sks/Desktop/456.csv";
	public static final String IP ="10.9.130.135";
	
	private static final Logger LOG = LoggerFactory.getLogger(EslController.class);
	
	static{
		try {
		   client = new PreBuiltTransportClient(Settings.EMPTY)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 创建索引名称
	 * 
	 * @param indices
	 *  索引名称
	 */
	public static void createCluterName(String indexName) {
		IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
		IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
		//判断是否存在索引，如果存在，则删除后创建
		if(inExistsResponse.isExists()){
			DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
			//根据DeleteIndexResponse对象的isAcknowledged()方法判断删除是否成功,返回值为boolean类型
			if(dResponse.isAcknowledged()){
				client.admin().indices().prepareCreate(indexName).execute().actionGet();
			}
		}else{
				client.admin().indices().prepareCreate(indexName).execute().actionGet();
		}
		
	}
	
	/**
	 * 创建mapping(feid("indexAnalyzer","ik")该字段分词IK索引
	 * ;feid("searchAnalyzer","ik")该字段分词ik查询；具体分词插件请看IK分词插件说明)
	 * 
	 * @param indices
	 *            索引名称；
	 * @param mappingType
	 *            索引类型
	 * @throws Exception
	 */
	public static void createMapping(String indexName, String mappingType) {
		
		try{
	    	XContentBuilder builder=XContentFactory
	    			.jsonBuilder()
	    			.startObject()
	    			    .startObject(mappingType)
	    				.startObject("properties")
	    				   .startObject("id")
	    				   		.field("type", "long")
	    				   	.endObject()
				           .startObject("title")
				           		.field("type", "text")
				           		.field("index", "not_analyzed")
				           		.field("store", "yes")
				           	.endObject()
				           	.startObject("body")
				           		.field("type", "text")
				           		.field("indexAnalyzer", "ik")
				           		.field("searchAnalyzer", "ik")
			           	   .endObject()
				           .startObject("sid")
				           		.field("type", "long")
				           		.field("index", "not_analyzed")
				           	.endObject()
				           .startObject("type")
				           		.field("type", "text")
				           		.field("index", "not_analyzed")
				           	.endObject()
				           .startObject("sourceId")
				           		.field("type", "long")
				           .endObject()
				           .startObject("pic")
				           		.field("type", "text")
				           .endObject()
	    				.endObject()
	    			.endObject()
	    		.endObject();
		    PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(mappingType).source(builder);
		    client.admin().indices().putMapping(mapping).actionGet();
		}catch(Exception e){
			
		}
		
	}

	 public static void createIndex(String indexName, String typeName, String filePath){
     	 File file = new File(filePath);  
         LinkedHashMap<String, Class<?>> colNames = new LinkedHashMap<String, Class<?>>();  
         colNames.put("id", Long.class);  
         colNames.put("sid", Long.class);
         colNames.put("title", String.class);
         colNames.put("body", String.class); 
         colNames.put("label", String.class);
         colNames.put("pic", String.class);  
         colNames.put("type", Integer.class); 
         colNames.put("sourceId", Long.class); 
         int count = 0;  
         long startTime = System.currentTimeMillis();  
             int currentCount = 0;  
             long startCurrentTime = System.currentTimeMillis();  
             FileReader reader = new FileReader(file, "\\$", colNames);  
             BulkResponse resp = null;  
             BulkRequestBuilder bulkRequest = client.prepareBulk();  
             try  
             {  
                 List<Map<String, Object>> results = reader.readFile(); 
                 for (Map<String, Object> col : results)  
                 {  
                     bulkRequest.add(client.prepareIndex(indexName, typeName)  
                             .setSource(JSON.toJSONString(col)).setId("#"+col.get("id")));  
                     count++;  
                     currentCount++;  
                 }  
                 resp = bulkRequest.execute().actionGet();  
             }  
             catch (Exception e)  
             {  
            	 LOG.error("添加索引内容出现异常 bulkRequest.add(client.prepareIndex(indexName, typeName)..", e.getStackTrace());
             }  
             long endCurrentTime = System.currentTimeMillis();  
             System.out.println("[thread-0"  + "-]per count:" + currentCount);  
             System.out.println("[thread-0"+ "-]per time:"  
                     + (endCurrentTime - startCurrentTime));  
             System.out.println("[thread-0" + "-]per count/s:"  
                     + (float) currentCount / (endCurrentTime - startCurrentTime)  
                     * 1000);  
             System.out.println("[thread-0" + "-]per count/s:"  
                     + resp.toString());  
             long endTime = System.currentTimeMillis();  
             System.out.println("[thread-0" + "-]total count:" + count);  
             System.out.println("[thread-0"  + "-]total time:"  
                    + (endTime - startTime));  
             System.out.println("[thread-0" + "-]total count/s:" + (float) count  
                    / (endTime - startTime) * 1000);  
             client.close();
     }
   
  
    public static void main(String args[])  
    {  
    	/*//创建索引
    	createCluterName(INDEX);
    	//创建文档
    	createMapping(INDEX,TYPE);*/
    	//导入数据
    	createIndex(INDEX,TYPE,FILEPATH);
        
    }

}  

