package Web.Controller;


import Web.Controllers.ProductController;
import Web.Models.ProductCreateDto;
import Web.Models.ProductDto;
import Persistence.Entity.ProductEntity;
import Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

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

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.findById(product.getId())).thenReturn(Optional.of(product));
        when(productService.save(ArgumentMatchers.any(ProductEntity.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteById(product.getId());

        mockMvc.perform(delete("/api/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Configuration
    public static class TestConfig {
        // Add your test-specific bean definitions here
    }


}

