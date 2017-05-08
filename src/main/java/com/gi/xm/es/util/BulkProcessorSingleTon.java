package com.gi.xm.es.util;

import org.elasticsearch.action.bulk.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zcy on 17-5-4.
 */
public enum BulkProcessorSingleTon {
    INSTANCE;
    public Client getClient(){
        Client client = null;
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch").build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.9.130.135"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
    public BulkProcessor getInstance() {
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                getClient(),
                new BulkProcessor.Listener() {
                    //批量成功后执行
                    public void afterBulk(long l, BulkRequest bulkRequest,
                                          BulkResponse bulkResponse) {
                        System.out.println(Thread.currentThread().getName()+"请求数量："+ bulkRequest.numberOfActions());
                        if (bulkResponse.hasFailures()) {
                            for (BulkItemResponse item :
                                    bulkResponse.getItems()) {
                                if (item.isFailed()) {
                                    System.out.println("失败信息:--------" +
                                            item.getFailureMessage());
                                }
                            }
                        }
                    }

                    //批量提交之前执行
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                    }

                    //批量失败后执行
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        System.out.println("happen fail = " +
                                failure.getMessage() + " , cause = " + failure.getCause());
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(
                                TimeValue.timeValueMillis(100), 3))
                .setConcurrentRequests(1)
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .build();
        return bulkProcessor;
    }

}
