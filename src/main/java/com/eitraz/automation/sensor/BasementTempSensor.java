package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class BasementTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "101".equals(id);
    }
}
