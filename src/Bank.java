import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bank {
    private Account currentAccount;
    private boolean accountDeleted;
    private final SQLRequests REQUEST;

    public Bank(SQLRequests sqlRequest) {
        this.REQUEST = sqlRequest;
        accountDeleted = false;
    }

    public Account login(String username, String password) {
        try {
            accountDeleted = false;

            ResultSet resultSet = REQUEST.loginRequest(username, password);
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
    //Don't know how to split it correctly. Want to get rid of the try catch

    public void createAccount(Scanner userInput, String firstName, String lastName, String userName, String password) {
            boolean usernameUnique = isUsernameUnique(userName);

            while (!usernameUnique) {
                System.out.println("Username already in use.\nPlease choose a different username:");
                userName = userInput.nextLine();
                usernameUnique = isUsernameUnique(userName);
            }

            REQUEST.createAccountRequest(firstName, lastName, userName, password);

            System.out.println("Account created successfully.");
    }

    private boolean isUsernameUnique(String userName) {
        return REQUEST.isUsernameUniqueRequest(userName) == 0;
    }

    public void logout() {
        if (this.currentAccount != null) {
            this.currentAccount = null;
        }
    }

    public void deposit(double amount) {
        if (this.currentAccount != null) {
                if (REQUEST.depositRequest(amount, currentAccount)) {
                    double newBalance = this.currentAccount.getBalance() + amount;
                    this.currentAccount.setBalance(newBalance);
                    System.out.println("Deposit successful. New balance: " + newBalance);
                } else {
                    System.out.println("Deposit failed.");
                }
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void withdraw(double amount) {
        if (this.currentAccount != null) {
                if (REQUEST.withdrawRequest(amount, currentAccount)) {
                    double newBalance = this.currentAccount.getBalance() - amount;
                    this.currentAccount.setBalance(newBalance);
                    System.out.println("Withdrawal successful. Remaining balance: " + newBalance);
                } else {
                    System.out.println("Insufficient funds or withdrawal failed.");
                }
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void deleteAccount() {
        if (this.currentAccount != null) {
                if (REQUEST.deleteAccountRequest(currentAccount)) {
                    System.out.println("Account deleted successfully.");
                    this.currentAccount = null;
                    accountDeleted = true;
                } else {
                    System.out.println("Delete account failed.");
                }
        } else {
            System.out.println("No user is currently logged in to delete an account.");
        }
    }

    public boolean isAccountDeleted() {
        return accountDeleted;
    }

}