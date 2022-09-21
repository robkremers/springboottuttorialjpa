package com.study.springboottuttorialjpa.payment.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.study.springboottuttorialjpa.enums.Currency;
import com.study.springboottuttorialjpa.payment.CardPaymentCharge;
import com.study.springboottuttorialjpa.payment.CardPaymentCharger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Resources:
 * - https://stripe.com/docs/api/authentication?lang=java
 *   - Authentication
 *
 * ConditionalOnProperty
 *  The annotations as set below means that if stripe.enabled = true the StripeService implementation
 *  will be called and not StripeService.
 */
@Service
@Slf4j
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "true"
)
public class StripeService implements CardPaymentCharger {

    @Autowired
    private final StripeApi stripeApi;

    // Should actually be set in a property file.
    private static final RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
            .build();

    public StripeService(StripeApi stripeApi) {
        this.stripeApi = stripeApi;
    }

    @Override
    public CardPaymentCharge chargeCard(
            String cardSource,
            BigDecimal amount,
            Currency currency,
            String description
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", cardSource);
        params.put("description", description);

        try {
            // Charge.create is a static method.
            // Problem: it is not possible to mock a static class.
            /**
             * Charge.create is a static method.
             * Problem: it is not possible to mock a static class.
             * Methods to solve this:
             * - https://github.com/powermock/powermock/wiki/Mockito
             *   - Can deal with static methods.
             * - Create a separate StripeAPI class.
             */
            Charge charge = stripeApi.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e) {
            throw new IllegalStateException("Cannot make stripe charge", e);
        }
    }
}
