package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    public DataSource routingDataSource() {
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put("master", masterDataSource());
//        targetDataSources.put("slave", slaveDataSource());
//
//        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
//        routingDataSource.setTargetDataSources(targetDataSources);
//        routingDataSource.setDefaultTargetDataSource(masterDataSource());
//
//        return routingDataSource;
//    }
}
