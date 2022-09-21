package com.study.springboottuttorialjpa.payment.stripe;

import com.stripe.exception.StripeException;
import com.study.springboottuttorialjpa.enums.Currency;
import com.study.springboottuttorialjpa.payment.CardPaymentCharge;
import com.study.springboottuttorialjpa.payment.CardPaymentCharger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Annotation Type ConditionalOnProperty
 *
 *
 *     @Retention(value=RUNTIME)
 *      @Target(value={TYPE,METHOD})
 *      @Documented
 *      @Conditional(value=org.springframework.boot.autoconfigure.condition.OnPropertyCondition.class)
 *     public @interface ConditionalOnProperty
 *
 *     The annotations as set below means that if stripe.enabled = false the MockStripeService implementation
 *     will be called and not StripeService.
 *
 *     @Conditional that checks if the specified properties have a specific value.
 */
@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true);
    }
}
