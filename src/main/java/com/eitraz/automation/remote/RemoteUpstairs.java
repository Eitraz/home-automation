package com.eitraz.automation.remote;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoteUpstairs extends AbstractRemoteController {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        boolean isRemoteUnit2 = "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "3232178".equals(event.get("house")) &&
                "2".equals(event.get("unit")) &&
                "0".equals(event.get("group"));

        boolean isWallRemoteUnit12 = "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "179518".equals(event.get("house")) &&
                "12".equals(event.get("unit")) &&
                "0".equals(event.get("group"));

        return isRemoteUnit2 || isWallRemoteUnit12;
    }
}
