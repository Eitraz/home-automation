package com.eitraz.automation.ip;

import org.springframework.stereotype.Component;

@Component
public class AnkiPC extends NetworkDevice {

    public AnkiPC() {
        super("192.168.1.42");
    }
}
