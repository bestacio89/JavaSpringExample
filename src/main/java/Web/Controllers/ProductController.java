package Web.Controllers;

import Persistence.Entity.ProductEntity;
import Web.Models.ProductDto;
import Service.ProductService;
import Web.Models.ProductCreateDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        List<ProductDto> products = productService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductEntity> product = productService.findById(id);
        return product.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateDto productCreateDTO) {
        ProductEntity product = new ProductEntity();
        product.setName(productCreateDTO.name());
        product.setDescription(productCreateDTO.description());
        product.setPrice(productCreateDTO.price());
        ProductEntity savedProduct = productService.save(product);
        return ResponseEntity.ok(convertToDTO(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto productCreateDTO) {
        Optional<ProductEntity> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            ProductEntity product = productOptional.get();
            product.setName(productCreateDTO.name());
            product.setDescription(productCreateDTO.description());
            product.setPrice(productCreateDTO.price());
            ProductEntity updatedProduct = productService.save(product);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ProductDto convertToDTO(ProductEntity product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}