package com.eitraz.automation.ip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class NetworkDevice {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean isOn = false;

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ApplicationEventPublisher publisher;

    public NetworkDevice(String ip) {
        executor.scheduleAtFixedRate(() -> {
            boolean reachable = isReachable(ip);
            if (reachable != isOn) {
                isOn = reachable;
                publisher.publishEvent(this);
            }
        }, 15, 15, TimeUnit.SECONDS);
    }

    public boolean isOn() {
        return isOn;
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
