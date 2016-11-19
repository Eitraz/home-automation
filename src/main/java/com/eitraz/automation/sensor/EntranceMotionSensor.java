package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class EntranceMotionSensor extends AbstractRawDevice {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "codeswitch".equals(event.getModel()) &&
                "M".equals(event.get("house"));
    }
}
