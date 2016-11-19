package com.eitraz.automation;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationApplication(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Bean(name = "hazelcast")
    public HazelcastInstance hazelcast() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean(name = "tellstickHazelcastCluster")
    @Autowired
    public TellstickHazelcastCluster tellstickHazelcastCluster(HazelcastInstance hazelcast) {
        TellstickHazelcastCluster tellstick = new TellstickHazelcastCluster(hazelcast);

        // Hand over raw events to Spring
        tellstick.addRawDeviceEventListener(publisher::publishEvent);

        return tellstick;
    }

    @Override
    public void run(String... strings) throws Exception {
        // TODO: Keep it going
    }

    public static void main(String[] args) {
        SpringApplication.run(HomeAutomationApplication.class, args);
    }
}
