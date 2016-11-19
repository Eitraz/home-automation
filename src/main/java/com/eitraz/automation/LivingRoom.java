package com.eitraz.automation;

import com.eitraz.automation.device.TestDevice;
import com.eitraz.automation.sensor.LivingRoomMotionSensor;
import com.eitraz.automation.sensor.LivingRoomTempSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Component
public class LivingRoom {
    @Autowired
    private TestDevice testDevice;

    @Autowired
    private LivingRoomMotionSensor livingRoomMotionSensor;

    @EventListener
    public void handleSensorUpdate(LivingRoomTempSensor sensor) {
        Double temperature = sensor.getTemperature();
        System.out.println("Living room temperature is " + temperature);

        if (temperature < 22.8)
            testDevice.on();
        else
            testDevice.off();
    }
}