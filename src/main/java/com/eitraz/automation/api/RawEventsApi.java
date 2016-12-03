package com.eitraz.automation.api;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/rawEvents")
public class RawEventsApi {
    private static final int SIZE = 100;

    private final Deque<Map<String, String>> events = new ArrayDeque<>(SIZE);

    private final TimeoutHandler<Map<String, String>> timeoutHandler = new TimeoutHandler<>(Duration.ofMinutes(1));

    @EventListener
    public void handle(RawDeviceEvent event) {
        Map<String, String> parameters = event.getParameters();
        parameters.remove("timestamp");

        if (timeoutHandler.isReady(parameters, Duration.ofMillis(500))) {
            synchronized (events) {
                parameters.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                events.addFirst(parameters);
                while (events.size() >= SIZE)
                    events.removeLast();
            }
        }
    }

    @GetMapping("/")
    public List<Map<String, String>> events() {
        synchronized (events) {
            return new ArrayList<>(events);
        }
    }
}
