package Persistence.Repository;



import Core.Entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GenericRepository<ProductEntity, Long> {
    // Add any custom queries if needed
}