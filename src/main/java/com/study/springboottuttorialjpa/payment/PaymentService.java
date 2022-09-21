package com.study.springboottuttorialjpa.payment;

import com.stripe.exception.StripeException;
import com.study.springboottuttorialjpa.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.study.springboottuttorialjpa.general.Constants.ACCEPTED_CURRENCIES;

@Service
@Slf4j
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(
            CustomerRepository customerRepository
            , PaymentRepository paymentRepository
            , CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    public void chargeCard(UUID customerId, PaymentRequest paymentRequest) throws IllegalAccessException {
        // 1. Does customer exist if not throw
        if (!customerRepository.findById(customerId).isPresent()) {
            throw new IllegalAccessException(String.format("Customer with id [%s] has not been found", customerId));
        }

        // 2. Do we support the currency if not throw
        boolean isCurrencySupported = ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency());

        if (!isCurrencySupported) {
            String message = String.format(
                    "Currency [%s] not supported",
                    paymentRequest.getPayment().getCurrency());
            throw new IllegalStateException(message);
        }

        // 3. Charge card
        // At the moment there is no implementation of CardPaymentCharge with these properties yet.
        // But the interface makes this possible.
        CardPaymentCharge cardPaymentCharge = null;
        try {
            cardPaymentCharge = cardPaymentCharger.chargeCard(
                    paymentRequest.getPayment().getSource()
                    , paymentRequest.getPayment().getAmount()
                    , paymentRequest.getPayment().getCurrency()
                    , paymentRequest.getPayment().getDescription());
        } catch (StripeException e) {
            e.printStackTrace();
        }

        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalAccessException(String.format("The card is not debited for customer %s", customerId));
        }
        // 5. Insert payment
        paymentRequest.getPayment().setCustomerId(customerId);
        paymentRepository.save(paymentRequest.getPayment());
        // 6. TODO: send sms
    }

}
