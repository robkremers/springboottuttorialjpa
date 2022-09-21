package com.study.springboottuttorialjpa.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TDD example.
 */
class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @Test
    void testShouldValidatePhoneNumber() {
        // Given
        String phoneNumber = "+31464332059";

        // When
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should fail when length is larger than 12")
    void testShouldValidatePhoneNumberWhenIncorrectAndHasLengthBiggerThan12() {
        // Given
        String phoneNumber = "+314643320591234567";

        // When
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should fail when does not start with a + sign")
    void testShouldValidatePhoneNumberWhenDoesNotStartWithPlusSign() {
        // Given
        String phoneNumber = "-314643320591";

        // When
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        // Then
        assertThat(isValid).isFalse();
    }

    /**
     * Parameterized tests
     */

    @ParameterizedTest
    @CsvSource(value = {"+31464332059, true",
            "+314643320591234567, false",
            "-314643320591, false",
            "1234567890, false"
    })
    void testShouldValidatePhoneNumberPT(String phoneNumber, boolean expected) {
        // Given

        // When
        boolean isValid = phoneNumberValidator.test(phoneNumber);
        // Then
        assertThat(isValid).isEqualTo(expected);
    }
}
