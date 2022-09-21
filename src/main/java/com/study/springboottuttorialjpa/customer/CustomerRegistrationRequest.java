package com.study.springboottuttorialjpa.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Resource:
 * https://javadoc.io/static/com.fasterxml.jackson.core/jackson-annotations/2.13.0/index.html
 * public @interface JsonProperty:
 * Marker annotation that can be used to define a non-static method as a "setter" or "getter" for a logical property
 * (depending on its signature), or non-static object field to be used (serialized, deserialized) as a logical property.
 *
 */
public class CustomerRegistrationRequest {

    private final Customer customer;

    public CustomerRegistrationRequest (
            @JsonProperty("customer") Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerRegistrationRequest{" +
                "customer=" + customer +
                '}';
    }
}
