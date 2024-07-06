package Service;

import Persistence.Entity.ProductEntity;
import Persistence.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductEntity product1;
    private ProductEntity product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        product1 = new ProductEntity(1L, "Product1", "Description1", 10.0);
        product2 = new ProductEntity(2L, "Product2", "Description2", 20.0);
    }

    @Test
    void testFindAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductEntity> products = productService.findAll();

        assertEquals(2, products.size());
        assertEquals(product1, products.get(0));
        assertEquals(product2, products.get(1));
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<ProductEntity> foundProduct1 = productService.findById(1L);
        Optional<ProductEntity> foundProduct2 = productService.findById(2L);
        Optional<ProductEntity> notFoundProduct = productService.findById(3L);

        assertTrue(foundProduct1.isPresent());
        assertEquals(product1, foundProduct1.get());

        assertTrue(foundProduct2.isPresent());
        assertEquals(product2, foundProduct2.get());

        assertFalse(notFoundProduct.isPresent());
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(product1)).thenReturn(product1);

        ProductEntity savedProduct = productService.save(product1);

        assertEquals(product1, savedProduct);
    }

    @Test
    void testDeleteProductById() {
        productService.deleteById(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
