package com.eitraz.automation.remote;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoteDownstairs extends AbstractRemoteController {
    @Override
    protected boolean matches(RawDeviceEvent event) {
        boolean isRemoteUnit1 = "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "3232178".equals(event.get("house")) &&
                "1".equals(event.get("unit")) &&
                "0".equals(event.get("group"));

        boolean isWallRemoteUnit11 = "arctech".equals(event.getProtocol()) &&
                "selflearning".equals(event.getModel()) &&
                "179518".equals(event.get("house")) &&
                "11".equals(event.get("unit")) &&
                "0".equals(event.get("group"));

        return isRemoteUnit1 || isWallRemoteUnit11;
    }
}
