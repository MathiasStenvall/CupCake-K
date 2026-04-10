package entities;

public class Client extends User {

    public Client(int userID, String firstName, String lastName, String role, String email, String password, double balance) {
        super(userID, firstName, lastName, role, email, password, balance);
    }
}

