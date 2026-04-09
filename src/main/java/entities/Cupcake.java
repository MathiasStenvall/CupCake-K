package entities;

public class Cupcake {

    private int id;
    private int base_id;
    private String baseName;
    private int topping_id;
    private String toppingName;
    private double price;

    public Cupcake(int id, int base_id, String baseName,
                   int topping_id, String toppingName, double price) {
        this.id = id;
        this.base_id = base_id;
        this.baseName = baseName;
        this.topping_id = topping_id;
        this.toppingName = toppingName;
        this.price = price;
    }

    public String toString (){
        return baseName + " " + toppingName;
    }

}
