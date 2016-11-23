package com.eitraz.automation.device.group;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractDeviceGroup {
    private final List<TellstickHazelcastClusterDevice> devices;

    public AbstractDeviceGroup(TellstickHazelcastClusterDevice... devices) {
        this.devices = Arrays.asList(devices);
    }

    public void on() {
        devices.forEach(TellstickHazelcastClusterDevice::on);
    }

    public void off() {
        devices.forEach(TellstickHazelcastClusterDevice::off);
    }
}
