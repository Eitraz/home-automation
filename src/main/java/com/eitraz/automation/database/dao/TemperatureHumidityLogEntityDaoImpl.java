package com.eitraz.automation.database.dao;

import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class TemperatureHumidityLogEntityDaoImpl implements TemperatureHumidityLogEntityDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public TemperatureHumidityLogEntityDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @Transactional
    public void save(TemperatureHumidityLogEntity entity) {
        getSession().save(entity);
    }

    @Override
    public List<TemperatureHumidityLogEntity> list() {
        return getSession()
                .createNamedQuery("temperature_humidity_log.list", TemperatureHumidityLogEntity.class)
                .setMaxResults(100)
                .getResultList();
    }

    @Override
    public TemperatureHumidityLogEntity lastEntry(String sensorName) {
        return getSession()
                .createNamedQuery("temperature_humidity_log.last_entry", TemperatureHumidityLogEntity.class)
                .setParameter("sensorName", sensorName)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public List<TemperatureHumidityLogEntity> lastEntries() {
        List<String> sensorNames = getSession()
                .createNamedQuery("temperature_humidity_log.sensor_names", String.class)
                .getResultList();

        return sensorNames.stream()
                          .map(this::lastEntry)
                          .collect(Collectors.toList());
    }

    @Override
    public List<TemperatureHumidityLogEntity> listFromDateTime(Timestamp fromTimestamp) {
        return getSession()
                .createNamedQuery("temperature_humidity_log.list_from_timestamp", TemperatureHumidityLogEntity.class)
                .setParameter("fromDateTime", fromTimestamp)
                .getResultList();
    }
}
