package entities;

public class Cupcake {

    private int id;
    private int baseId;
    private String baseName;
    private int toppingId;
    private String toppingName;
    private double price;

    private int amount;

    public Cupcake(int id, int baseId, String baseName,
                   int toppingId, String toppingName, double price) {
        this.id = id;
        this.baseId = baseId;
        this.baseName = baseName;
        this.toppingId = toppingId;
        this.toppingName = toppingName;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String toString (){
        return baseName + " " + toppingName;
    }

}
