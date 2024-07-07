package Web.Models;

public class ProductCreateDto {

    private String name;
    private String description;
    private Double price;

    // Optional constructor to set initial values (if needed)
    public ProductCreateDto(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters (optional)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }
}
