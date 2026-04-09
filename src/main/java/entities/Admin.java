package entities;

public class Admin extends User {

    public Admin(int userID, String firstName, String lastName, String role, String email, String password, double balance) {
        super(userID, firstName, lastName, role, email, password, balance);
    }
}
