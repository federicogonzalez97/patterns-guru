package com.noctua.factory_patern.controller;

import com.noctua.factory_patern.factory.SaleFactory;
import com.noctua.factory_patern.models.SaleRequest;
import com.noctua.factory_patern.models.SaleResponse;
import com.noctua.factory_patern.service.Sale;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SaleController {

    private final SaleFactory factory;

    @PostMapping("/price")
    public ResponseEntity<SaleResponse> calculate(@Valid @RequestBody SaleRequest request) {
        Sale sale = factory.createSale(request.country());
        double finalAmount = sale.calculatePriceWithVAT(request.amount());

        // Redondear a 2 decimales para evitar problemas de precisión de punto flotante
        double vatRate = BigDecimal.valueOf((finalAmount - request.amount()) / request.amount())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        
        double finalAmountRounded = BigDecimal.valueOf(finalAmount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        return ResponseEntity.ok(
                new SaleResponse(
                        request.country(),
                        request.amount(),
                        vatRate,
                        finalAmountRounded
                )
        );
    }
}
