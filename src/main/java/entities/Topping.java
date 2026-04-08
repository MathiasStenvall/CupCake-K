package entities;

public class Topping {

    private String variant;
    private double price;

    public Topping(String variant, double price) {
        this.variant = variant;
        this.price = price;
    }

    public String getVariant() {
        return variant;
    }

    public double getPrice() {
        return price;
    }
}
