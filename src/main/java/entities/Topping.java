package entities;

public class Topping {

    private int id;
    private String variant;
    private double price;

    public Topping(int id, String variant, double price) {
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
        return "Topping{" +
                "id=" + id +
                ", variant='" + variant + '\'' +
                ", price=" + price +
                '}';
    }
}
