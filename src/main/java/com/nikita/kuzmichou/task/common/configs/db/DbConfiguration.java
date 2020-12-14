package com.nikita.kuzmichou.task.common.configs.db;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public interface DbConfiguration {
    DriverManagerDataSource dataSource();

    String dialect();
}
