package com.eitraz.automation.database.dao;

import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
