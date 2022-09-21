package com.study.springboottuttorialjpa.utils;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

/**
 * For real-life purposes use available validator libraries. e.g. from Google.
 * The following is intended for instructive purposes.
 */
@Service
public class PhoneNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String phoneNumber) {
        return phoneNumber.startsWith("+31") && phoneNumber.length() == 12;
    }
}
