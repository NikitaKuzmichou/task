package com.nikita.kuzmichou.task.common.configs.db;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

public class PostgreSqlDbConfig {
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/task");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setConnectionProperties(this.getConnectionProperties());
        return dataSource;
    }

    private Properties getConnectionProperties() {
        Properties props = new Properties();
        props.setProperty("spring.database.hikari.connectionTimeout", "1740");
        return props;
    }
}
