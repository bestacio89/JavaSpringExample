package Core.Entity;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;

    // Getters and Setters (or use Lombok annotations)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Additional methods (optional):
    //  - Override toString() for meaningful representation
    //  - Add validation logic for properties (e.g., price cannot be negative)
}

