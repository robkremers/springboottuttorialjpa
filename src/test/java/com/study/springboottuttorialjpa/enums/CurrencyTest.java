package com.study.springboottuttorialjpa.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void testDoesExist() {
        assertThat(Currency.doesExist(Currency.GBP.name())).isTrue();
        assertThat(Currency.doesExist(Currency.EUR.name())).isTrue();
        assertThat(Currency.doesExist(Currency.USD.name())).isTrue();
    }

    @Test
    void testDoesNotExist() {
        assertThat(Currency.doesExist("Yen")).isFalse();
    }
}