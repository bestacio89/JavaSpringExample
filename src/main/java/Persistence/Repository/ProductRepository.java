package Persistence.Repository;



import Persistence.Entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GenericRepository<ProductEntity, Long> {
    // Add any custom queries if needed
}