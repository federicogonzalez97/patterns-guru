package com.noctua.factory_patern.service;

import org.springframework.stereotype.Component;

@Component
public class BrazilSale implements Sale {
    @Override
    public double calculatePriceWithVAT(double baseAmount) {
        return baseAmount * 1.12;
    }
}
