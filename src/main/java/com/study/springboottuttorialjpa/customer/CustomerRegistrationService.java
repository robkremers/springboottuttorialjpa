package com.study.springboottuttorialjpa.customer;

import com.study.springboottuttorialjpa.utils.PhoneNumberValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository, PhoneNumberValidator phoneNumberValidator) {
        this.customerRepository = customerRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) throws IllegalAccessException {

        String phoneNumber = request.getCustomer().getPhoneNumber();

        log.info("Correctness phonenumber = {}", phoneNumberValidator.test(phoneNumber));

        if (!phoneNumberValidator.test(phoneNumber)) {
            throw new IllegalStateException(String.format("Phone Number %s is not valid", phoneNumber));
        }

        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (customer.getName().equals(request.getCustomer().getName())) {
                return;
            }
            throw new IllegalAccessException(String.format("phone number [%s] has been taken", customer.getPhoneNumber()));
        }
        // RJWK: This is not the correct way of dealing with an ID. The ID might be duplicated.
        if (request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }
        customerRepository.save(request.getCustomer());
    }

}
