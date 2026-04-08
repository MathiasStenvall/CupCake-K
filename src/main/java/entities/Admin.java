package entities;

public class Admin {

    private int userID;
    private String firstName;
    private String lastName;
    private String role;
    private String email;
    private String password;
    private double balance;

    public Admin(int userID, String firstName, String lastName,
                 String role, String email, String password, double balance) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
