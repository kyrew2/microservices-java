package br.edu.atitus.productservice.controllers;

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

    public ProductController(ProductRepository repository, Environment environment) {
        this.repository = repository;
        this.environment = environment;
    }

    @GetMapping("/{idproduct}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable Long idproduct,
            @RequestParam String targetCurrency) {

        ProductEntity entity = repository.findById(idproduct)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        String port = environment.getProperty("local.server.port");

        ProductDTO dto = new ProductDTO(
                entity.getId(),
                entity.getDescription(),
                entity.getBrand(),
                entity.getModel(),
                entity.getPrice(),
                entity.getCurrency(),
                entity.getStock(),
                "Product-service running on Port: " + port,
                null,
                targetCurrency
        );

        return ResponseEntity.ok(dto);
    }
}