package entities;

public class Order {

    private int orderId;
    private int userId;
    private String date;
    private double price;
    private boolean paid;

    public Order(int orderId, int userId, String date, double price, boolean paid) {
        this.orderId = orderId;
        this.userId = userId;
        this.date = date;
        this.price = price;
        this.paid = paid;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPaid() {
        return paid;
    }

    public String toString() {
        return "{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", date='" + date + '\'' +
                ", price=" + price +
                ", paid=" + paid +
                '}';
    }
}
