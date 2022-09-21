package com.study.springboottuttorialjpa.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
// For JPA we need to specify a blank constructor.
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity(name = "customer")
@Table(name = "customer")
/**
 * https://javadoc.io/static/com.fasterxml.jackson.core/jackson-annotations/2.13.0/index.html
 * public @interface JsonIgnoreProperties
 * Annotation that can be used to either suppress serialization of properties (during serialization),
 * or ignore processing of JSON properties read (during deserialization).
 */
//@JsonIgnoreProperties(
//        value = "{id}"          // Ignore everything coming from the client.
//        , allowGetters = true   // When sending the payload to the client the id is included.
//)
public class Customer {

    @Id
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(
            name = "phone_number"
            , nullable = false
            , unique = true
    )
    private String phoneNumber;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Customer customer = (Customer) o;
//        return id.equals(customer.id) && name.equals(customer.name) && phoneNumber.equals(customer.phoneNumber);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, phoneNumber);
//    }

//    @Override
//    public String toString() {
//        return "Customer{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                '}';
//    }
}
