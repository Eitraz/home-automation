package com.eitraz.automation.device;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StairWindow extends TellstickHazelcastClusterDevice {
    @Autowired
    public StairWindow(TellstickHazelcastCluster cluster) {
        super(cluster, "StairWindow");
    }
}
