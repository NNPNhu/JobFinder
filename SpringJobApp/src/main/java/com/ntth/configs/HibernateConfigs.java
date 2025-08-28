/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.configs;

import com.ntth.repositories.CompanyRepository;
import com.ntth.repositories.JobCategoryRepository;
import com.ntth.repositories.JobPostingsRepository;
import com.ntth.repositories.UserRepository;
import com.ntth.repositories.impl.CompanyRepositoryImpl;
import com.ntth.repositories.impl.JobCategoryRepositoryImpl;
import com.ntth.repositories.impl.JobPostingsRepositoryImpl;
import com.ntth.repositories.impl.UserRepositoryImpl;
import java.util.Properties;
import javax.sql.DataSource;
import static org.hibernate.cfg.JdbcSettings.DIALECT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author admin
 */
@Configuration
//đọc cấu hình cơ sở dữ liệu từ file database.properties
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
public class HibernateConfigs {

    @Autowired
    private Environment env;//chua tất cả thông tin đọc từ property

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        System.out.println("Creating LocalSessionFactoryBean");
        LocalSessionFactoryBean sessionFactory
                = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan(new String[]{
            "com.ntth.pojo"
        });
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(
                env.getProperty("hibernate.connection.driverClass"));
        dataSource.setUrl(env.getProperty("hibernate.connection.url"));
        dataSource.setUsername(
                env.getProperty("hibernate.connection.username"));
        dataSource.setPassword(
                env.getProperty("hibernate.connection.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put(DIALECT, env.getProperty("hibernate.dialect"));
        props.put("hibernate.show_sql", env.getProperty("hibernate.showSql"));
        return props;
    }

    @Bean//bật giao tác
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(
                sessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public UserRepository userRepository(LocalSessionFactoryBean sessionFactory, BCryptPasswordEncoder passwordEncoder) {
        return new UserRepositoryImpl(sessionFactory, passwordEncoder);
    }

//    @Bean
//    public CompanyRepository companyRepository(LocalSessionFactoryBean sessionFactory) {
//        return new CompanyRepositoryImpl(sessionFactory);
//    }

    @Bean
    public JobPostingsRepository jobpostingRepository(LocalSessionFactoryBean sessionFactory) {
        return new JobPostingsRepositoryImpl(sessionFactory);
    }

//    @Bean
//    public JobCategoryRepository jobcategoryRepository(LocalSessionFactoryBean sessionFactory) {
//        return new JobCategoryRepositoryImpl(sessionFactory);
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
