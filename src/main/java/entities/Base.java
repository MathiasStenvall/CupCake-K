package entities;

public class Base {

    private String variant;
    private double price;

    public Base(String variant, double price) {
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
