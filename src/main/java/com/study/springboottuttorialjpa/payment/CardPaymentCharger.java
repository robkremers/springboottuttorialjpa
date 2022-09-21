package com.study.springboottuttorialjpa.payment;

import com.stripe.exception.StripeException;
import com.study.springboottuttorialjpa.enums.Currency;

import java.math.BigDecimal;

public interface CardPaymentCharger {

    CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    ) throws StripeException;
}
