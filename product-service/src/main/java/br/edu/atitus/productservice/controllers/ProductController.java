package br.edu.atitus.productservice.controllers;

import br.edu.atitus.productservice.clients.CurrencyClient;
import br.edu.atitus.productservice.clients.CurrencyResponse;
import br.edu.atitus.productservice.dtos.ProductDTO;
import br.edu.atitus.productservice.entities.ProductEntity;
import br.edu.atitus.productservice.repositories.ProductRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductRepository repository;
    private final Environment environment;
    private final CurrencyClient currencyClient;

    public ProductController(ProductRepository repository, Environment environment, CurrencyClient currencyClient) {
        this.repository = repository;
        this.environment = environment;
        this.currencyClient = currencyClient;
    }

    @GetMapping("/{idproduct}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable Long idproduct,
            @RequestParam String targetCurrency) {

        ProductEntity entity = repository.findById(idproduct)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        String port = environment.getProperty("local.server.port");

        Double convertedPrice = null;
        if (targetCurrency.equals(entity.getCurrency())){
            convertedPrice = entity.getPrice();
        } else {
            CurrencyResponse currency = currencyClient.getCurrency(entity.getCurrency(), targetCurrency);
            convertedPrice = entity.getPrice() * currency.conversionRate();
        }


        ProductDTO dto = new ProductDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getBrand(),
                entity.getModel(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getStock(),
                "Product-service running on Port: " + port,
                convertedPrice,
                targetCurrency
        );

        return ResponseEntity.ok(dto);
    }
}