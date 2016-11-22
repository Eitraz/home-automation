package com.eitraz.automation;

import com.eitraz.automation.device.group.MainFloorGroup1;
import com.eitraz.automation.device.group.MainFloorGroup2;
import com.eitraz.automation.ip.LivingRoomTv;
import com.eitraz.automation.remote.RemoteController1;
import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.automation.sensor.EntranceMotionSensor;
import com.eitraz.automation.sensor.KitchenMotionSensor;
import com.eitraz.automation.sensor.LivingRoomMotionSensor;
import com.eitraz.automation.tool.Forecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Component
public class MainFloor {
    @Autowired
    private MainFloorGroup1 mainFloorGroup1;

    @Autowired
    private MainFloorGroup2 mainFloorGroup2;

    @Autowired
    private RemoteController1 remoteController1;

    @Autowired
    private Forecast forecast;

    @Autowired
    private LivingRoomMotionSensor livingRoomMotionSensor;

    @Autowired
    private KitchenMotionSensor kitchenMotionSensor;

    @Autowired
    private EntranceMotionSensor entranceMotionSensor;

    @Autowired
    private LivingRoomTv tv;

    @EventListener
    public void handleSensorUpdate(AbstractRawDevice sensor) {
        update();
    }

    private void update() {
        if (forecast.sunIsUp()) {
            mainFloorGroup1.off();
            mainFloorGroup2.off();
            return;
        }

        LocalTime morningOn = LocalTime.of(6, 15);

        LocalTime nightOff = LocalTime.of(22, 30);
        LocalTime now = LocalTime.now();
    }
}