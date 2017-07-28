package com.eitraz.automation.tool;

import com.eitraz.darksky.DarkSky;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.Instant.ofEpochSecond;

@Component
public class Forecast {
    private static final Logger logger = LogManager.getLogger();

    private Double latitude = 58.038844;
    private Double longitude = 14.959616;

    @Value("${darksky.apiKey}")
    private String apiKey;

    private DarkSky darkSky;

    private TimeoutSupplier<com.eitraz.darksky.data.Forecast> forecast = new TimeoutSupplier<>(Duration.ofMinutes(30), () -> {
        try {
            return Optional.of(darkSky.getForecast(latitude, longitude));
        } catch (IOException e) {
            return Optional.empty();
        }
    });

    @PostConstruct
    public void init() {
        darkSky = new DarkSky(apiKey);
    }

    public LocalDateTime getSunset() {
        Long sunsetTime = forecast.get().getDaily().getData().get(0).getSunsetTime();
        return LocalDateTime.ofInstant(ofEpochSecond(sunsetTime), ZoneId.systemDefault());
    }

    public LocalDateTime getSunrise() {
        Long sunsetTime = forecast.get().getDaily().getData().get(0).getSunriseTime();
        return LocalDateTime.ofInstant(ofEpochSecond(sunsetTime), ZoneId.systemDefault());
    }

    public double getCloudCover() {
        return forecast.get().getCurrently().getCloudCover();
    }

    public double getPrecipitation() {
        return forecast.get().getCurrently().getPrecipIntensity();
    }

    public boolean sunIsDown() {
        LocalDateTime now = LocalDateTime.now();
        double cloudCover = getCloudCover();
        double precipitation = getPrecipitation();
        LocalDateTime sunrise = getSunrise();
        LocalDateTime sunset = getSunset();

        int cloudCoverOffset = new Double(60 * cloudCover).intValue();
        int precipitationOffset = new Double(60 * precipitation).intValue();

        double halfYear = 365d / 2d;
        int monthOffset = new Double((halfYear - Math.abs(now.getDayOfYear() - halfYear)) / halfYear * 45).intValue();

        LocalDateTime sunriseWithOffset = sunrise
                .plusMinutes(cloudCoverOffset)
                .plusMinutes(precipitationOffset)
                .plusMinutes(monthOffset)
                .plusMinutes(30);

        LocalDateTime sunsetWithOffset = sunset
                .minusMinutes(cloudCoverOffset)
                .minusMinutes(precipitationOffset)
                .minusMinutes(monthOffset)
                .minusMinutes(45);

        logger.info("Sunrise: {} ({}), sunset: {} ({}), cloud cover: {}, precipitation: {}, month offset: {}",
                sunrise, sunriseWithOffset, sunset, sunsetWithOffset, cloudCover, precipitation, monthOffset);

        return now.isBefore(sunriseWithOffset) || now.isAfter(sunsetWithOffset);
    }

    public boolean sunIsUp() {
        return !sunIsDown();
    }
}
