package com.eitraz.automation;

import com.eitraz.automation.database.dao.TemperatureHumidityLogEntityDao;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import com.eitraz.automation.sensor.AbstractTemperatureAndHumiditySensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
public class PersistTemperatureAndHumidity {
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
        entity.setDatetime(Timestamp.valueOf(sensor.getLastEventTime()));
        entity.setTemperature(new BigDecimal(sensor.getTemperature()));
        entity.setHumidity(new BigDecimal(sensor.getHumidity()));

        dao.save(entity);
    }
}
