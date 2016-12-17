package com.eitraz.automation.remote;

import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AbstractRemoteController extends AbstractRawDevice {
    public static final String TURNON = "turnon";
    public static final String TURNOFF = "turnoff";

    private RawDeviceEvent localLastEvent = null;

    public Optional<Boolean> isOn() {
        return isOn(Duration.ofHours(5));
    }

    public Optional<Boolean> isOn(Duration timeout) {
        if (getLastEventTime() != null && getLastEvent() != null && getLastEventTime().plus(timeout).isAfter(LocalDateTime.now()) &&
                TURNON.equals(getMethod(getLastEvent())))
            return Optional.of(true);
        return Optional.empty();
    }

    public Optional<Boolean> isOff() {
        return isOff(Duration.ofHours(5));
    }

    public Optional<Boolean> isOff(Duration timeout) {
        if (getLastEventTime() != null && getLastEvent() != null && getLastEventTime().plus(timeout).isAfter(LocalDateTime.now()) &&
                TURNOFF.equals(getMethod(getLastEvent())))
            return Optional.of(true);
        return Optional.empty();
    }

    private String getMethod(RawDeviceEvent event) {
        return event.get("method");
    }

    @Override
    protected void setEvent(RawDeviceEvent event) {
        // Same method is called again, after at least one minute
        if (getLastEventTime() != null && localLastEvent != null && getMethod(localLastEvent).equals(getMethod(event))) {
            if (getLastEventTime().plus(Duration.ofMinutes(1)).isBefore(LocalDateTime.now())) {
                lastEventTime = LocalDateTime.now();
                lastEvent = null;
            }
        }
        // Use default behaviour
        else {
            super.setEvent(event);
        }

        localLastEvent = event;
    }
}
