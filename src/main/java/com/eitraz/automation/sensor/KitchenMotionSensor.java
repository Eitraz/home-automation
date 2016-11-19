package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class KitchenMotionSensor extends AbstractRawDevice {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "8040222".equals(event.get("house")) &&
                "10".equals(event.get("unit")) &&
                "turnon".equals(event.get("method"));
    }
}
