package entities;

public class Base {

    private int id;
    private String variant;
    private double price;

    public Base(int id, String variant, double price) {
        this.id = id;
        this.variant = variant;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getVariant() {
        return variant;
    }

    public double getPrice() {
        return price;
    }

    public String toString() {
        return "Base{" +
                "id=" + id +
                ", variant='" + variant + '\'' +
                ", price=" + price +
                '}';
    }
}
