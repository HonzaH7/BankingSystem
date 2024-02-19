public class Account {
    private static int nextId = 1;
    private final int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private double balance;
    private boolean loggedIn;

    public Account(String firstName, String lastName, String userName, String password) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.balance = 0.0;
    }

    public int getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getPassword() {
        return this.password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
