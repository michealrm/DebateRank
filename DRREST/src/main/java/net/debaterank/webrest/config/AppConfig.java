package net.debaterank.webrest.config;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

@Configuration
@EntityScan("net.debaterank.webrest")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${spring.datasource.driverClassName}") String driverClassName;
    @Value("${spring.datasource.url}") String url;
    @Value("${spring.datasource.username}") String username;
    @Value("${spring.datasource.password}") String password;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DataSource dataSource = DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory( DataSource dataSource )
    {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource( dataSource );
        entityManagerFactory.setPackagesToScan("net.debaterank.webrest");
        entityManagerFactory.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        return entityManagerFactory;
    }

}
