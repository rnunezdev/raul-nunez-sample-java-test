package com.clip.assesment.config;

import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

public interface ApplicationProperties {

    HikariDataSource dataSource();
    default Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }
}
