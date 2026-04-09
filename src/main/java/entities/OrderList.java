package entities;

import persistence.OrderMapper;

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

}
