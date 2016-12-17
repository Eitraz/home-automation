package com.eitraz.automation;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterNode;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.time.Duration;

@Profile("production")
@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {
    private final ApplicationEventPublisher publisher;

    @Autowired
    public HomeAutomationApplication(ApplicationEventPublisher publisher) {
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
    public DataSource mysqlDatasource(@Value("${database.host}") String host,
                                      @Value("${database.port}") int port,
                                      @Value("${database.user}") String user,
                                      @Value("${database.password}") String password) {
        MysqlDataSource mysql = new MysqlDataSource();
        mysql.setServerName(host);
        mysql.setPort(port);
        mysql.setUser(user);
        mysql.setPassword(password);
        return mysql;
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
