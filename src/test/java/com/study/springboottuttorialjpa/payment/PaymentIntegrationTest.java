package com.study.springboottuttorialjpa.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springboottuttorialjpa.customer.Customer;
import com.study.springboottuttorialjpa.customer.CustomerRegistrationController;
import com.study.springboottuttorialjpa.customer.CustomerRegistrationRequest;
import com.study.springboottuttorialjpa.enums.Currency;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * @SpringBootTest
 * Ensures that when testing the entire application will start up
 * en die after the test has finished.
 *
 * We don't want the call the entire functionality of the CustomerRegistrationController.
 * We just want to test whether the url is effective.
 *
 * Note that currently the MockStripeService is being used.
 * See: application.properties: stripe.enabled=false
 *
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    /**
     * Using @Autowired should normally not be done in an integration test.
     * But in this case the rule is broken, because there is endpoint for given a list of payments.
     *
     * What should actually be done is to create an endpoint in PaymentController, in order to give a
     * list of payments for a specific user.
     */
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+31464332059");
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // Register the customer.
        ResultActions customerRegResultActions = mockMvc.perform(
                put("/api/v1/customer-registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest)))
        );
        log.info("customerRegResultActions = {}", customerRegResultActions);

        // Payment.
        long paymentId = 1L;
        Payment payment = new Payment(paymentId,
                customerId,
                new BigDecimal("100.00"),
                Currency.GBP,
                "0x0x0x",
                "Zakat");

        // Payment request.
        PaymentRequest paymentRequest = new PaymentRequest( payment);
        // When
        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest)))
        );

        // Then both customer registration and payment requests are 200 http status code.
        customerRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());
        // Payment is stored in db.
        // TODO: Do not use paymentRepository instead create an endpoint to retrieve payments for customers.
        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .hasValueSatisfying( payment1 -> assertThat(payment1).isEqualToComparingFieldByField(payment));

        // TODO: Ensure sms is delivered.
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json", e);
            return null;
        }
    }
}
