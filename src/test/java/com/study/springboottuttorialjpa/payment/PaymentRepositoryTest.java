package com.study.springboottuttorialjpa.payment;

import com.study.springboottuttorialjpa.payment.Payment;
import com.study.springboottuttorialjpa.enums.Currency;
import com.study.springboottuttorialjpa.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void testShouldInsertPayment() {
        // Given
        Payment payment = new Payment(
                1L
                , UUID.randomUUID()
                , new BigDecimal("10.00")
                , Currency.USD
                , "card123"
                , "Donation");

        // When
        paymentRepository.save(payment);
        // Then
        Optional<Payment> paymentOptional = paymentRepository.findById(1L);

        assertThat(paymentOptional)
                .isPresent()
                .hasValueSatisfying( p -> {
                    /**
                     * In order to let isEqualTo to pass successfully Payment.equal and Payment.hashcode
                     * need to be present.
                     */
                    assertThat(p).isEqualTo(payment);
                    assertThat(p.getPaymentId()).isEqualTo(payment.getPaymentId());
                    assertThat(p.getCustomerId()).isEqualTo(payment.getCustomerId());
                    assertThat(p.getAmount()).isEqualTo(payment.getAmount());
                    assertThat(p.getCurrency()).isEqualTo(payment.getCurrency());
                    assertThat(p.getSource()).isEqualTo(payment.getSource());
                    assertThat(p.getDescription()).isEqualTo(payment.getDescription());

                });
    }
}