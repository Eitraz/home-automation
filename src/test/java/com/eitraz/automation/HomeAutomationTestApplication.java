package com.eitraz.automation;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Profile("test")
@SpringBootApplication
public class HomeAutomationTestApplication extends HomeAutomationApplication {
    private final Environment environment;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationTestApplication(Environment environment, ApplicationEventPublisher publisher) {
        super(environment, publisher);
        this.environment = environment;
        this.publisher = publisher;
    }

    @Override
    @Bean
    public HazelcastInstance hazelcast() {
        return new TestHazelcastInstanceFactory().newHazelcastInstance();
    }

    @Override
    @Bean
    public DataSource datasource(@Value("${database.driver}") String driverClassName,
                                 @Value("${database.url}") String url,
                                 @Value("${database.username}") String username,
                                 @Value("${database.password}") String password) {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("temperature_humidity_log.sql")
                .build();
    }

}
