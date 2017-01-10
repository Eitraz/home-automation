package com.eitraz.automation.device;

import com.eitraz.tellstick.hazelcast.TellstickHazelcastCluster;
import com.eitraz.tellstick.hazelcast.TellstickHazelcastClusterDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfficeVitrine extends TellstickHazelcastClusterDevice {
    @Autowired
    public OfficeVitrine(TellstickHazelcastCluster cluster) {
        super(cluster, "OfficeVitrine");
    }
}
