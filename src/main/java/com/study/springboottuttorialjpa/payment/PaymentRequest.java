package com.study.springboottuttorialjpa.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * public @interface JsonProperty
 *
 * Marker annotation that can be used to define a non-static method as a "setter" or "getter" for a logical property
 * depending on its signature), or non-static object field to be used (serialized, deserialized) as a logical property.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentRequest {

    private final Payment payment;

    public PaymentRequest( @JsonProperty("payment") Payment payment) {
        this.payment = payment;
    }
}
