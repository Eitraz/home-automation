package com.eitraz.automation.database.dao;

import com.eitraz.automation.HomeAutomationTestApplication;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HomeAutomationTestApplication.class})
public class TemperatureHumidityLogEntityDaoImplTest {
    @Autowired
    private TemperatureHumidityLogEntityDao dao;

    @Test
    public void save() throws Exception {
        TemperatureHumidityLogEntity entity = new TemperatureHumidityLogEntity();
        entity.setDatetime(new Timestamp(System.currentTimeMillis()));
        entity.setSensor("test-sensor");
        entity.setTemperature(BigDecimal.valueOf(-3.4));
        entity.setHumidity(BigDecimal.valueOf(50));

        dao.save(entity);

        List<TemperatureHumidityLogEntity> entries = dao.list();
        assertThat(entries).hasSize(1);
        TemperatureHumidityLogEntity entry = entries.get(0);
        assertThat(entry.getSensor()).isEqualTo("test-sensor");
        assertThat(entry.getTemperature()).isEqualTo(new BigDecimal("-3.40"));
        assertThat(entry.getHumidity()).isEqualTo(new BigDecimal("50.00"));
        assertThat(entry.getDatetime()).isBefore(new Date());
    }
}