package com.eitraz.automation.condition;

import java.util.Objects;
import java.util.function.Consumer;

public final class Decision {
    private Condition condition;

    public Decision(Condition condition) {
        Objects.requireNonNull(condition);
        this.condition = condition;
    }

    public static Decision decision(Condition condition) {
        return new Decision(condition);
    }

    public Decision and(Condition condition) {
        Condition currentCondition = this.condition;
        this.condition = () -> currentCondition.isTrue() && condition.isTrue();
        return this;
    }

    public Decision or(Condition condition) {
        Condition currentCondition = this.condition;
        this.condition = () -> currentCondition.isTrue() || condition.isTrue();
        return this;
    }

    public boolean isTrue() {
        return condition.isTrue();
    }

    public void then(Consumer<Boolean> consumer) {
        consumer.accept(isTrue());
    }
}
