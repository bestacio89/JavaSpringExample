package Core.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Product name is mandatory")  // Validates that the name is not null or empty
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")  // Limits the size of the product name
    private String name;

    @Column(length = 255)
    @Size(max = 255, message = "Description cannot exceed 255 characters")  // Limits the size of the description
    private String description;

    @Column(name = "price")
    @NotNull(message = "Price is mandatory")  // Validates that the price is not null
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")  // Ensures that the price is greater than zero
    private Double price;

    // You can also define methods specific to the JPA entity here
}
