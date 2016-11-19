package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class LivingRoomTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "215".equals(id);
    }
}
