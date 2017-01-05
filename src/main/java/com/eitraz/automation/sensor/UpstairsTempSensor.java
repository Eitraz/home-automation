package com.eitraz.automation.sensor;

import org.springframework.stereotype.Component;

@Component
public class UpstairsTempSensor extends AbstractTemperatureAndHumiditySensor {
    @Override
    protected boolean idMatches(String id) {
        return "114".equals(id);
    }
}
