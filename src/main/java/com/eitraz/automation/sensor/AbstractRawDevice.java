package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

public abstract class AbstractRawDevice {
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ApplicationEventPublisher publisher;

    private RawDeviceEvent lastEvent;
    private LocalDateTime lastEventTime;

    @EventListener
    public void handle(RawDeviceEvent event) {
        if (matches(event)) {
            lastEvent = event;
            lastEventTime = LocalDateTime.now();
            publisher.publishEvent(this);
        }
    }

    public RawDeviceEvent getLastEvent() {
        return lastEvent;
    }

    public LocalDateTime getLastEventTime() {
        return lastEventTime;
    }

    protected abstract boolean matches(RawDeviceEvent event);
}
