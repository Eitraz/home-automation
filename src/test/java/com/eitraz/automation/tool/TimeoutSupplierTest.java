package com.eitraz.automation.tool;

import org.junit.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeoutSupplierTest {
    @Test
    public void testTimeout() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        TimeoutSupplier<Integer> supplier = new TimeoutSupplier<>(Duration.ofSeconds(1), () ->
                Optional.of(counter.getAndAdd(1)));

        assertThat(supplier.get()).isEqualTo(0);
        assertThat(supplier.get()).isEqualTo(0);

        Thread.sleep(1500);
        assertThat(supplier.get()).isEqualTo(1);
        assertThat(supplier.get()).isEqualTo(1);
    }
}