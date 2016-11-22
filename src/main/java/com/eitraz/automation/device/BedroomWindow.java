package com.eitraz.automation.device;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpstairsHallway extends TellstickHazelcastClusterDevice {
    @Autowired
    public UpstairsHallway(TellstickHazelcastCluster cluster) {
        super(cluster, "UpstairsHallway");
    }
}
