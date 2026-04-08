package persistence;

import entities.Topping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToppingMapper {

    private final ConnectionPool cp;

    public ToppingMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public List<Topping> getAllToppings() {
        List<Topping> toppingList = new ArrayList<>();
        String sql = "SELECT topping_id, name, price FROM toppings";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("topping_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                Topping topping = new Topping(id, name, price);
                toppingList.add(topping);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toppingList;
    }
}

