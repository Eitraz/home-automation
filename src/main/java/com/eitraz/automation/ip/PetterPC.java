package com.eitraz.automation.ip;

import org.springframework.stereotype.Component;

@Component
public class PetterPC extends NetworkDevice {

    public PetterPC() {
        super("192.168.1.41");
    }
}
