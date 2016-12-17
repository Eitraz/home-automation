package com.eitraz.automation;


import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.TestHazelcastInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.Duration;

@Profile("test")
@SpringBootApplication
public class HomeAutomationTestApplication {
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationTestApplication(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Bean
    public HazelcastInstance hazelcast() {
        return new TestHazelcastInstanceFactory().newHazelcastInstance();
    }

    @Bean
    @Autowired
    public TellstickHazelcastCluster tellstickHazelcastCluster(HazelcastInstance hazelcastInstance) {
        TellstickHazelcastCluster tellstick = new TellstickHazelcastCluster(hazelcastInstance, Duration.ofSeconds(2));

        // Hand over raw events to Spring
        tellstick.addRawDeviceEventListener(publisher::publishEvent);

        return tellstick;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("temperature_humidity_log.sql")
                .build();
    }
}
