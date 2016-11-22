package com.eitraz.automation.remote;

import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoteController1 extends AbstractRawDevice {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return false;
    }

}
