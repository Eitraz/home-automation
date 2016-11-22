package com.eitraz.automation.ip;

import org.springframework.stereotype.Component;

@Component
public class LivingRoomTv extends NetworkDevice {

    public LivingRoomTv() {
        super("192.168.1.19");
    }
}
