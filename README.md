# Factory Pattern - Sistema de Ventas por País

Este proyecto implementa el **Patrón Factory** en Java Spring Boot para crear tipos de venta por país, calculando el precio con IVA según la región.

## Características

- **Interfaz Sale**: Define el contrato para calcular precios con IVA, incluyendo un método default
- **Implementaciones por país**:
  - `BrazilSale`: IVA del 12%
  - `ChileSale`: IVA del 19%
  - `MexicoSale`: IVA del 16%
- **SaleFactory**: Factory registrado como bean Spring que crea instancias según el código de país
- **REST API**: Endpoint para calcular precios con IVA
- **Tests completos**: Suite de pruebas unitarias e integración

## Requisitos

- Java 21
- Maven 3.6+

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/noctua/factory_patern/
│   │   ├── controller/
│   │   │   └── SaleController.java      # REST Controller
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── factory/
│   │   │   └── SaleFactory.java         # Factory Pattern
│   │   ├── models/
│   │   │   ├── SaleRequest.java
│   │   │   └── SaleResponse.java
│   │   └── service/
│   │       ├── Sale.java                # Interfaz con método default
│   │       ├── BrazilSale.java
│   │       ├── ChileSale.java
│   │       └── MexicoSale.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/noctua/factory_patern/
        └── SaleFactoryTest.java          # Tests completos
```

## Ejecutar la Aplicación

### Opción 1: Usando Maven Wrapper (Recomendado)

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Opción 2: Usando Maven

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## Ejecutar los Tests

### Ejecutar todos los tests

```bash
# Windows
.\mvnw.cmd test

# Linux/Mac
./mvnw test
```

### Ejecutar un test específico

```bash
# Windows
.\mvnw.cmd test -Dtest=SaleFactoryTest

# Linux/Mac
./mvnw test -Dtest=SaleFactoryTest
```

## API REST

### Endpoint: Calcular Precio con IVA

**POST** `/api/sales/price`

**Request Body:**
```json
{
  "country": "CL",
  "amount": 100.0
}
```

**Response (200 OK):**
```json
{
  "country": "CL",
  "baseAmount": 100.0,
  "vatRate": 0.19,
  "finalAmount": 119.0
}
```

**Response (400 Bad Request) - País no soportado:**
```
País no soportado: US
```

### Ejemplos de uso

#### Brasil (IVA 12%)
```bash
curl -X POST http://localhost:8080/api/sales/price \
  -H "Content-Type: application/json" \
  -d '{"country": "BR", "amount": 100.0}'
```

#### Chile (IVA 19%)
```bash
curl -X POST http://localhost:8080/api/sales/price \
  -H "Content-Type: application/json" \
  -d '{"country": "CL", "amount": 100.0}'
```

#### México (IVA 16%)
```bash
curl -X POST http://localhost:8080/api/sales/price \
  -H "Content-Type: application/json" \
  -d '{"country": "MX", "amount": 100.0}'
```

## Tests Incluidos

El archivo `SaleFactoryTest.java` incluye:

### A. Tests del Factory
- ✅ Verificar que `createSale("BR")` devuelva instancia de `BrazilSale` y calcule correctamente el IVA
- ✅ Verificar que `createSale("CL")` devuelva instancia de `ChileSale` y calcule correctamente el IVA
- ✅ Verificar que `createSale("MX")` devuelva instancia de `MexicoSale` y calcule correctamente el IVA
- ✅ Verificar que un código no soportado lance `IllegalArgumentException`

### B. Test del método default
- ✅ Probar el método default de la interfaz `Sale` en las implementaciones
- ✅ Asegurar que devuelve el comportamiento esperado por defecto

### C. Tests del Controller
- ✅ Validar respuesta correcta para cada país (BR, CL, MX) y monto
- ✅ Validar status 400 y mensaje de error para país no soportado

## Tecnologías Utilizadas

- **Spring Boot 3.5.7**
- **Java 21**
- **Maven**
- **JUnit 5**
- **MockMvc** (para tests de integración)
- **Lombok**

## Patrón Factory Implementado

El `SaleFactory` utiliza el patrón Factory para encapsular la lógica de creación de objetos `Sale` según el código de país:

```java
@Component
public class SaleFactory {
    public Sale createSale(String country) {
        return switch (country.toUpperCase()) {
            case "BR" -> brazilSale;
            case "CL" -> chileSale;
            case "MX" -> mexicoSale;
            default -> throw new IllegalArgumentException("País no soportado: " + country);
        };
    }
}
```

## Método Default en la Interfaz

La interfaz `Sale` incluye un método default que puede ser utilizado por todas las implementaciones:

```java
public interface Sale {
    double calculatePriceWithVAT(double baseAmount);
    
    default double defaultPrice(double baseAmount) {
        return baseAmount;
    }
}
```

## Autor

Implementado como parte de un ejercicio de diseño de patrones.

