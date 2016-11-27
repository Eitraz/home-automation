package com.eitraz.automation.device.group;

import com.eitraz.automation.device.EntranceWindow;
import com.eitraz.automation.device.Garden;
import com.eitraz.automation.device.LivingRoomWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFloorAlwaysOn extends AbstractDeviceGroup {
    @Autowired
    public MainFloorAlwaysOn(EntranceWindow entranceWindow,
                             LivingRoomWindow livingRoomWindow,
                             Garden garden) {
        super(entranceWindow, livingRoomWindow, garden);
    }
}
