package com.gi.xm.es.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * 连接elasticsearch client
 * @author zhangchunyuan
 *
 */
@Configuration
@EntityScan(basePackages = "com.gi.xm.es.pojo")
public class ClientConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    @SuppressWarnings({ "resource", "unchecked" })
	@Bean(name= "client")
    @Autowired
    public Client getClient(@Value("${es.cluster.name}") String clusterName, @Value("${es.client.host}") String host , @Value("${es.client.port}") int port) {
    	Client client = null;
		try {
			   Settings settings = Settings.builder()
					.put("cluster.name", clusterName).build();
			   client = new PreBuiltTransportClient(settings)
				        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
			} catch (UnknownHostException e) {
				LOG.error("---连接client出现异常---",e);
		}
		return client;
    }
}

