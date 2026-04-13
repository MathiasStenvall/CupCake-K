package persistence;

import entities.Cupcake;
import entities.Order;
import entities.OrderList;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
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

    public void uploadOrder(User user, List<Cupcake> cupcakes, double price) {
        LocalDate localDate = LocalDate.now();
        String date = localDate.toString();

        String sql = "INSERT INTO orders (user_id, date, price, paid)" +
                "VALUES (?, ?, ?, true) RETURNING order_id";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user.getUserID());
            ps.setString(2, date);
            ps.setDouble(3, price);

            ResultSet rs = ps.executeQuery();
            int orderId = -1;

            if (!rs.next()) {
                System.out.println("no order_id returned");
            }

            orderId = rs.getInt("order_id");

            uploadOrderDetails(orderId, cupcakes);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void uploadOrderDetails(int orderId, List<Cupcake> cupcakes) {

        String sql = "INSERT INTO orders_cupcakes (order_id, cupcake_id, amount)" +
                "VALUES (?, ?, ?)";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            for (Cupcake c : cupcakes) {

                ps.setInt(1, orderId);
                ps.setInt(2, c.getId());
                ps.setInt(3, c.getAmount());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
