package com.sukanta.springbootecom.model.enums;

import lombok.Getter;

@Getter
public enum Currency {
    INR("₹"),
    DOLLARS("$"),
    GBP("£");

    private final String abbreviation;

    Currency(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}
