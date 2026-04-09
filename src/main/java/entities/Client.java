package entities;

public class Client extends User {

    private final String role = "client";

    public Client(int userID, String firstName, String lastName,
                  String email, String password, double balance) {
        super(userID, firstName, lastName, email, password, balance);
    }

    public String getRole() {
        return role;
    }
}

