package persistence;

import entities.Cupcake;
import entities.CupcakeList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CupcakeMapper {

    private final ConnectionPool cp;

    public CupcakeMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public void generateCupcakes() {
        String sql = "INSERT INTO cupcakes (topping_id, base_id, price) " +
                     "SELECT " +
                     "t.topping_id, " +
                     "b.base_id, " +
                     "b.price + t.price " +
                     "FROM bases b " +
                     "CROSS JOIN toppings t " +
                     "ON CONFLICT (topping_id, base_id) DO NOTHING";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllCupcakes(CupcakeList cupcakeList) {
        String sql =  "SELECT c.cupcake_id, b.base_id, b.name AS base_name, t.topping_id, t.name AS topping_name, c.price " +
                      "FROM cupcakes c JOIN bases b ON c.base_id = b.base_id " +
                      "JOIN toppings t ON t.topping_id = c.topping_id ";
        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cupcake_id");
                int baseId = rs.getInt("base_id");
                String baseName = rs.getString("base_name");
                int toppingId = rs.getInt("topping_id");
                String toppingName = rs.getString("topping_name");
                double price = rs.getDouble("price");

                Cupcake cupcake = new Cupcake(id, baseId, baseName, toppingId, toppingName, price);
                cupcakeList.addCupcake(cupcake);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
