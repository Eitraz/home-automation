package com.eitraz.automation.condition;

import org.junit.Test;

import static com.eitraz.automation.condition.Decision.decision;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DecisionTest {
    public static final Condition TRUE = () -> true;
    public static final Condition FALSE = () -> false;

    @Test
    public void testWhen() throws Exception {
        assertTrue(TRUE.isTrue());
        assertTrue(decision(TRUE).and(TRUE).isTrue());
        assertTrue(decision(FALSE).or(TRUE).isTrue());
        assertTrue(decision(TRUE).or(FALSE).isTrue());
        assertTrue(decision(FALSE).and(FALSE).or(TRUE).and(TRUE).isTrue());

        assertFalse(decision(TRUE).and(FALSE).isTrue());
        assertFalse(decision(FALSE).or(FALSE).isTrue());
        assertFalse(decision(FALSE).and(FALSE).or(TRUE).and(FALSE).isTrue());

        assertTrue(decision(FALSE).and(FALSE).or(FALSE).and(FALSE).or(TRUE).isTrue());
        assertTrue(decision(TRUE).or(FALSE).or(FALSE).isTrue());
    }
}