package com.eitraz.automation;

import com.eitraz.automation.device.*;
import com.eitraz.automation.ip.AnkiPC;
import com.eitraz.automation.ip.LivingRoomTv;
import com.eitraz.automation.ip.NetworkDevice;
import com.eitraz.automation.ip.PetterPC;
import com.eitraz.automation.remote.RemoteDownstairs;
import com.eitraz.automation.remote.RemoteUpstairs;
import com.eitraz.automation.sensor.*;
import com.eitraz.automation.tool.Forecast;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.*;

import static com.eitraz.automation.condition.Decision.decision;
import static java.lang.Integer.parseInt;
import static java.time.LocalTime.now;

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Component
public class DeviceAutomation {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private RemoteDownstairs remoteDownstairs;

    @Autowired
    private RemoteUpstairs remoteUpstairs;

    @Autowired
    private Forecast forecast;

    @Autowired
    private LivingRoomTv livingRoomTv;

    @Autowired
    private PetterPC petterPC;

    @Autowired
    private AnkiPC ankiPC;

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
        final boolean remoteDownstairsOn = this.remoteDownstairs.isOn().orElse(false);
        final boolean remoteDownstairsOff = this.remoteDownstairs.isOff().orElse(false);

        boolean specialOn = false;
        boolean forceTvBackLight = false;

        // Christmas special
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));
        if (now.getMonth().equals(Month.DECEMBER) && now.getDayOfMonth() >= 22 && now.getDayOfMonth() <= 25) {
            if (timeIsBetween("07:00", "09:30") || timeIsBetween("15:30", "22:30")) {
                specialOn = true;
            }

            // TV back light
            if (now.getDayOfMonth() == 24 && timeIsBetween("14:45", "15:45")) {
                forceTvBackLight = true;
            }
            else if (timeIsBetween("19:30", "21:45")) {
                forceTvBackLight = true;
            }
        }
        // New years special
        else if ((now.getMonth().equals(Month.DECEMBER) && now.getDayOfMonth() == 31)) {
            if (timeIsBetween("15:30", "23:58")) {
                specialOn = true;
            }
        }
        else if (now.getMonth().equals(Month.JANUARY) && now.getDayOfMonth() == 1) {
            if (timeIsBetween("00:02", "01:00")) {
                specialOn = true;
            }
        }

        boolean ipDeviceIsOn = livingRoomTv.isOn() || petterPC.isOn() || ankiPC.isOn() || specialOn;
        boolean isForceTvBackLight = forceTvBackLight;

        decision(() -> !remoteDownstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30") || ipDeviceIsOn)
                .and(() -> ipDeviceIsOn ||
                        livingRoomMotionSensor.isActive() ||
                        entranceMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive() ||
                        upstairsMotionSensor.isActive() ||
                        timeIsBetween("07:30", "08:30") ||
                        timeIsBetween("16:30", "22:00")
                )
                .or(() -> remoteDownstairsOn)
                .then(isOn -> {
                    setOn(LivingRoomWindow.class, isOn);
                    setOn(EntranceWindow.class, isOn);
                    setOn(Garden.class, isOn);
                });

        decision(() -> !remoteDownstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30") || ipDeviceIsOn)
                .and(() -> ipDeviceIsOn ||
                        livingRoomMotionSensor.isActive() ||
                        entranceMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive()
                )
                .or(() -> remoteDownstairsOn)
                .then(isOn -> {
                    setOn(KitchenWindow.class, isOn);
                    setOn(OfficeWindow.class, isOn);
                    setOn(PlayRoomWindow.class, isOn);
                });

        // Office Vitrine
        decision(() -> !remoteDownstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("13:00", "22:30") || ipDeviceIsOn)
                .and(() -> ipDeviceIsOn ||
                        livingRoomMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive()
                )
                .or(() -> remoteDownstairsOn)
                .then(isOn -> setOn(OfficeVitrine.class, isOn));

        // Linns room
        decision(() -> forecast.sunIsDown())
                .and(() -> (timeIsBetween("6:45", "11:01") && LocalDate.now().getDayOfWeek().getValue() < 6) ||
                        (timeIsBetween("8:00", "11:01") && LocalDate.now().getDayOfWeek().getValue() >= 6)
                        || timeIsBetween("10:59", "19:00")
                )
                .and(() -> livingRoomTv.isOn() ||
                        livingRoomMotionSensor.isActive() ||
                        entranceMotionSensor.isActive() ||
                        kitchenMotionSensor.isActive()
                )
                .then(isOn -> setOn(LinnRoomWindow.class, isOn));

        // TV back light
        decision(() -> !remoteDownstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> livingRoomTv.isOn() || isForceTvBackLight)
                .or(() -> remoteDownstairsOn)
                .then(isOn -> setOn(LivingRoomTvBackLight.class, isOn));

        final boolean remoteUpstairsOn = remoteUpstairs.isOn().orElse(false);
        final boolean remoteUpstairsOff = remoteUpstairs.isOff().orElse(false);

        // Upstairs hallway
        decision(() -> !remoteUpstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> (timeIsBetween("6:45", "11:01") && LocalDate.now().getDayOfWeek().getValue() < 6) ||
                        (timeIsBetween("8:00", "11:01") && LocalDate.now().getDayOfWeek().getValue() >= 6)
                        || timeIsBetween("10:59", "21:30")
                )
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(60)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(60))
                )
                .or(() -> remoteUpstairsOn)
                .then(isOn -> setOn(UpstairsHallway.class, isOn));

        // Kids room
        decision(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("08:00", "11:01") || timeIsBetween("10:59", "18:15"))
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(45)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(45))
                )
                .then(isOn -> setOn(IsakRoomWindow.class, isOn));

        // Stair window
        decision(() -> !remoteUpstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> timeIsBetween("6:00", "11:01") || timeIsBetween("10:59", "22:30"))
                .and(() -> upstairsMotionSensor.isActive() ||
                        upstairsHallwayMotionSensor.isActive() ||
                        entranceMotionSensor.isActive()
                )
                .or(() -> remoteDownstairsOn || remoteUpstairsOn)
                .then(isOn -> setOn(StairWindow.class, isOn));

        // Bedroom
        decision(() -> !remoteUpstairsOff)
                .and(() -> forecast.sunIsDown())
                .and(() -> (timeIsBetween("6:30", "11:01") && LocalDate.now().getDayOfWeek().getValue() < 6) ||
                        (timeIsBetween("8:00", "11:01") && LocalDate.now().getDayOfWeek().getValue() >= 6)
                        || timeIsBetween("10:59", "22:00")
                )
                .and(() -> upstairsMotionSensor.isActive(Duration.ofMinutes(30)) ||
                        upstairsHallwayMotionSensor.isActive(Duration.ofMinutes(30)) ||
                        (timeIsBetween("06:30", "08:00") && LocalDate.now().getDayOfWeek().getValue() < 6)
                )
                .or(() -> remoteUpstairsOn)
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