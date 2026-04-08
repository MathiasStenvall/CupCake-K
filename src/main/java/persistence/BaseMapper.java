package persistence;

import entities.Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseMapper {

    private final ConnectionPool cp;

    public BaseMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public List<Base> getAllBases() {
        List<Base> baseList = new ArrayList<>();
        String sql = "SELECT base_id, name, price FROM bases";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("base_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                Base base = new Base(id, name, price);
                baseList.add(base);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return baseList;
    }
}
