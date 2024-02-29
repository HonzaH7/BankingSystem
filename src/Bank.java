import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Connection;

public class Bank {
    private Account currentAccount;
    private Connection connection;
    private boolean accountDeleted;

    public Bank() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/BankingSystem", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        accountDeleted = false;
    }

    public Account login(String username, String password) {
        try {
            accountDeleted = false;
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

    public void createAccount(Scanner userInput, String firstName, String lastName, String userName, String password) {
        try {
            boolean usernameUnique = isUsernameUnique(userName);

            while (!usernameUnique) {
                System.out.println("Username already in use.\nPlease choose a different username:");
                userName = userInput.nextLine();
                usernameUnique = isUsernameUnique(userName);
            }

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

    private boolean isUsernameUnique(String userName) throws SQLException {
        PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM accounts WHERE username = ?");
        checkStatement.setString(1, userName);
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count == 0;
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
                    accountDeleted = true;
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

    public boolean isAccountDeleted() {
        return accountDeleted;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }
}