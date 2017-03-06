package com.eitraz.automation.database.dao;

import com.eitraz.automation.HomeAutomationTestApplication;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HomeAutomationTestApplication.class})
public class TemperatureHumidityLogEntityDaoImplTest {
    @Autowired
    private TemperatureHumidityLogEntityDao dao;

    @Test
    public void save() throws Exception {
        TemperatureHumidityLogEntity entity = new TemperatureHumidityLogEntity();
        entity.setSensor("test-sensor");
        entity.setTemperature(BigDecimal.valueOf(-3.4));
        entity.setHumidity(BigDecimal.valueOf(50));

        dao.save(entity);
    }
}