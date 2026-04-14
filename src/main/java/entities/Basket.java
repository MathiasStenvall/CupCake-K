package entities;

import persistence.ConnectionPool;
import persistence.OrderMapper;
import persistence.UserMapper;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private OrderMapper orderMapper;
    private UserMapper userMapper;
    private User basketUser;
    private List<Cupcake> basketCupcakes;
    private double basketTotalPrice;

    public Basket (User basketUser, ConnectionPool cp){
        this.basketUser = basketUser;
        basketCupcakes = new ArrayList<>();
        orderMapper = new OrderMapper(cp);
        userMapper = new UserMapper(cp);
    }

    public void addCupcakeToBasket(Cupcake cupcake){
        basketCupcakes.add(cupcake);
        basketTotalPrice += cupcake.getPrice() * cupcake.getAmount();

    }

    public void removeCupcakeFromBasket(Cupcake cupcake){
        basketCupcakes.remove(cupcake);
        basketTotalPrice -= cupcake.getPrice() * cupcake.getAmount();
    }

    public void payBasket(){
        if (basketUser.getBalance() >= basketTotalPrice){
            basketUser.setBalance(basketUser.getBalance() - basketTotalPrice);

            orderMapper.uploadOrder(basketUser, basketCupcakes, basketTotalPrice);
            userMapper.updateUserBalance(basketUser.getUserID(), basketUser.getBalance());

            basketTotalPrice = 0;
            basketCupcakes.clear();
        } else {
            System.out.println("Insufficient balance");
        }
    }



}
