package com.eitraz.automation.database.dao;

import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TemperatureHumidityLogEntityDaoImpl implements TemperatureHumidityLogEntityDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public TemperatureHumidityLogEntityDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(TemperatureHumidityLogEntity entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<TemperatureHumidityLogEntity> list() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

session.createQuery("select TemperatureHumidityLogEntity ");

        session.getTransaction().commit();
        session.close();
        return null;
    }
}
