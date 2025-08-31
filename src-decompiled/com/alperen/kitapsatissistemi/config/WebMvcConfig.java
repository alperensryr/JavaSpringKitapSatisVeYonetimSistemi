/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.WebMvcConfig
 *  org.springframework.boot.web.server.ErrorPage
 *  org.springframework.boot.web.server.WebServerFactoryCustomizer
 *  org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.http.HttpStatus
 *  org.springframework.web.servlet.config.annotation.ViewControllerRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package com.alperen.kitapsatissistemi.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig
implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/400").setViewName("error/400");
        registry.addViewController("/error/401").setViewName("error/401");
        registry.addViewController("/error/403").setViewName("error/403");
        registry.addViewController("/error/404").setViewName("error/404");
        registry.addViewController("/error/429").setViewName("error/429");
        registry.addViewController("/error/500").setViewName("error/500");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return factory -> factory.addErrorPages(new ErrorPage[]{new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"), new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"), new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"), new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"), new ErrorPage(HttpStatus.TOO_MANY_REQUESTS, "/error/429"), new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"), new ErrorPage("/error/500")});
    }
}

