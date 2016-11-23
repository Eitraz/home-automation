package com.eitraz.automation.remote;

import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RemoteController2 extends AbstractRawDevice {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Optional<Boolean> isOn() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Optional<Boolean> isOff() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
