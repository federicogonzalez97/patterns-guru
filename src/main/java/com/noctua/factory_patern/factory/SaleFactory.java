package com.noctua.factory_patern.factory;

import com.noctua.factory_patern.service.BrazilSale;
import com.noctua.factory_patern.service.ChileSale;
import com.noctua.factory_patern.service.MexicoSale;
import com.noctua.factory_patern.service.Sale;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaleFactory {

    private final BrazilSale brazilSale;
    private final ChileSale chileSale;
    private final MexicoSale mexicoSale;

    public Sale createSale(String country) {
        return switch (country.toUpperCase()) {
            case "BR" -> brazilSale;
            case "CL" -> chileSale;
            case "MX" -> mexicoSale;
            default -> throw new IllegalArgumentException("País no soportado: " + country);
        };
    }
}
