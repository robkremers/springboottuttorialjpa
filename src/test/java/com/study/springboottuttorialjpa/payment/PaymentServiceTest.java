package com.study.springboottuttorialjpa.payment;

import com.stripe.exception.StripeException;
import com.study.springboottuttorialjpa.customer.Customer;
import com.study.springboottuttorialjpa.customer.CustomerRepository;
import com.study.springboottuttorialjpa.enums.Currency;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@Slf4j
class PaymentServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
    }

    @Test
    void testChargeCardSuccessfully() throws IllegalAccessException, StripeException {
        // Given
        UUID customerId = UUID.randomUUID();
        // Could have used Optional.of(new Customer()))
        // Customer exists
        given(customerRepository
                .findById(customerId)
        )
                .willReturn(Optional.of(mock(Customer.class)));

        // In PaymentService customerId is set in Step 5. Not during the initialization of the Payment.
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // Card is charged successfully.
        // Mocking step 3 / 4 in PaymentService.
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource()
                , paymentRequest.getPayment().getAmount()
                , paymentRequest.getPayment().getCurrency()
                , paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));
        // When
        paymentService.chargeCard(customerId, paymentRequest);
        // Now we need to mock cardPaymentCharger.chargeCard(); (see class PaymentService; Step 3).

        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
        // The deprecated methoud should be replaced by a set of checks on the methods of the Payment properties.
        // Ignore customerId because at this point the Payment.customerId property has not yet been set.
        assertThat(paymentArgumentCaptorValue)
                .isEqualToIgnoringGivenFields(paymentRequest.getPayment(), "customerId");
        assertThat(paymentArgumentCaptorValue.getSource()).isEqualTo(paymentRequest.getPayment().getSource());
        assertThat(paymentArgumentCaptorValue.getAmount()).isEqualTo(paymentRequest.getPayment().getAmount());
        assertThat(paymentArgumentCaptorValue.getCurrency()).isEqualTo(paymentRequest.getPayment().getCurrency());
        assertThat(paymentArgumentCaptorValue.getDescription()).isEqualTo(paymentRequest.getPayment().getDescription());

        /**
         * Tests step 5 in PaymentService
         * Here in PaymentService Payment.customer_id is set. The result is tested below.
         *
         * paymentArgumentCaptorValue.getCustomerId() ==> tests paymentRequest.getPayment().setCustomerId(customerId);
         */
        assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void testShouldThrowWhenCardIsNotCharged() throws IllegalAccessException, StripeException {
        // Given
        UUID customerId = UUID.randomUUID();
        // Customer exists
        given(customerRepository
                .findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));

        // In PaymentService customerId is set in Step 5. Not during the initialization of the Payment.
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // Card is NOT charged successfully.
        // Mocking step 3 / 4 in PaymentService.
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource()
                , paymentRequest.getPayment().getAmount()
                , paymentRequest.getPayment().getCurrency()
                , paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false));
        // When
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining(String.format("The card is not debited for customer %s", customerId));
        // Then
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void testShouldNotChargeCardAndThrowWhenCurrencyNotSupported() {
        // Given
        UUID customerId = UUID.randomUUID();
        // Customer exists
        given(customerRepository
                .findById(customerId))
                .willReturn(Optional.of(mock(Customer.class)));

        // In PaymentService customerId is set in Step 5. Not during the initialization of the Payment.
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.EUR,
                        "card123xx",
                        "Donation"
                )
        );
        // When
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Currency [%s] not supported", paymentRequest.getPayment().getCurrency()))
        ;

        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void testShouldNotChargeAndShouldThrowWhenCustomerNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();
        // Customer exists
        given(customerRepository
                .findById(customerId))
                .willReturn(Optional.empty());
        // When
        assertThatThrownBy(() -> paymentService.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining(String.format("Customer with id [%s] has not been found", customerId));
        // Then
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }
}