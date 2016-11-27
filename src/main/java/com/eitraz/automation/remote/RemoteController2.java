package com.eitraz.automation.remote;

import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RemoteController2 extends AbstractRawDevice {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return false;
    }

    public Optional<Boolean> isOn() {
        return Optional.empty();
    }

    public Optional<Boolean> isOff() {
        return Optional.empty();
    }
}
