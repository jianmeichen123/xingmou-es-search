package com.gi.xm.es.main;
  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;  
  
@SpringBootApplication  
@ComponentScan("com.gi.xm.es")
public class AppMain
{  
    public static ConfigurableApplicationContext ctx;  
  
    private static final Logger LOG = LoggerFactory.getLogger(AppMain.class);  
  
    public static void main(String[] args)  
    {  
    	SpringApplication.run(AppMain.class, args);
    }  
  
} 