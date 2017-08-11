package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class BalconyTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "151".equals(id);
    }
}
