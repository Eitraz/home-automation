package com.eitraz.automation.remote;

import com.eitraz.automation.sensor.AbstractRawDevice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class AbstractRemoteController extends AbstractRawDevice {
    public Optional<Boolean> isOn() {
        return isOn(Duration.ofHours(5));
    }

    public Optional<Boolean> isOn(Duration timeout) {
        if (getLastEventTime() != null && getLastEventTime().plus(timeout).isAfter(LocalDateTime.now()) &&
                "turnon".equals(getLastEvent().get("method")))
            return Optional.of(true);
        return Optional.empty();
    }

    public Optional<Boolean> isOff() {
        return isOff(Duration.ofHours(5));
    }

    public Optional<Boolean> isOff(Duration timeout) {
        if (getLastEventTime() != null && getLastEventTime().plus(timeout).isAfter(LocalDateTime.now()) &&
                "turnoff".equals(getLastEvent().get("method")))
            return Optional.of(true);
        return Optional.empty();
    }
}
