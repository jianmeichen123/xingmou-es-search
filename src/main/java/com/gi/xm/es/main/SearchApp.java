package com.gi.xm.es.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.gi.xm.es"})
public class SearchApp {
    private static final Logger LOG = LoggerFactory.getLogger(SearchApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class, args);
    }

} 