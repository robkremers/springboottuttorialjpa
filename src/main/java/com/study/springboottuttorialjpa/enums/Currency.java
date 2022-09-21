package com.study.springboottuttorialjpa.enums;

import antlr.StringUtils;

import java.util.Arrays;

public enum Currency {

    USD,
    GBP,
    EUR;

    public static Boolean doesExist(String name) {

        for (Currency currency: Currency.values()) {
            if (currency.name().equals(name)) {
                return true;
            }
        }
        return false;

//        Arrays.stream(Currency.values()).anyMatch( currency -> {
//            if (currency.name().equals(name)) {
//                return true;
//            }
//            return false;
//        });
//        return false;
    }
}
