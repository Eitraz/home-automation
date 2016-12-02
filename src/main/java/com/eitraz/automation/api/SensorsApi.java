package com.eitraz.automation.api;

import com.eitraz.automation.sensor.AbstractTemperatureAndHumiditySensor;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sensors")
public class SensorsApi {
    private Map<String, Map<String, Object>> sensorValues = new ConcurrentHashMap<>();

    @EventListener
    public void handleSensorUpdate(AbstractTemperatureAndHumiditySensor sensor) {
        sensorValues.put(sensor.getClass().getSimpleName(), new HashMap<String, Object>() {
            {
                put("time", sensor.getLastEventTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                put("temperature", sensor.getTemperature());
                put("humidity", sensor.getHumidity());
            }
        });
    }

    @GetMapping("/")
    public Map<String, Map<String, Object>> sensors() {
        return sensorValues;
    }

    @GetMapping("/{sensor}")
    public Map<String, Object> sensor(@PathVariable String sensor) {
        return sensorValues.getOrDefault(sensor, Collections.emptyMap());
    }
}
