package persistence;

import entities.Cupcake;
import entities.Order;
import entities.OrderList;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderMapper {

    private final ConnectionPool cp;

    public OrderMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public void getAllOrders(OrderList orderList) {
        String sql = "SELECT order_id, user_id, date, price, paid FROM orders";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                String date = rs.getString("date");
                double price = rs.getDouble("price");
                boolean paid = rs.getBoolean("paid");

                Order order = new Order(orderId, userId, date, price, paid);
                orderList.addOrder(order);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void uploadOrder(User user, List<Cupcake> cupcakes){

    }


}
