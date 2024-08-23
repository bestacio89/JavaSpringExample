package Persistence.Repository;



import Core.Entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GenericRepository<ProductEntity, Long> {
    // Add any custom queries if needed
    Page<ProductEntity> findAll(Pageable pageable);
}