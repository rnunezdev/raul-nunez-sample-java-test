package com.clip.assesment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "transactionsEntityManagerFactory",
        transactionManagerRef = "transactionsTransactionManager",
        basePackages = {"com.clip.assesment.dao"})
public class DataSourceConfiguration implements ApplicationProperties {
    @Bean(name = "transactionsDBProperties")
    @ConfigurationProperties("spring.datasource")
    public Properties dataSourceProperties() {
        return new Properties();
    }
    @Primary
    @Bean(name="transactionsDataSource")
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig(dataSourceProperties());
        return new HikariDataSource(config);
    }
    @PersistenceContext(unitName = "transactions")
    @Bean(name = "transactionsEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean transactionsEntityManagerFactory(@Qualifier("transactionsDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.clip.assesment.model" });
        em.setPersistenceUnitName("transactions");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }
    @Bean(name = "transactionsTransactionManager")
    @Primary
    public PlatformTransactionManager transactionsTransactionManager(
            @Qualifier("transactionsEntityManagerFactory") EntityManagerFactory transactionsEntityManagerFactory) {
        return new JpaTransactionManager(transactionsEntityManagerFactory);
    }
}