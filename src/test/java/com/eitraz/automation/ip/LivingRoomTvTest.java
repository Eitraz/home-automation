package com.eitraz.automation.ip;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LivingRoomTvTest {
    @Test
    public void isOn() throws Exception {
        assertThat(new LivingRoomTv().isOn()).isTrue();
    }
}