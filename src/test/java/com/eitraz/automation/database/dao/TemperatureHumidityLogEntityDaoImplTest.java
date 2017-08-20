package com.eitraz.automation.database.dao;

import com.eitraz.automation.HomeAutomationTestApplication;
import com.eitraz.automation.api.TemperatureApi;
import com.eitraz.automation.database.model.TemperatureHumidityLogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HomeAutomationTestApplication.class})
public class TemperatureHumidityLogEntityDaoImplTest {
    @Autowired
    private TemperatureHumidityLogEntityDao dao;

    @Autowired
    private TemperatureApi api;

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

    @Test
    public void getLastEntry() throws Exception {
        TemperatureHumidityLogEntity entry = dao.lastEntry("BalconyTempSensor");
        System.out.println(entry.getSensor() + ", " + entry.getId() + ", " + entry.getDatetime() + ", " + entry.getTemperature() + ", " + entry.getHumidity());
    }


    @Test
    public void getLastEntryBySensor() throws Exception {
        List<TemperatureHumidityLogEntity> entries = dao.lastEntries();

        entries.forEach(e -> System.out.println(e.getSensor() + ", " + e.getDatetime() + ", " + e.getTemperature()));
    }

    @Test
    public void getLastEntryFromDateTime() throws Exception {
        List<TemperatureHumidityLogEntity> entries = dao.listFromDateTime(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60 * 12));
        //entries.forEach(e -> System.out.println(e.getSensor() + ", " + e.getDatetime() + ", " + e.getTemperature()));

        Map<String, List<TemperatureHumidityLogEntity>> groupedBySensor = entries
                .stream()
                .collect(Collectors.groupingBy(TemperatureHumidityLogEntity::getSensor));

        long dateTimeGroupDivider = 1000;   // Sec
        dateTimeGroupDivider *= 60; // Min
        dateTimeGroupDivider *= 60 / 4; // 15 min
        //dateTimeGroupDivider *= 60 / 2; // Half hour
        //dateTimeGroupDivider *= 60; // Hour

        long lambdaDateTimeGroupDivider = dateTimeGroupDivider;

        Map<String, Map<LocalDateTime, Double>> averageTemperatureByDateTime = groupedBySensor
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,

                        // Group by date time divider (for example minute, hour or day)
                        e -> e.getValue().stream()
                              .collect(Collectors.groupingBy(t -> LocalDateTime.ofInstant(
                                      Instant.ofEpochMilli(Math.round(t.getDatetime().getTime() / (double) lambdaDateTimeGroupDivider) * lambdaDateTimeGroupDivider), ZoneId.systemDefault()
                              )))

                              // Calculate average value for group
                              .entrySet().stream()
                              .collect(Collectors.toMap(
                                      Map.Entry::getKey,
                                      d -> d.getValue().stream()
                                            .map(dt -> dt.getTemperature().doubleValue())
                                            .mapToDouble(dv -> dv)
                                            .average().orElse(0d)
                              ))
                ));

        averageTemperatureByDateTime.forEach((key, value) -> {
            System.out.println("===== " + key + " =====");
            new TreeMap<>(value).forEach((key1, value1) -> System.out.println(key1 + " - " + value1));
        });
    }

    @Test
    public void apiTest() throws Exception {
        Map<String, List<Map<String, Object>>> hour = api.history("1h");

        hour.forEach((key, value) -> {
            System.out.println("===== " + key + " =====");
            value.forEach(System.out::println);
        });
    }
}