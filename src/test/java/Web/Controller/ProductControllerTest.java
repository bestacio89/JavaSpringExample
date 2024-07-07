package Web.Controller;

import Core.Entity.Product;
import Persistence.Repository.ProductRepository;
import Web.Models.ProductCreateDto;
import Persistence.Entity.ProductEntity;
import Service.ProductService;
import com.example.demo.ProductApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {ProductApplication.class})
@AutoConfigureMockMvc
//@Import({ProductService.class})
@AutoConfigureDataJpa
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private  ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Test data builder
    public static class ProductEntityBuilder extends Product {
        private Long id;
        private String name;
        private String description;
        private Double price;

        public ProductEntityBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ProductEntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProductEntityBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductEntityBuilder withPrice(Double price) {
            this.price = price;
            return this;
        }

        public ProductEntity build() {
            ProductEntity product = new ProductEntity();
            product.setId(id);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            return product;
        }
    }






    @Test
    void GetAllProductsReturnsList() throws Exception {
        // Mock data
        List<ProductEntity> products = Arrays.asList(
                new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build(),
                new ProductEntityBuilder().withId(2L).withName("Product2").withDescription("Description2").withPrice(20.0).build()
        );
        // Mocking service behavior instead of repository
        when(productService.findAll()).thenReturn(products);

        // Perform GET request and validate the response
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(products.get(0).getName()))
                .andExpect(jsonPath("$[0].description").value(products.get(0).getDescription()))
                .andExpect(jsonPath("$[0].price").value(products.get(0).getPrice()))
                .andExpect(jsonPath("$[1].id").value(products.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(products.get(1).getName()))
                .andExpect(jsonPath("$[1].description").value(products.get(1).getDescription()))
                .andExpect(jsonPath("$[1].price").value(products.get(1).getPrice()));
    }

    @Test
    public void GetAllProductsReturnsEmptyList() throws Exception {
        // Mock service behavior to return an empty list
        when(productService.findAll()).thenReturn(Collections.emptyList());

        // Perform GET request and validate the response (empty list expected)
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty()); // Assert that the response body is an empty array
    }
    @Test
    public void GetProductByIdReturnsObject() throws Exception {
        // Create mock product using builder
        ProductEntity product = new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build();

        // Mock service behavior
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));

        // Perform GET request and validate the response
        mockMvc.perform(get("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void GetProductByIdReturns404() throws Exception {
        // Create mock product using builder
        ProductEntity product = new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build();

        // Mock service behavior for both existing and non-existent cases
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));
        when(productService.findById(2L)).thenReturn(Optional.empty()); // Simulate non-existent product with ID 2

        // Test case 1: Existing product
        mockMvc.perform(get("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

        // Test case 2: Non-existent product
        mockMvc.perform(get("/api/products/{id}", 2L) // Use the ID of the non-existent product
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Expect 404 Not Found
    }
    @Test
    public void CreateProductReturns200() throws Exception {
        // Create mock product and DTO using builder
        ProductEntity product = new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build();
        ProductCreateDto productCreateDTO = new ProductCreateDto("Product1", "Description1", 10.0);

        // Mock repository and service behavior
        when(productRepository.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);

        // Perform POST request and validate the response
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }
    @Test
    public void CreateProductReturns400() throws Exception {
        // Create mock product and DTO with invalid data
        ProductCreateDto invalidProduct = new ProductCreateDto("", null, -1.0); // Empty name, null description, negative price

        // No need to mock repository as we expect validation error before saving
        // Mock service behavior (modify based on your specific logic)
        // Assuming service throws an exception or returns null for invalid data
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenThrow(new IllegalArgumentException("Invalid product data"));

        // Perform POST request and validate error response (expecting 400 Bad Request)
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());  // Expect 400 Bad Request
    }
    @Test
    public void UpdateProductReturns200() throws Exception {
        // Create mock product for initial state and updated state using builder
        ProductEntity existingProduct = new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build();
        ProductEntity updatedProduct = new ProductEntityBuilder().withId(1L).withName("Updated Product Name").withDescription("Updated Description").withPrice(15.0).build();

        // Mock service behavior
        when(productService.findById(existingProduct.getId())).thenReturn(Optional.of(existingProduct));
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(updatedProduct);

        // Perform PUT request and validate the response
        mockMvc.perform(put("/api/products/{id}", existingProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.description").value(updatedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));

        // Verify that the service methods were called
        verify(productService).findById(existingProduct.getId());
        verify(productService).save(ArgumentMatchers.any(ProductEntity.class));
    }
    @Test
    public void UpdateNonExistentProductReturns404() throws Exception {
        // Create mock product for updated state using builder
        ProductEntity updatedProduct = new ProductEntityBuilder().withId(1L).withName("Updated Product Name").withDescription("Updated Description").withPrice(15.0).build();

        // Mock service behavior for non-existent product
        when(productService.findById(updatedProduct.getId())).thenReturn(Optional.empty());

        // Perform PUT request and validate response (expecting 404)
        mockMvc.perform(put("/api/products/{id}", updatedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());  // Expect 404 Not Found

        // Verify only findById was called (no save call for non-existent product)
        verify(productService).findById(updatedProduct.getId());
        verify(productService, times(0)).save(ArgumentMatchers.any(ProductEntity.class));  // Verify save wasn't called
    }

    @Test
    public void DeleteProductRetunsNoContent() throws Exception {
        // Create mock product using builder (optional, can be removed)
        ProductEntity product = new ProductEntityBuilder().withId(1L).withName("Product1").withDescription("Description1").withPrice(10.0).build();

        // Mock repository behavior (verify findById is called)
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // Perform DELETE request and validate the response (no content expected)
        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isNoContent());
    }


}
