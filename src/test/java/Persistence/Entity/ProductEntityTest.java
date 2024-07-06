package Persistence.Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductEntityTest {

    @Test
    public void testProductEntityConstruction() {
        ProductEntity product = new ProductEntity(1L, "Product1", "Description1", 10.0);

        assertEquals(1L, product.getId());
        assertEquals("Product1", product.getName());
        assertEquals("Description1", product.getDescription());
        assertEquals(10.0, product.getPrice());
    }

    @Test
    public void testEqualsAndHashCode() {
        ProductEntity product1 = new ProductEntity(1L, "Product1", "Description1", 10.0);
        ProductEntity product2 = new ProductEntity(1L, "Product1", "Description1", 10.0);
        ProductEntity product3 = new ProductEntity(2L, "Product2", "Description2", 20.0);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);

        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }
}