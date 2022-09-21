package com.study.springboottuttorialjpa.customer;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/customer-registration")
@Slf4j
public class CustomerRegistrationController {

    private final CustomerRegistrationService customerRegistrationService;

    @Autowired
    public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
    }

    @PutMapping
    public void registerNewCustomer(
            @Valid @RequestBody CustomerRegistrationRequest request) throws IllegalAccessException {
        log.info("request = {}", request.toString());
        customerRegistrationService.registerNewCustomer(request);
    }
}
