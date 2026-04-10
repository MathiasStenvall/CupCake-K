package persistence;

import entities.Admin;
import entities.Client;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper {

    private final ConnectionPool cp;

    public UserMapper(ConnectionPool cp) {
        this.cp = cp;
    }

    public User login(String inputEmail, String inputPassword) {

        User user = null;
        String sql = "SELECT user_id, first_name, last_name, role, email, password, balance FROM Users WHERE email = ? AND password = ?";

        try (Connection connection = cp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, inputEmail);
            preparedStatement.setString(2, inputPassword);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int userID = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String role = rs.getString("role");
                String email = rs.getString("email");
                String password = rs.getString("password");
                double balance = rs.getDouble("balance");

                if (role.equals("admin")) {
                    return new Admin(userID, firstName, lastName, role, email, password, balance);

                } else {
                    return new Client(userID, firstName, lastName, role, email, password, balance);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

