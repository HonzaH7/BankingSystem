import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLRequests {
    private final Connection CONNECTION;
    private static final Logger logger = LoggerFactory.getLogger(SQLRequests.class);

    public SQLRequests(Connection connection) {
        this.CONNECTION = connection;
    }

    public ResultSet loginRequest(String username, String password) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM accounts WHERE userName = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeQuery();
        } catch (SQLException e) {
            logger.error("Error executing login request", e);
            return null;
        }
    }

    public void createAccountRequest(String firstName, String lastName, String userName, String password) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("INSERT INTO accounts (firstName, lastName, userName, password, balance) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);
            statement.setString(4, password);
            statement.setDouble(5, 0.0);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error executing create account request", e);
        }
    }

    public int isUsernameUniqueRequest(String userName) {
        try {
            PreparedStatement checkStatement = CONNECTION.prepareStatement("SELECT COUNT(*) FROM accounts WHERE username = ?");
            checkStatement.setString(1, userName);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error("Error executing username uniqueness check request", e);
            return 0;
        }
    }

    public boolean depositRequest(double amount, Account currentAccount) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE userName = ?");
            statement.setDouble(1, amount);
            statement.setString(2, currentAccount.getUserName());
            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error executing deposit request", e);
            return false;
        }
        return false;
    }

    public boolean withdrawRequest(double amount, Account currentAccount) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE userName = ? AND balance >= ?");
            statement.setDouble(1, amount);
            statement.setString(2, currentAccount.getUserName());
            statement.setDouble(3, amount);
            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error executing withdraw request", e);
            return false;
        }
        return false;
    }

    public boolean deleteAccountRequest(Account currentAccount) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("DELETE FROM accounts WHERE userName = ?");
            statement.setString(1, currentAccount.getUserName());
            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error executing delete account request", e);
            return false;
        }
        return false;
    }
}
