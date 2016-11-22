package com.eitraz.automation.tool;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

public class TimeoutSupplier<T> {
    private final Duration timeout;
    private final Supplier<Optional<T>> supplier;
    private LocalDateTime lastTime;
    private T last;

    public TimeoutSupplier(Duration timeout, Supplier<Optional<T>> supplier) {
        this.timeout = timeout;
        this.supplier = supplier;
    }

    public synchronized T get() {
        LocalDateTime now = LocalDateTime.now();
        if (last == null || lastTime.plus(timeout).isBefore(now) || lastTime.getDayOfYear() != now.getDayOfYear()) {
            supplier.get().ifPresent(t -> {
                last = t;
                lastTime = now;
            });
        }
        return last;
    }
}
