import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    private Account currentAccount;
    int attempts = 0;

    public Bank() {
    }

    public Account login(String username, String password) {
        Account account = this.accounts.get(username);
        if (account != null && password.equals(account.getPassword())) {
            this.currentAccount = account;
            System.out.println("Login successful");
            return account;
        } else {
            ++this.attempts;
            if (this.attempts >= 4) {
                System.out.println("Login failed after multiple attempts.");
            } else {
                System.out.println("Wrong username or password! Try again.");
            }
            return null;
        }
    }

    public void createAccount(String firstName, String lastName, String userName, String password) {
        Account account = new Account(firstName, lastName, userName, password);
        this.accounts.put(account.getUserName(), account);
        System.out.println("Account created, your ID is: " + account.getId());
    }

    public void deleteAccount() {
        if (this.currentAccount != null) {
            this.accounts.remove(this.currentAccount.getUserName());
            System.out.println("Account deleted successfully.");
            this.currentAccount = null;
        } else {
            System.out.println("No user is currently logged in to delete an account.");
        }

    }

    public void logout() {
        if (this.currentAccount != null) {
            this.currentAccount = null;
            System.out.println("Logged out successfully.");
        }

    }

    public void deposit(double amount) {
        if (this.currentAccount != null) {
            this.currentAccount.setBalance(this.currentAccount.getBalance() + amount);
            System.out.println("Deposit successful. New balance: " + this.currentAccount.getBalance());
        } else {
            System.out.println("No user is currently logged in.");
        }

    }

    public void withdraw(double amount) {
        if (this.currentAccount != null) {
            if (this.currentAccount.getBalance() >= amount) {
                this.currentAccount.setBalance(this.currentAccount.getBalance() - amount);
                System.out.println("Withdrawal successful. Remaining balance: " + this.currentAccount.getBalance());
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("No user is currently logged in.");
        }

    }
}