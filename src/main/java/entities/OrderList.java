package entities;

import java.util.ArrayList;
import java.util.List;

public class OrderList {

    List<Order> orderList;

    public OrderList(){
        orderList = new ArrayList<>();
    }

    public void addOrder(Order order){
        orderList.add(order);
    }

    public List<Order> getOrderList(){
        return orderList;
    }

    public Order findOrderById(int id){
        for (Order o: orderList){
            if (o.getOrderId() == id){
                return o;
            }
        }
        return null;
    }

    public Order findOrdersByUserId(int userId){
        for (Order o: orderList){
            if (o.getUserId() == userId){
                return o;
            }
        }
        return null;
    }

}
