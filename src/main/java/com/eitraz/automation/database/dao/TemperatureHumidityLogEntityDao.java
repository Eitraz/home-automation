package com.eitraz.automation.database.dao;

import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;

import java.util.List;

public interface TemperatureHumidityLogEntityDao {
    void save(TemperatureHumidityLogEntity entity);

    List<TemperatureHumidityLogEntity> list();
}
