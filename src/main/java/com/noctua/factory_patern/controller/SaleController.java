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

@RestController
@RequestMapping("/api/sales")
@AllArgsConstructor
public class SaleController {

    private final SaleFactory factory;

    @PostMapping("/price")
    public ResponseEntity<SaleResponse> calculate(@Valid @RequestBody SaleRequest request) {
        Sale sale = factory.getSaleByCountry(request.country());
        double finalAmount = sale.calculatePriceWithVAT(request.amount());

        double vatRate = (finalAmount - request.amount()) / request.amount();

        return ResponseEntity.ok(
                new SaleResponse(
                        request.country(),
                        request.amount(),
                        vatRate,
                        finalAmount
                )
        );
    }
}
