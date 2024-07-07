package Web.Controllers;

import Persistence.Entity.ProductEntity;
import Web.Models.ProductDto;
import Service.ProductService;
import Web.Models.ProductCreateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> products = productService.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.out.println("Error retrieving products: " + e.getMessage()); // Use System.out for basic logging
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            Optional<ProductEntity> productOptional = productService.findById(id);
            return productOptional.map(this::convertToDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.out.println("Error retrieving product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        try {
            ProductEntity product = convertToEntity(productCreateDto);
            ProductEntity savedProduct = productService.save(product);
            return ResponseEntity.ok(convertToDto(savedProduct));
        } catch (IllegalArgumentException e) { // Catch specific exception for invalid data
            System.out.println("Error creating product: " + e.getMessage());
            return ResponseEntity.badRequest().build(); // Return 400 with a message
        } catch (Exception e) { // Catch other exceptions for unexpected errors
            System.out.println("Unexpected error creating product: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto productCreateDto) {
        try {
            Optional<ProductEntity> productOptional = productService.findById(id);
            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();
                product.setName(productCreateDto.getName());
                product.setDescription(productCreateDto.getDescription());
                product.setPrice(productCreateDto.getPrice());
                ProductEntity updatedProduct = productService.save(product);
                return ResponseEntity.ok(convertToDto(updatedProduct));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Error updating product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            // Handle specific exception for non-existent product
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println("Error deleting product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private ProductDto convertToDto(ProductEntity product) {
        if (product == null) {
            return null; // Handle null entity gracefully (e.g., return a default DTO)
        }
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    private ProductEntity convertToEntity(ProductCreateDto productCreateDto) {
        ProductEntity product = new ProductEntity();
        product.setName(productCreateDto.getName()); // Use getName() instead of Gdescription()
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        return product;
    }

}
