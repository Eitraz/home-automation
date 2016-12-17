package com.eitraz.automation.database;

import com.eitraz.automation.HomeAutomationTestApplication;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import com.eitraz.tellstick.core.rawdevice.events.RawTemperatureHumiditySensorEvent;
import com.jayway.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static com.jayway.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HomeAutomationTestApplication.class})
public class TemperatureHumidityToDatabaseTest {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Test
    public void testTemperatureHumidityToDatabase() throws Exception {
        publisher.publishEvent(new RawTemperatureHumiditySensorEvent(new HashMap<String, String>() {
            {
                put(RawDeviceEvent.MODEL, "temperaturehumidity");
                put(RawDeviceEvent._CLASS, "sensor");
                put("id", "215");
                put("temp", "3.5");
                put("humidity", "53");
            }
        }));

        await().atMost(Duration.TEN_SECONDS).until(() -> {
            jdbcTemplate.query("SELECT * FROM temperature_humidity_log", new RowMapper<SensorTemperatureHumidityValue>() {
                @Override
                public SensorTemperatureHumidityValue mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return null;
                }
            });
        });
    }
}