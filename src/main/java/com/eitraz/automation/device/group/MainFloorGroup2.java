package com.eitraz.automation.device.group;

import com.eitraz.automation.device.LinnRoomWindow;
import com.eitraz.automation.device.PlayRoomWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFloorGroup2 extends AbstractDeviceGroup {
    @Autowired
    public MainFloorGroup2(LinnRoomWindow guestRoomWindow,
                           PlayRoomWindow playRoomWindow) {
        super(guestRoomWindow, playRoomWindow);
    }
}
