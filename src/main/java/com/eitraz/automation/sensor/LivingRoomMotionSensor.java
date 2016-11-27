package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class LivingRoomMotionSensor extends AbstractMotionSensor {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "codeswitch".equals(event.getModel()) &&
                "E".equals(event.get("house"));
    }
}
