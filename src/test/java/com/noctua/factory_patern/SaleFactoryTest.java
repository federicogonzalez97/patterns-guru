package com.noctua.factory_patern;

import com.noctua.factory_patern.factory.SaleFactory;
import com.noctua.factory_patern.service.BrazilSale;
import com.noctua.factory_patern.service.ChileSale;
import com.noctua.factory_patern.service.MexicoSale;
import com.noctua.factory_patern.service.Sale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SaleFactoryTest {

    @Autowired
    private SaleFactory saleFactory;

    @Autowired
    private MockMvc mockMvc;

    // A. Tests del Factory
    @Test
    void testCreateSale_BR_ReturnsBrazilSaleInstance() {
        Sale sale = saleFactory.createSale("BR");
        
        assertNotNull(sale);
        assertInstanceOf(BrazilSale.class, sale);
    }

    @Test
    void testCreateSale_BR_CalculatesCorrectVAT() {
        Sale sale = saleFactory.createSale("BR");
        double baseAmount = 100.0;
        double expectedFinalAmount = 112.0; // 100 * 1.12
        
        double result = sale.calculatePriceWithVAT(baseAmount);
        
        assertEquals(expectedFinalAmount, result, 0.01);
    }

    @Test
    void testCreateSale_CL_ReturnsChileSaleInstance() {
        Sale sale = saleFactory.createSale("CL");
        
        assertNotNull(sale);
        assertInstanceOf(ChileSale.class, sale);
    }

    @Test
    void testCreateSale_CL_CalculatesCorrectVAT() {
        Sale sale = saleFactory.createSale("CL");
        double baseAmount = 100.0;
        double expectedFinalAmount = 119.0; // 100 * 1.19
        
        double result = sale.calculatePriceWithVAT(baseAmount);
        
        assertEquals(expectedFinalAmount, result, 0.01);
    }

    @Test
    void testCreateSale_MX_ReturnsMexicoSaleInstance() {
        Sale sale = saleFactory.createSale("MX");
        
        assertNotNull(sale);
        assertInstanceOf(MexicoSale.class, sale);
    }

    @Test
    void testCreateSale_MX_CalculatesCorrectVAT() {
        Sale sale = saleFactory.createSale("MX");
        double baseAmount = 100.0;
        double expectedFinalAmount = 116.0; // 100 * 1.16
        
        double result = sale.calculatePriceWithVAT(baseAmount);
        
        assertEquals(expectedFinalAmount, result, 0.01);
    }

    @Test
    void testCreateSale_UnsupportedCountry_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            saleFactory.createSale("US");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            saleFactory.createSale("AR");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            saleFactory.createSale("INVALID");
        });
    }

    @Test
    void testCreateSale_UnsupportedCountry_ErrorMessage() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> saleFactory.createSale("US")
        );
        
        assertTrue(exception.getMessage().contains("País no soportado"));
        assertTrue(exception.getMessage().contains("US"));
    }

    // B. Test del metodo default

    @Test
    void testDefaultMethod_ReturnsBaseAmount() {
        Sale sale = saleFactory.createSale("BR");
        double baseAmount = 100.0;
        
        double result = sale.defaultPrice(baseAmount);
        
        assertEquals(baseAmount, result, 0.01);
    }

    @Test
    void testDefaultMethod_WorksWithAllImplementations() {
        double baseAmount = 50.0;
        
        Sale brazilSale = saleFactory.createSale("BR");
        Sale chileSale = saleFactory.createSale("CL");
        Sale mexicoSale = saleFactory.createSale("MX");
        
        assertEquals(baseAmount, brazilSale.defaultPrice(baseAmount), 0.01);
        assertEquals(baseAmount, chileSale.defaultPrice(baseAmount), 0.01);
        assertEquals(baseAmount, mexicoSale.defaultPrice(baseAmount), 0.01);
    }

    // C. Tests del Controller

    @Test
    void testController_BR_ReturnsCorrectResponse() throws Exception {
        String requestJson = """
            {
                "country": "BR",
                "amount": 100.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("BR"))
                .andExpect(jsonPath("$.baseAmount").value(100.0))
                .andExpect(jsonPath("$.vatRate").value(0.12))
                .andExpect(jsonPath("$.finalAmount").value(112.0));
    }

    @Test
    void testController_CL_ReturnsCorrectResponse() throws Exception {
        String requestJson = """
            {
                "country": "CL",
                "amount": 100.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("CL"))
                .andExpect(jsonPath("$.baseAmount").value(100.0))
                .andExpect(jsonPath("$.vatRate").value(0.19))
                .andExpect(jsonPath("$.finalAmount").value(119.0));
    }

    @Test
    void testController_MX_ReturnsCorrectResponse() throws Exception {
        String requestJson = """
            {
                "country": "MX",
                "amount": 100.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("MX"))
                .andExpect(jsonPath("$.baseAmount").value(100.0))
                .andExpect(jsonPath("$.vatRate").value(0.16))
                .andExpect(jsonPath("$.finalAmount").value(116.0));
    }

    @Test
    void testController_DifferentAmount_ReturnsCorrectResponse() throws Exception {
        String requestJson = """
            {
                "country": "CL",
                "amount": 50.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("CL"))
                .andExpect(jsonPath("$.baseAmount").value(50.0))
                .andExpect(jsonPath("$.vatRate").value(0.19))
                .andExpect(jsonPath("$.finalAmount").value(59.5));
    }

    @Test
    void testController_UnsupportedCountry_Returns400() throws Exception {
        String requestJson = """
            {
                "country": "US",
                "amount": 100.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("País no soportado")));
    }

    @Test
    void testController_InvalidCountry_Returns400() throws Exception {
        String requestJson = """
            {
                "country": "AR",
                "amount": 100.0
            }
            """;

        mockMvc.perform(post("/api/sales/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("País no soportado")));
    }
}

