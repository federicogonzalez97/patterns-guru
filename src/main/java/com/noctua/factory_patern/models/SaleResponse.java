package com.noctua.factory_patern.models;

public record SaleResponse(
        String country,
        double baseAmount,
        double vatRate,
        double finalAmount
) {}