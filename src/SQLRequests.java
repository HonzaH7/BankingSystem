import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLRequests {
    private final Connection connection;
    private final Account currentAccount;

    public SQLRequests(Connection connection, Bank bank) {
        this.connection = connection;
        this.currentAccount = bank.getCurrentAccount();
    }
    public Connection getConnection() {
        return this.connection;
    }

    public ResultSet loginRequest(String username, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE userName = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createAccountRequest(String firstName, String lastName, String userName, String password) {
        try {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (firstName, lastName, userName, password, balance) VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, userName);
        statement.setString(4, password);
        statement.setDouble(5, 0.0);
        statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int isUsernameUniqueRequest(String userName) {
        try {
            PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM accounts WHERE username = ?");
            checkStatement.setString(1, userName);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int depositRequest(double amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE userName = ?");
            statement.setDouble(1, amount);
            statement.setString(2, this.currentAccount.getUserName());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int withdrawRequest(double amount) {
        try {
        PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE userName = ? AND balance >= ?");
        statement.setDouble(1, amount);
        statement.setString(2, this.currentAccount.getUserName());
        statement.setDouble(3, amount);
        return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteAccountRequest() {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE userName = ?");
            statement.setString(1, this.currentAccount.getUserName());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
