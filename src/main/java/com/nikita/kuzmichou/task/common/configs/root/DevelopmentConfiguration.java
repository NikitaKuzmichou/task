package com.nikita.kuzmichou.task.common.configs.root;

import com.nikita.kuzmichou.task.common.configs.db.DbConfiguration;
import com.nikita.kuzmichou.task.common.configs.db.H2DbConfig;
import com.nikita.kuzmichou.task.common.configs.db.PostgreSqlDbConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DevelopmentConfiguration {
    private DbConfiguration dbConfig = new H2DbConfig();

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
                                          DriverManagerDataSource dataSource) {
        LocalContainerEntityManagerFactoryBean
                entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(this.dataSource());
        entityManagerFactoryBean.setPackagesToScan(
                "com.nikita.kuzmichou.task.entities.value"
        );
        entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(this.getJpaProperties());
        return entityManagerFactoryBean;
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = this.dbConfig.dataSource();
        return dataSource;
    }

    private Properties getJpaProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", this.dbConfig.dialect());
        props.setProperty("spring.jpa.hibernate.ddl-auto", "update");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        props.setProperty("logging.level.org.hibernate.SQL", "DEBUG");
        props.setProperty("hibernate.use_sql_comments", "true");
        return props;
    }
}
