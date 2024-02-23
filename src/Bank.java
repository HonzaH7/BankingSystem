import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bank {
    private Account currentAccount;
    private Connection connection;

    public Bank() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/BankingSystem", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account login(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE userName = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("userName"),
                        resultSet.getString("password")
                );
                account.setBalance(resultSet.getDouble("balance"));
                this.currentAccount = account;
                System.out.println("Login successful");
                return account;
            } else {
                System.out.println("Wrong username or password! Try again.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createAccount(String firstName, String lastName, String userName, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (firstName, lastName, userName, password, balance) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);
            statement.setString(4, password);
            statement.setDouble(5, 0.0);
            statement.executeUpdate();
            System.out.println("Account created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        if (this.currentAccount != null) {
            this.currentAccount = null;
        }
    }

    public void deposit(double amount) {
        if (this.currentAccount != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE userName = ?");
                statement.setDouble(1, amount);
                statement.setString(2, this.currentAccount.getUserName());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    double newBalance = this.currentAccount.getBalance() + amount;
                    this.currentAccount.setBalance(newBalance);
                    System.out.println("Deposit successful. New balance: " + newBalance);
                } else {
                    System.out.println("Deposit failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void withdraw(double amount) {
        if (this.currentAccount != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE userName = ? AND balance >= ?");
                statement.setDouble(1, amount);
                statement.setString(2, this.currentAccount.getUserName());
                statement.setDouble(3, amount);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    double newBalance = this.currentAccount.getBalance() - amount;
                    this.currentAccount.setBalance(newBalance);
                    System.out.println("Withdrawal successful. Remaining balance: " + newBalance);
                } else {
                    System.out.println("Insufficient funds or withdrawal failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void deleteAccount() {
        if (this.currentAccount != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE userName = ?");
                statement.setString(1, this.currentAccount.getUserName());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Account deleted successfully.");
                    this.currentAccount = null;
                } else {
                    System.out.println("Delete account failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No user is currently logged in to delete an account.");
        }
    }
}