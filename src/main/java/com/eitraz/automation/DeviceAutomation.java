package com.eitraz.automation;

import com.eitraz.automation.device.*;
import com.eitraz.automation.ip.LivingRoomTv;
import com.eitraz.automation.ip.NetworkDevice;
import com.eitraz.automation.remote.RemoteController1Unit1;
import com.eitraz.automation.remote.RemoteController1Unit2;
import com.eitraz.automation.sensor.*;
import com.eitraz.automation.tool.Forecast;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.eitraz.automation.condition.Decision.decision;
import static java.lang.Integer.parseInt;
import static java.time.LocalTime.now;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Component
public class DeviceAutomation {
    @Autowired
    private RemoteController1Unit1 remoteController1;

    @Autowired
    private RemoteController1Unit2 remoteController1Unit2;

    @Autowired
    private Forecast forecast;

    @Autowired
    private LivingRoomTv livingRoomTv;

    @Autowired
    private LivingRoomMotionSensor livingRoomMotionSensor;

    @Autowired
    private KitchenMotionSensor kitchenMotionSensor;

    @Autowired
    private EntranceMotionSensor entranceMotionSensor;

    @Autowired
    private UpstairsHallwayMotionSensor upstairsHallwayMotionSensor;

    @Autowired
    private UpstairsMotionSensor upstairsMotionSensor;

    @Autowired
    private ApplicationContext context;

    @SuppressWarnings("UnusedParameters")
    @EventListener
    public void handleSensorUpdate(AbstractRawDevice sensor) {
        update();
    }

    @SuppressWarnings("UnusedParameters")
    @EventListener
    public void handleNetworkDeviceUpdate(NetworkDevice device) {
        update();
    }

    private synchronized void update() {
        final boolean remote1ForceOn = remoteController1.isOn().orElse(false);
        final boolean remote1ForceOff = remoteController1.isOff().orElse(false);

        decision(() -> remote1ForceOn)
                .or(() -> !remote1ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30") || livingRoomTv.isOn())
                .and(() -> livingRoomTv.isOn() ||
                        livingRoomMotionSensor.isActive() ||
                        entranceMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive() ||
                        upstairsMotionSensor.isActive() ||
                        timeIsBetween("07:30", "08:30") ||
                        timeIsBetween("16:30", "22:00")
                )
                .then(isOn -> {
                    setOn(LivingRoomWindow.class, isOn);
                    setOn(EntranceWindow.class, isOn);
                    setOn(Garden.class, isOn);
                });

        decision(() -> remote1ForceOn)
                .or(() -> !remote1ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30") || livingRoomTv.isOn())
                .and(() -> livingRoomTv.isOn() ||
                        livingRoomMotionSensor.isActive() ||
                        entranceMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive()
                )
                .then(isOn -> {
                    setOn(KitchenWindow.class, isOn);
                    setOn(GuestRoomWindow.class, isOn);
                    setOn(OfficeWindow.class, isOn);
                    setOn(PlayRoomWindow.class, isOn);
                });

        // TV back light
        decision(() -> remote1ForceOn)
                .or(() -> !remote1ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> livingRoomTv.isOn())
                .then(isOn -> setOn(LivingRoomTvBackLight.class, isOn));

        final boolean remote2ForceOn = remoteController1Unit2.isOn().orElse(false);
        final boolean remote2ForceOff = remoteController1Unit2.isOff().orElse(false);

        // Upstairs hallway
        decision(() -> remote2ForceOn)
                .or(() -> !remote2ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> (timeIsBetween("6:45", "11:01") && LocalDate.now().getDayOfWeek().getValue() < 6) ||
                        (timeIsBetween("8:00", "11:01") && LocalDate.now().getDayOfWeek().getValue() >= 6)
                        || timeIsBetween("10:59", "20:30")
                )
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(45)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(45))
                )
                .then(isOn -> setOn(UpstairsHallway.class, isOn));

        // Kids room
        decision(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("08:00", "11:01") || timeIsBetween("10:59", "18:00"))
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(45)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(45))
                )
                .then(isOn -> setOn(KidsRoomWindow.class, isOn));

        // Stair window
        decision(() -> remote1ForceOn || remote2ForceOn)
                .or(() -> !remote2ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30"))
                .and(() -> upstairsMotionSensor.isActive() ||
                        upstairsHallwayMotionSensor.isActive() ||
                        entranceMotionSensor.isActive()
                )
                .then(isOn -> setOn(StairWindow.class, isOn));

        // Bedroom
        decision(() -> remote1ForceOn || remote2ForceOn)
                .or(() -> !remote2ForceOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> (timeIsBetween("6:30", "11:01") && LocalDate.now().getDayOfWeek().getValue() < 6) ||
                        (timeIsBetween("8:00", "11:01") && LocalDate.now().getDayOfWeek().getValue() >= 6)
                        || timeIsBetween("10:59", "22:00")
                )
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(30)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(30)) ||
                        (timeIsBetween("06:30", "08:00") && LocalDate.now().getDayOfWeek().getValue() < 6)
                )
                .then(isOn -> setOn(BedroomWindow.class, isOn));
    }

    private static boolean timeIsBetween(String start, String end) {
        return now().isAfter(time(start)) && now().isBefore(time(end));
    }

    private static LocalTime time(String time) {
        String[] t = time.split(":");
        return LocalTime.of(parseInt(t[0]), parseInt(t[1]));
    }

    private void setOn(Class<? extends TellstickHazelcastClusterDevice> deviceClass, boolean on) {
        context.getBean(deviceClass).setOn(on);
    }
}