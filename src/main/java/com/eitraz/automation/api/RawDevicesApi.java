package com.eitraz.automation.api;

import com.eitraz.automation.sensor.AbstractRawDevice;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rawDevices")
public class RawDevicesApi {
    @Autowired
    private ApplicationContext context;

    private Map<Class<? extends AbstractRawDevice>, LocalDateTime> lastRawDeviceActivity = new ConcurrentHashMap<>();

    @EventListener
    public void handleSensorUpdate(AbstractRawDevice sensor) {
        lastRawDeviceActivity.put(sensor.getClass(), LocalDateTime.now());
    }

    @GetMapping("/")
    public List<Map<String, Object>> active() {
        return lastRawDeviceActivity
                .entrySet().stream()
                .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .map(entry -> context.getBean(entry.getKey()))
                .map(device -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    RawDeviceEvent event = device.getLastEvent();
                    row.put("type", event.getClass());
                    row.put("parameters", event.getParameters().toString());
                    row.put("lastEventTime", device.getLastEventTime());
                    return row;
                }).collect(Collectors.toList());
    }
}
