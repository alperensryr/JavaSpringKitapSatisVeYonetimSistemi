/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.KitapSatisSistemiApplication
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.ApplicationRunner
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.domain.EntityScan
 *  org.springframework.context.annotation.Bean
 *  org.springframework.data.jpa.repository.config.EnableJpaRepositories
 */
package com.alperen.kitapsatissistemi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(value={"com.alperen.kitapsatissistemi.entity"})
@EnableJpaRepositories(value={"com.alperen.kitapsatissistemi.repository"})
public class KitapSatisSistemiApplication {
    @Value(value="${server.port:8091}")
    private int serverPort;

    public static void main(String[] args) {
        SpringApplication.run(KitapSatisSistemiApplication.class, (String[])args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {};
    }
}

