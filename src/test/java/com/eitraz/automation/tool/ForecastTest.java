package com.eitraz.automation.tool;

import com.eitraz.automation.HomeAutomationTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HomeAutomationTestApplication.class})
public class ForecastTest {
    @Autowired
    private Forecast forecast;

    @Test
    public void sunset() throws Exception {
        assertThat(forecast.getSunset()).isAfter(LocalDateTime.of(2016, Month.NOVEMBER, 19, 0, 0));
    }

    @Test
    public void sunrise() throws Exception {
        assertThat(forecast.getSunrise()).isAfter(LocalDateTime.of(2016, Month.NOVEMBER, 19, 0, 0));
    }

    @Test
    public void cloudCover() throws Exception {
        assertThat(forecast.getCloudCover()).isGreaterThanOrEqualTo(0.0);
    }
}