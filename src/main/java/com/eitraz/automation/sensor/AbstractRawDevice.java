package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractRawDevice {
    private final static Logger logger = LogManager.getLogger();

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private ApplicationEventPublisher publisher;

    protected RawDeviceEvent lastEvent;
    protected LocalDateTime lastEventTime;

    @EventListener
    public void handle(RawDeviceEvent event) {
        if (matches(event)) {
            // No need to run to often
            if (lastEventTime != null && lastEventTime.plus(Duration.ofMillis(500)).isAfter(LocalDateTime.now()))
                return;

            logger.info("RawDevice: " + getClass().getSimpleName() + ", event: " + event);

            setEvent(event);
            publisher.publishEvent(this);
        }
    }

    protected void setEvent(RawDeviceEvent event) {
        lastEvent = event;
        lastEventTime = LocalDateTime.now();
    }

    public RawDeviceEvent getLastEvent() {
        return lastEvent;
    }

    public LocalDateTime getLastEventTime() {
        return lastEventTime;
    }

    protected abstract boolean matches(RawDeviceEvent event);
}
