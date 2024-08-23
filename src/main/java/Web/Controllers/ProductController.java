package Web.Controllers;

import Core.Entity.ProductEntity;
import Web.Models.Dto.ProductDto;
import Service.ProductService;
import Web.Models.Dto.ProductCreateDto;
import Web.Models.Generic.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Exceptions.ResourceNotFoundException;

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
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        try {
            // Define the sort direction and pageable object
            Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(page, size, sort);

            // Pass the pageable to the service layer to fetch a page of products
            Page<ProductEntity> productPage = productService.findAll(pageable);

            // Convert the product entities to DTOs
            List<ProductDto> products = productPage.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            // Wrap the result in your custom ApiResponse
            ApiResponse<List<ProductDto>> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Products retrieved successfully",
                    products
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle exceptions and log the error message
            System.out.println("Error retrieving products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error retrieving products", null)
            );
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        try {
            Optional<ProductEntity> productOptional = productService.findById(id);
            if (productOptional.isPresent()) {
                ProductDto productDto = convertToDto(productOptional.get());
                ApiResponse<ProductDto> response = new ApiResponse<>(HttpStatus.OK.value(), "Product found", productDto);
                return ResponseEntity.ok(response);
            } else {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e; // This will be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            System.out.println("Error retrieving product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductCreateDto productCreateDto) {
        try {
            ProductEntity product = convertToEntity(productCreateDto);
            ProductEntity savedProduct = productService.save(product);
            ProductDto productDto = convertToDto(savedProduct);

            ApiResponse<ProductDto> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Product created successfully", productDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ApiResponse<ProductDto> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid data", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Unexpected error creating product: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDto productCreateDto) {
        try {
            Optional<ProductEntity> productOptional = productService.findById(id);
            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();
                product.setName(productCreateDto.getName());
                product.setDescription(productCreateDto.getDescription());
                product.setPrice(productCreateDto.getPrice());
                ProductEntity updatedProduct = productService.save(product);

                ApiResponse<ProductDto> response = new ApiResponse<>(HttpStatus.OK.value(), "Product updated successfully", convertToDto(updatedProduct));
                return ResponseEntity.ok(response);
            } else {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e; // This will be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            System.out.println("Error updating product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            Optional<ProductEntity> productOptional = productService.findById(id);
            if (productOptional.isPresent()) {
                productService.deleteById(id);
                ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Product deleted successfully", null);
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                throw new ResourceNotFoundException("Product not found with id " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e; // This will be caught by the GlobalExceptionHandler
        } catch (Exception e) {
            System.out.println("Error deleting product with id " + id + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Helper methods to convert between entities and DTOs
    private ProductDto convertToDto(ProductEntity product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    private ProductEntity convertToEntity(ProductCreateDto productCreateDto) {
        ProductEntity product = new ProductEntity();
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        return product;
    }
}
