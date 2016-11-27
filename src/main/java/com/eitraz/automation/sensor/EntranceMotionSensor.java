package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class EntranceMotionSensor extends AbstractMotionSensor {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "20782302".equals(event.get("house")) &&
                "10".equals(event.get("unit")) &&
                "00".equals(event.get("group")) &&
                "turnon".equals(event.get("method"));
    }
}
