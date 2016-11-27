package com.eitraz.automation.sensor;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractMotionSensor extends AbstractRawDevice {
    public boolean isActive() {
        return isActive(Duration.ofMinutes(30));
    }

    public boolean isActive(Duration timeout) {
        return getLastEventTime() != null && getLastEventTime().plus(timeout).isAfter(LocalDateTime.now());
    }
}
