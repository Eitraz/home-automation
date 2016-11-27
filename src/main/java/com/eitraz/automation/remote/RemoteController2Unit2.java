package com.eitraz.automation.remote;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoteController2Unit2 extends AbstractRemoteController {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        return "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "3232178".equals(event.get("house")) &&
                "2".equals(event.get("unit")) &&
                "0".equals(event.get("group"));
    }
}
