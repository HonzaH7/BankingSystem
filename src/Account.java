public class Account {
    private static int nextId = 1;
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String password;
    private double balance;

    public Account(String firstName, String lastName, String userName, String password) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.balance = 0.0;
    }

    public String getUserName() {
        return this.userName;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}