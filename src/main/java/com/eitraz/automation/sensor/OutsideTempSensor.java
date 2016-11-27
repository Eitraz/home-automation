package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class OutsideTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "31".equals(id);
    }
}
