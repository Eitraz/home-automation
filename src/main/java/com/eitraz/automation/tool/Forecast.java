package com.eitraz.automation.tool;

import com.eitraz.darksky.DarkSky;
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

    public boolean sunIsDown() {
        LocalDateTime now = LocalDateTime.now();
        double cloudCover = getCloudCover();
        LocalDateTime sunrise = getSunrise().plusMinutes(new Double(60 * cloudCover).intValue());
        LocalDateTime sunset = getSunset().minusMinutes(new Double(60 * cloudCover).intValue());

        return now.isBefore(sunrise) || now.isAfter(sunset);
    }

    public boolean sunIsUp() {
        return !sunIsDown();
    }
}
