package Web.Controller;

import Persistence.Repository.ProductRepository;
import Web.Controllers.ProductController;
import Web.Models.ProductCreateDto;
import Web.Models.ProductDto;
import Persistence.Entity.ProductEntity;
import Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
// Each test method will have its own @Test annotation
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;  // Use real service

    @MockBean
    private ProductRepository productRepository;  // Mock repository

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity product;
    private ProductDto productDTO;
    private ProductCreateDto productCreateDTO;

    @BeforeEach
    void setUp() {
        product = new ProductEntity(1L, "Product1", "Description1", 10.0);
        productDTO = new ProductDto(1L, "Product1", "Description1", 10.0);
        productCreateDTO = new ProductCreateDto("Product1", "Description1", 10.0);
    }

    // Test for getting all products
    @Test
    void testGetAllProducts() throws Exception {
        List<ProductEntity> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()));
    }

    // Test for getting a product by ID
    @Test
    void testGetProductById() throws Exception {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    // Test for creating a new product
    @Test
    void testCreateProduct() throws Exception {
        when(productRepository.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isCreated())  // Expect 201 Created
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductEntity updatedProduct = new ProductEntity(1L, "Updated Product Name", "Updated Description", 15.0);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(updatedProduct);
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(updatedProduct);
        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.description").value(updatedProduct.getDescription()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isNoContent()); // Expect 204 No Content
    }
}
