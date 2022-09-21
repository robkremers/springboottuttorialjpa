package com.study.springboottuttorialjpa.customer;

import com.study.springboottuttorialjpa.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * An instance variable can be mocked / initiated via:
 * 1.a. @Mock CustomerRepository customerRepository;
 * 1.b @BeforeEach() void setUp() {.. MockitoAnnotations.openMocks(this); ..}
 * 2.a Or: CustomerRepository customerRepository = mock(CustomerRepository.class);
 * 2.b  @BeforeEach() void setUp() {.. MockitoAnnotations.openMocks(this); ..}
 * The preferred method is the use of annotations.
 * <p>
 * Mocking is used here because when testing the class ustomerRegistrationService
 * underlying functionality needs to be given fixed data.
 * When implementing unit tests we normally don't use external data (e.g. an external database)
 * but simulate the data via mocking.
 * <p>
 * See for captors:
 * - https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/Mockito.html#captors
 */
class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    // Class to be tested.
    private CustomerRegistrationService customerRegistrationService;

    @BeforeEach()
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRegistrationService = new CustomerRegistrationService(customerRepository, phoneNumberValidator);
    }

    @Test
    void testShouldSaveNewCustomer() throws IllegalAccessException {
        // Given
        final String phoneNumber = "1234567890";
        Customer customer = new Customer(UUID.randomUUID(), "Rob", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number present so the new customer will be saved.
        given(customerRepository
                .selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //... Valid phone number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        // When
        /**
         * Since the customer does not exist as yet he will be saved.
         */
        customerRegistrationService.registerNewCustomer(customerRegistrationRequest);
        // Then
        /**
         * We want to ensure that the mock receives the correct arguments.
         * In order to do this we will use an argument capture.
         * In the following we are capturing the request.getCustomer() from
         * customerRepository.save(request.getCustomer());
         * in class CustomerRegistrationService.java.
         */
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void testShouldSaveNewCustomerWhenIdIsNull() throws IllegalAccessException {
        // Given
        final String phoneNumber = "1234567890";
        Customer customer = new Customer(null, "Rob", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number present so the new customer will be saved.
        given(customerRepository
                .selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        //... Valid phone number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        // When
        customerRegistrationService.registerNewCustomer(customerRegistrationRequest);
        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptorValue.getName()).isEqualTo(customer.getName());
        assertThat(customerArgumentCaptorValue.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

    @Test
    void testShouldNotSaveCustomerWhenCustomerExists() throws IllegalAccessException {
        // Given
        final String phoneNumber = "1234567890";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Rob", phoneNumber);

        CustomerRegistrationRequest Request = new CustomerRegistrationRequest(customer);

        // A customer has been saved (mocked situation).
        given(customerRepository
                .selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        //... Valid phone number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        // When
        customerRegistrationService.registerNewCustomer(Request);
        // Then
        then(customerRepository).should(never()).save(any());
        // or...
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
        then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testShouldNotSaveCustomerWhenPhoneNumberTaken() throws IllegalAccessException {
        // Given
        final String phoneNumber = "1234567890";
        UUID id = UUID.randomUUID();
        Customer customerRob = new Customer(id, "Rob", phoneNumber);
        // Different customer; same phonenumber.
        Customer customerAlice = new Customer(id, "Alice", phoneNumber);

        CustomerRegistrationRequest requestAlice = new CustomerRegistrationRequest(customerAlice);

        // Saving customerRob is mocked enabling a return of customerRob.
        given(customerRepository
                .selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerRob));

        //... Valid phone number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(requestAlice))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining(String.format("phone number [%s] has been taken", phoneNumber));
        // finally
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void testShouldNotSaveNewCustomerWhenPhoneNumberIsInvalid() throws IllegalAccessException {
        // Given
        final String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Rob", phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // Invalid phone number
        given(phoneNumberValidator.test(phoneNumber)).willReturn(false);

        // When
        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(customerRegistrationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Phone Number %s is not valid", phoneNumber));

        // Then
        then(customerRepository).shouldHaveNoInteractions();

    }
}