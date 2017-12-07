package com.gi.xm.es.main;

import com.gi.xm.es.config.SwaggerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.gi.xm.es"})
@Import(SwaggerConfiguration.class)
public class SearchApp {
    private static final Logger LOG = LoggerFactory.getLogger(SearchApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SearchApp.class, args);
    }

} 