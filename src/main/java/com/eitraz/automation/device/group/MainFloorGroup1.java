package com.eitraz.automation.device.group;

import com.eitraz.automation.device.*;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MainFloorGroup1 {
    private final List<TellstickHazelcastClusterDevice> devices;

    @Autowired
    public MainFloorGroup1(EntranceWindow entranceWindow,
                           GuestRoomWindow guestRoomWindow,
                           KitchenWindow kitchenWindow,
                           LivingRoomWindow livingRoomWindow,
                           OfficeWindow officeWindow,
                           Garden garden) {
        devices = Arrays.asList(entranceWindow, guestRoomWindow, kitchenWindow, livingRoomWindow, officeWindow, garden);
    }

    public void on() {
        devices.forEach(TellstickHazelcastClusterDevice::on);
    }

    public void off() {
        devices.forEach(TellstickHazelcastClusterDevice::off);
    }
}
