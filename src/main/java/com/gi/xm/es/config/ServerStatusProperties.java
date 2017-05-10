package com.gi.xm.es.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

/**
 * Created by vincent on 16-9-9.
 */
@ConfigurationProperties
public class ServerStatusProperties {

    static ErrorPage ernror404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new EmbeddedServletContainerCustomizer(){
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(ernror404);
                container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
                container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
                container.addErrorPages(new ErrorPage(Throwable.class,"/error/500"));

            }
        };
    }
}
