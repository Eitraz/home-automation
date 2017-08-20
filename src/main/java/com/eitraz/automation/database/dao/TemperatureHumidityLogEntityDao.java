package com.eitraz.automation.database.dao;

import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureHumidityLogEntityDao {
    void save(TemperatureHumidityLogEntity entity);

    List<TemperatureHumidityLogEntity> list();

    TemperatureHumidityLogEntity lastEntry(String sensorName);

    List<TemperatureHumidityLogEntity> lastEntries();

    List<TemperatureHumidityLogEntity> listFromDateTime(Timestamp fromTimestamp);
}
