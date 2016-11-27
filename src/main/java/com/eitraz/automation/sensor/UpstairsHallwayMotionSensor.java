package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class UpstairsHallwayMotionSensor extends AbstractMotionSensor {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "8090342".equals(event.get("house")) &&
                "0".equals(event.get("group")) &&
                "10".equals(event.get("unit")) &&
                "turnon".equals(event.get("method"));
    }
}
