package com.eitraz.automation.database.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "temperature_humidity_log")
@NamedQueries({
        @NamedQuery(
                name = "temperature_humidity_log.mean_values_hour",
                query = "SELECT sensor,avg(temperature) AS temperature,avg(humidity) AS humidity, min(datetime), hour(datetime) FROM TemperatureHumidityLogEntity t GROUP BY sensor,year(datetime),month(datetime),day(datetime),hour(datetime) ORDER BY min(datetime) DESC"
        ),
        @NamedQuery(
                name = "temperature_humidity_log.list",
                query = "SELECT s FROM TemperatureHumidityLogEntity s ORDER BY s.datetime DESC"
        ),
        @NamedQuery(
                name = "temperature_humidity_log.list_from_timestamp",
                query = "SELECT s FROM TemperatureHumidityLogEntity s WHERE s.datetime >= :fromDateTime ORDER BY s.datetime DESC"
        ),
        @NamedQuery(
                name = "temperature_humidity_log.last_entry",
                query = "SELECT s FROM TemperatureHumidityLogEntity s WHERE s.sensor = :sensorName ORDER BY s.datetime DESC"
        ),
        @NamedQuery(
                name = "temperature_humidity_log.sensor_names",
                query = "SELECT DISTINCT s.sensor FROM TemperatureHumidityLogEntity s"
        ),
        @NamedQuery(
                name ="temperature_humidity_log.last_entry_by_sensor",
                query = "SELECT DISTINCT(s.sensor) AS sensor, s.id AS id, s.datetime AS datetime, s.temperature AS temperature, s.humidity AS humidity FROM TemperatureHumidityLogEntity s ORDER BY s.datetime DESC"
        )
})
public class TemperatureHumidityLogEntity {
    private String sensor;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Timestamp datetime;
    private int id;

    @Basic
    @Column(name = "sensor")
    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    @Basic
    @Column(name = "temperature")
    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    @Basic
    @Column(name = "humidity")
    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    @Basic
    @Column(name = "datetime")
    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemperatureHumidityLogEntity that = (TemperatureHumidityLogEntity) o;

        if (id != that.id) return false;
        if (sensor != null ? !sensor.equals(that.sensor) : that.sensor != null) return false;
        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;
        if (humidity != null ? !humidity.equals(that.humidity) : that.humidity != null) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sensor != null ? sensor.hashCode() : 0;
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (humidity != null ? humidity.hashCode() : 0);
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }
}
