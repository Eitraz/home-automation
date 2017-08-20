package com.eitraz.automation;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterNode;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Properties;

@Profile("production")
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class HomeAutomationApplication implements CommandLineRunner {
    private final Environment environment;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationApplication(Environment environment, ApplicationEventPublisher publisher) {
        this.environment = environment;
        this.publisher = publisher;
    }

    @Bean
    public HazelcastInstance hazelcast() {
        Config config = new Config();
        config.setProperty("hazelcast.local.localAddress", System.getProperty("ip"));

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPublicAddress(System.getProperty("ip"));
        networkConfig.setPort(5702);
        networkConfig.setPortAutoIncrement(false);

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    @Autowired
    public TellstickHazelcastCluster tellstickHazelcastCluster(HazelcastInstance hazelcast) {
        TellstickHazelcastCluster tellstick = new TellstickHazelcastCluster(hazelcast, Duration.ofSeconds(2));

        // Hand over raw events to Spring
        tellstick.addRawDeviceEventListener(publisher::publishEvent);

        return tellstick;
    }

    @Bean
    public DataSource datasource(@Value("${database.driver}") String driverClassName,
                                 @Value("${database.url}") String url,
                                 @Value("${database.username}") String username,
                                 @Value("${database.password}") String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.eitraz.automation.database.model");
        factory.setHibernateProperties(getHibernateProperties());
        return factory;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DIALECT, environment.getRequiredProperty("hibernate.dialect"));
        properties.put(AvailableSettings.SHOW_SQL, environment.getRequiredProperty("hibernate.show_sql"));
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, environment.getRequiredProperty("hibernate.batch.size"));
//        properties.put(AvailableSettings.HBM2DDL_AUTO, environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, environment.getRequiredProperty("hibernate.current.session.context.class"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Override
    public void run(String... strings) throws Exception {
    }

    public static void main(String[] args) {
        TellstickHazelcastClusterNode.setSystemIpProperty();

        //TimeZone.setDefault(TimeZone.getTimeZone("Europe/Stockholm"));
        SpringApplication.run(HomeAutomationApplication.class, args);
    }
}
