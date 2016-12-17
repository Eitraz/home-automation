package com.eitraz.automation.database;

import com.eitraz.automation.sensor.AbstractTemperatureAndHumiditySensor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TemperatureHumidityToDatabase {
    private static final Logger logger = LogManager.getLogger();

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TemperatureHumidityToDatabase(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @EventListener
    public void handleSensorUpdate(AbstractTemperatureAndHumiditySensor sensor) {
        logger.info("Insert sensor {} with temperature {} and humidity {}",
                sensor.getClass().getSimpleName(),
                sensor.getTemperature(),
                sensor.getHumidity());

        jdbcTemplate.update(
                "INSERT INTO temperature_humidity_log (sensor, temperature, humidity) VALUES(:sensor, :temperature, :humidity)",
                new MapSqlParameterSource()
                        .addValue("sensor", sensor.getClass().getSimpleName())
                        .addValue("temperature", sensor.getTemperature())
                        .addValue("humidity", sensor.getHumidity()));

    }
}
