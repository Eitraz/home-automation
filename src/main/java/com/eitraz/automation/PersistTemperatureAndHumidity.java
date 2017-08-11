package com.eitraz.automation;

import com.eitraz.automation.database.dao.TemperatureHumidityLogEntityDao;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import com.eitraz.automation.sensor.AbstractTemperatureAndHumiditySensor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class PersistTemperatureAndHumidity {
    private static final Logger logger = LogManager.getLogger();

    private final TemperatureHumidityLogEntityDao dao;

    @Autowired
    public PersistTemperatureAndHumidity(TemperatureHumidityLogEntityDao dao) {
        this.dao = dao;
    }

    @EventListener
    @Async
    public void handleSensorUpdate(AbstractTemperatureAndHumiditySensor sensor) {
        TemperatureHumidityLogEntity entity = new TemperatureHumidityLogEntity();
        entity.setSensor(sensor.getClass().getSimpleName());
        entity.setDatetime(Timestamp.from(sensor.getLastEventTime().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setTemperature(new BigDecimal(sensor.getTemperature()));
        entity.setHumidity(new BigDecimal(sensor.getHumidity()));

        logger.debug("Persisting entity: " + entity.getSensor() +
                ", temperature: " + entity.getTemperature() +
                ", humidity: " + entity.getHumidity() +
                ", date/time: " + entity.getDatetime());

        dao.save(entity);
    }
}
