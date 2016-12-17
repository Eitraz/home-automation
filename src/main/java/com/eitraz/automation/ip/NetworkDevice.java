package com.eitraz.automation.ip;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class NetworkDevice {
    private static final Logger logger = LogManager.getLogger();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private LocalDateTime lastChange = LocalDateTime.now().minus(Duration.ofMinutes(30));
    private boolean isOn = false;

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ApplicationEventPublisher publisher;

    public NetworkDevice(String ip) {
        executor.scheduleAtFixedRate(() -> {
            boolean reachable = isReachable(ip);
            if (reachable != isOn) {
                logger.info("NetworkDevice '{}' ({}) is on: {}", getClass().getSimpleName(), ip, reachable);
                lastChange = LocalDateTime.now();
                isOn = reachable;
                publisher.publishEvent(this);
            }
        }, 15, 15, TimeUnit.SECONDS);
    }

    public boolean isOn() {
        // Return "on" for another 10 minutes after switching to off
        if (!isOn && getLastChange().plus(Duration.ofMinutes(10)).isAfter(LocalDateTime.now())) {
            return true;
        }
        return isOn;
    }

    public LocalDateTime getLastChange() {
        return lastChange;
    }

    private static boolean isReachable(String ip) {
        try {
            return InetAddress.getByName(ip).isReachable(5000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
