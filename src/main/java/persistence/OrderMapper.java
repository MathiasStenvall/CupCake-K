package persistence;

import entities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private final ConnectionPool cp;

    public OrderMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
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
                orderList.add(order);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderList;
    }

    public void uploadOrder(User user, List<Cupcake> cupcakes, double price) {
        LocalDate localDate = LocalDate.now();
        String date = localDate.toString();

        String sql = "INSERT INTO orders (user_id, date, price, paid)" +
                "VALUES (?, ?, ?, true) RETURNING order_id";

        Connection connection = null;

        try {
            connection = cp.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(sql)) {


                ps.setInt(1, user.getUserID());
                ps.setString(2, date);
                ps.setDouble(3, price);

                ResultSet rs = ps.executeQuery();
                int orderId;

                if (rs.next()) {
                    orderId = rs.getInt("order_id");
                } else {
                    throw new SQLException("no order_id returned");
                }

                uploadOrderDetails(connection, orderId, cupcakes);
                connection.commit();
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rb) {
                    rb.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadOrderDetails(Connection connection, int orderId, List<Cupcake> cupcakes) throws SQLException {

        String sql = "INSERT INTO orders_cupcakes (order_id, cupcake_id, amount)" +
                "VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (Cupcake c : cupcakes) {

                ps.setInt(1, orderId);
                ps.setInt(2, c.getId());
                ps.setInt(3, c.getAmount());

                ps.addBatch();
            }

            ps.executeBatch();

        }

    }

    public boolean removeOrder(int id) {

        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public List<Order> getOrderByInputs(String date, Integer orderId, Integer userId) {

        List<Order> orderList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");

        try (Connection connection = cp.getConnection()) {

            List<Object> parameters = new ArrayList<>();

            if (date != null && !date.isEmpty()) {
                sql.append(" AND date = ?");
                parameters.add(date);
            }

            if (orderId != null) {
                sql.append(" AND order_id = ?");
                parameters.add(orderId);
            }

            if (userId != null) {
                sql.append(" AND user_id = ?");
                parameters.add(userId);
            }

            try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {

                for (int i = 0; i < parameters.size(); i++) {
                    ps.setObject(i + 1, parameters.get(i));
                }

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Order order = new Order(
                                rs.getInt("order_id"),
                                rs.getInt("user_id"),
                                rs.getString("date"),
                                rs.getDouble("price"),
                                rs.getBoolean("paid")
                        );

                        orderList.add(order);

                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return orderList;
    }

    public List<Order> clientOrders(int userId) {

        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT order_id, user_id, date, price, paid FROM orders WHERE user_id = ? ORDER BY date DESC";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    int orderId = rs.getInt("order_id");
                    int userWithId = rs.getInt("user_id");
                    String date = rs.getString("date");
                    double price = rs.getDouble("price");
                    boolean paid = rs.getBoolean("paid");

                    Order order = new Order(orderId, userWithId, date, price, paid);
                    orderList.add(order);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return orderList;

    }

}
