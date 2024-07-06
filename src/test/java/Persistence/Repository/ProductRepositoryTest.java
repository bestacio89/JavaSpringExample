package Persistence.Repository;

import Persistence.Entity.ProductEntity;
import com.example.demo.ProductApplication;
import com.example.demo.Config.TestJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ProductApplication.class, TestJpaConfig.class})
@ActiveProfiles("test")

public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAll() {
        ProductEntity product1 = new ProductEntity(null, "Product1", "Description1", 10.0);
        ProductEntity product2 = new ProductEntity(null, "Product2", "Description2", 20.0);

        productRepository.save(product1);
        productRepository.save(product2);

        List<ProductEntity> products = productRepository.findAll();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product1")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product2")));
    }

    @Test
    void testFindById() {
        ProductEntity product = new ProductEntity(null, "Product1", "Description1", 10.0);
        product = productRepository.save(product);
        Long productId = product.getId();

        Optional<ProductEntity> foundProduct = productRepository.findById(productId);

        assertTrue(foundProduct.isPresent());
        assertEquals(product.getName(), foundProduct.get().getName());
        assertEquals(product.getDescription(), foundProduct.get().getDescription());
        assertEquals(product.getPrice(), foundProduct.get().getPrice());
    }

    @Test
    void testSave() {
        ProductEntity product = new ProductEntity(null, "Product1", "Description1", 10.0);
        ProductEntity savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(product.getPrice(), savedProduct.getPrice());
    }

    @Test
    void testDelete() {
        ProductEntity product = new ProductEntity(null, "Product1", "Description1", 10.0);
        product = productRepository.save(product);
        Long productId = product.getId();

        productRepository.deleteById(productId);

        Optional<ProductEntity> deletedProduct = productRepository.findById(productId);
        assertFalse(deletedProduct.isPresent());
    }
}