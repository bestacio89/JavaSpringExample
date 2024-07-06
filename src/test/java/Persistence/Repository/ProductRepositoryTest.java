package Persistence.Repository;

import Persistence.Entity.ProductEntity;
import Persistence.Repository.*;

import com.example.demo.TestJpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestJpaConfig.class)
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
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }

    @Test
    void testFindById() {
        ProductEntity product = new ProductEntity(null, "Product1", "Description1", 10.0);
        product = productRepository.save(product);
        Long productId = product.getId();

        Optional<ProductEntity> foundProduct = productRepository.findById(productId);

        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
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
    void testDeleteById() {
        ProductEntity product = new ProductEntity(null, "Product1", "Description1", 10.0);
        product = productRepository.save(product);
        Long productId = product.getId();

        productRepository.deleteById(productId);

        Optional<ProductEntity> deletedProduct = productRepository.findById(productId);

        assertFalse(deletedProduct.isPresent());
    }

}
