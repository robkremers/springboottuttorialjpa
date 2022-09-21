package com.study.springboottuttorialjpa.customer;

import com.study.springboottuttorialjpa.customer.Customer;
import com.study.springboottuttorialjpa.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @DataJpaTest In order to be able to test JPA queries against a H2 database.
 *  - "spring.jpa.properties.javax.persistence.validation.mode=none"
 *      - Will trigger the use of the annotations on the columns as defined in Entity class Customer.
 *          - (in this case they already work without the property).
 * @AutoConfigureTestDatabase Annotation that can be applied to a test class to configure a test database
 * to use instead of the application-defined or auto-configured DataSource.
 * In the case of multiple DataSource beans, only the @Primary DataSource is considered.
 */
@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
//@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSelectCustomerByPhoneNumber() {
        // Given
        UUID uuid = UUID.randomUUID();
        String phoneNumber = "123456789";
        Customer customer = new Customer(uuid, "Rob", phoneNumber);
        // When
        customerRepository.save(customer);
        // Then
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying( c -> {
                    assertThat(c).isEqualTo(customer);
                });
    }

    @Test
    void testNotSelectCustomerByPhoneNumberWhenNumberDoesNotExist() {
        // Given
        String phoneNumber = "0000";
        // When
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        // Then
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void testSaveCustomer() {
        // Given
        Customer customer = new Customer(UUID.randomUUID(), "Rob", "1234567890");
        // When
        customerRepository.save(customer);
        // Then
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(customer1 -> {
                            assertThat(customer1.getName()).isEqualTo(customer.getName());
                            assertThat(customer1.getId()).isEqualTo(customer.getId());
                            assertThat(customer1.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
                    assertThat(customer1).isEqualTo(customer);
                        }
                );
    }

    @Test
    void testShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, null, "0000");
        // When
        // Then
        assertThat(customerRepository.findById(uuid)).isNotPresent();
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.study.springboottuttorialjpa.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // Given
        UUID uuid = UUID.randomUUID();
        Customer customer = new Customer(uuid, "Rob", null);
        // When
        // Then
        assertThat(customerRepository.findById(uuid)).isNotPresent();
        assertThatThrownBy(() -> customerRepository.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.study.springboottuttorialjpa.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}