package com.eitraz.automation.ip;

import com.eitraz.automation.tool.TimeoutSupplier;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Optional;

public abstract class NetworkDevice {
    private TimeoutSupplier<Boolean> isOn;

    public NetworkDevice(String ip) {
        isOn = new TimeoutSupplier<>(Duration.ofMinutes(1), () -> {
            try {
                return Optional.of(InetAddress.getByName(ip).isReachable(5000));
            } catch (IOException e) {
                e.printStackTrace();
                return Optional.of(false);
            }
        });
    }

    public boolean isOn() {
        return isOn.get();
    }
}
