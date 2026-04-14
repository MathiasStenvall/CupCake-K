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
        String sql = "SELECT user_id, first_name, last_name, role, email, password, balance FROM Users WHERE email = ? AND password = ?";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, inputEmail);
            ps.setString(2, inputPassword);
            ResultSet rs = ps.executeQuery();

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

    public void createClieant(String firstname, String lastname, String email, String password1, String password2) {
        String createClient = "INSERT INTO users (first_name, last_name, role, email, password)" +
                "VALUES (?, ?, 'client', ?, ?)";

        try (Connection connection = cp.getConnection();
             PreparedStatement ps = connection.prepareStatement(createClient)) {

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, password1);

            if (password1.equals(password2))
                ps.executeQuery();
            else
                System.out.println("Adgangskode matcher ikke");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUserBalance(int userId, double balance){
        String sql = "UPDATE users SET balance = ? WHERE user_id =  ?";

        try (Connection connection = cp.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setDouble(1, balance);
            ps.setInt(2,userId);

            int rows = ps.executeUpdate();

            if (rows == 0){
                System.out.println("User id: " + userId + " was not found");
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}

