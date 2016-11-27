package com.eitraz.automation;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterNode;
import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationApplication(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Bean(name = "hazelcast")
    public HazelcastInstance hazelcast() {
        Config config = new Config();
        config.setProperty("hazelcast.local.localAddress", System.getProperty("ip"));

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPublicAddress(System.getProperty("ip"));
        networkConfig.setPort(5702);
        networkConfig.setPortAutoIncrement(false);

        return Hazelcast.newHazelcastInstance(config);
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
        // TODO: Remove
        System.setProperty("darksky.apiKey", "8fc2bca342596e1a7d9470fbdfd0583f");

        TellstickHazelcastClusterNode.setSystemIpProperty();
        SpringApplication.run(HomeAutomationApplication.class, args);
    }
}
