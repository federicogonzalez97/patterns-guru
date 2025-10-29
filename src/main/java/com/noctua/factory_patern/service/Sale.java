package com.noctua.factory_patern.service;

public interface Sale {
    double calculatePriceWithVAT(double baseAmount);

    default double defaultPrice(double baseAmount) {
        return baseAmount;
    }
}
