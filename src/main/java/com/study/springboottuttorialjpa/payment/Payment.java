package com.study.springboottuttorialjpa.payment;

import com.study.springboottuttorialjpa.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long paymentId;

    private UUID customerId;

    private BigDecimal amount = null;

    private Currency currency;

    private String source;

    private String description;

    public Payment(
            Long paymentId,
            UUID customerId,
            BigDecimal amount,
            Currency currency,
            String source,
            String description
    ) {
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.source = source;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return paymentId.equals(payment.paymentId) && customerId.equals(payment.customerId) && amount.equals(payment.amount) && currency == payment.currency && source.equals(payment.source) && description.equals(payment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, customerId, amount, currency, source, description);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", customerId=" + customerId +
                ", amount=" + amount +
                ", currency=" + currency +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
