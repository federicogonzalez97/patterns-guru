package com.noctua.factory_patern.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleRequest(
        @NotBlank(message = "El país no puede estar vacío")
        String country,
        @NotNull(message = "El monto no puede ser nulo")
        @Positive(message = "El monto debe ser mayor a cero")
        Double amount
) {
}
