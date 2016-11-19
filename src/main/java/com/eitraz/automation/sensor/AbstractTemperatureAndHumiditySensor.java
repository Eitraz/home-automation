package com.eitraz.automation.sensor;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;

public abstract class AbstractTemperatureAndHumiditySensor extends AbstractRawDevice {
    public Double getTemperature() {
        return Double.parseDouble(getLastEvent().get("temp"));
    }

    public Double getHumidity() {
        return Double.parseDouble(getLastEvent().get("humidity"));
    }

    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "temperaturehumidity".equals(event.getModel()) &&
                "sensor".equals(event.get_Class()) &&
                idMatches(event.get("id"));
    }

    protected abstract boolean idMatches(String id);
}
