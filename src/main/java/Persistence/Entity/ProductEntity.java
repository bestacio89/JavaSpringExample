package Persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA annotation
 @Entity
 @Data
 @NoArgsConstructor
 @AllArgsConstructor
    public class ProductEntity {

        @Id  // JPA annotation
        @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA annotation
        private Long id;

        @Column(name = "name")  // Optional for clarity (column name matches property name)
        private String name;

        @Column(length = 255)  // Optional to specify column length
        private String description;

        @Column(name = "price")  // Optional for clarity (column name matches property name)
        private Double price;

        // Getters and Setters (or use Lombok annotations)
        // ...

        // You can also define methods specific to the JPA entity here
        // ...
    }