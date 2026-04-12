package entities;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private User basketUser;
    private List<Cupcake> basketCupcakes;
    private double basketTotalPrice;

    public Basket (User basketUser){
        this.basketUser = basketUser;
        basketCupcakes = new ArrayList<>();
    }

    public void addCupcakeToBasket(Cupcake cupcake){
        basketCupcakes.add(cupcake);
        basketTotalPrice += cupcake.getPrice();

    }

    public void removeCupcakeFromBasket(Cupcake cupcake){
        basketCupcakes.remove(cupcake);
        basketTotalPrice -= cupcake.getPrice();
    }

    public void reduceUserBalance(){
        if (basketUser.getBalance() >= basketTotalPrice){
            basketUser.setBalance(basketUser.getBalance() - basketTotalPrice);
        } else {
            System.out.println("Insufficient balance");
        }
    }

}
