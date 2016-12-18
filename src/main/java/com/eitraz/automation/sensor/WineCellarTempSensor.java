package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class WineCellarTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "53".equals(id);
    }
}
