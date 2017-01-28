package com.eitraz.automation.device.group;

import com.eitraz.automation.device.LinnRoomWindow;
import com.eitraz.automation.device.KitchenWindow;
import com.eitraz.automation.device.OfficeWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFloorGroup1 extends AbstractDeviceGroup {
    @Autowired
    public MainFloorGroup1(LinnRoomWindow guestRoomWindow,
                           KitchenWindow kitchenWindow,
                           OfficeWindow officeWindow) {
        super(guestRoomWindow, kitchenWindow, officeWindow);
    }
}
