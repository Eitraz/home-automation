package com.eitraz.automation.device.group;

import com.eitraz.automation.device.GuestRoomWindow;
import com.eitraz.automation.device.PlayRoomWindow;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MainFloorGroup2 {
    private final List<TellstickHazelcastClusterDevice> devices;

    @Autowired
    public MainFloorGroup2(GuestRoomWindow guestRoomWindow,
                           PlayRoomWindow playRoomWindow) {
        devices = Arrays.asList(guestRoomWindow, playRoomWindow);
    }

    public void on() {
        devices.forEach(TellstickHazelcastClusterDevice::on);
    }

    public void off() {
        devices.forEach(TellstickHazelcastClusterDevice::off);
    }
}
