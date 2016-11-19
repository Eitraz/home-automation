package com.eitraz.automation.device;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDevice extends TellstickHazelcastClusterDevice {
    @Autowired
    public TestDevice(TellstickHazelcastCluster cluster) {
        super(cluster, "TestDevice");
    }
}
