package com.gi.xm.es.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  


/**
 * 读取文件工具类
 * @author zhangchunyuan
 *
 */
public class FileReader  
{  
  
    private File file;  
    private String splitCharactor;  
    private Map<String, Class<?>> colNames;  
    private static final Logger LOG = LoggerFactory.getLogger(FileReader.class);
  
    /** 
     * @param path 
     *            文件路径 
     * @param fileName 
     *            文件名 
     * @param splitCharactor 
     *            拆分字符 
     * @param colNames 
     *            主键名称 
     */  
    public FileReader(File file, String splitCharactor, Map<String, Class<?>> colNames)  
    {  
        this.file = file;  
        this.splitCharactor = splitCharactor;  
        this.colNames = colNames;  
    }  
  
    /** 
     * 读取文件 
     *  
     * @return 
     * @throws Exception 
     */  
    public List<Map<String, Object>> readFile() throws Exception  
    {  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        if (!file.isFile())  
        {  
            throw new Exception("----该文件不存在---:" + file.getName());  
        }  
        LineIterator  lineIterator = null;  
        try  
        {  
            lineIterator = FileUtils.lineIterator(file, "GBK");  
            while (lineIterator.hasNext())  
            {  
                String line = (String) lineIterator.next();  
                String[] values = line.split(splitCharactor);  
                if (colNames.size() != values.length)  
                {  
                    continue;  
                }  
                Map<String, Object> map = new HashMap<String, Object>();  
                Iterator<Entry<String, Class<?>>> iterator = colNames.entrySet()  
                        .iterator();  
                int count = 0;  
                while (iterator.hasNext())  
                {  
                    Entry<String, Class<?>> entry = iterator.next();  
                    Object value = values[count];
                    if(value.equals("\\N")){
                    	 map.put(entry.getKey(), "");
                    	 continue;
                    }
                    
                	if (!String.class.equals(entry.getValue()))  
                    {  
                        value = entry.getValue().getMethod("valueOf", String.class)  
                                .invoke(null, value);  
                    }  
                	map.put(entry.getKey(), value);
                    count++;  
                }  
                list.add(map);  
            }  
        }  
        catch (IOException e)  
        {  
            LOG.error("----读取文件异常----" + e.toString(), e);  
        }  
        finally  
        {  
            LineIterator.closeQuietly((LineIterator) lineIterator);  
        }  
        return list;  
    }  
}