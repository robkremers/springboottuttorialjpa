package com.study.springboottuttorialjpa.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CardPaymentCharge {

    private final boolean isCardDebited;

    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }

    public boolean isCardDebited() {
        return isCardDebited;
    }
}
