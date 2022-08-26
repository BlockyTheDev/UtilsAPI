package dev.dontblameme.utilsapi.mysql;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.Optional;

@Getter
public class MySQL {

    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    /**
     *
     * @param hostname Hostname / IP / Domain of the server to connect to
     * @param port Port of the server
     * @param database Database-Name
     * @param username Username of the account
     * @param password Password of the account
     */
    public MySQL(String hostname, int port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * @apiNote Starts a connection to the provided server
     * @return If it was able to connect
     */
    public boolean connect() {
        if(isConnected()) return true;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
            return connection.isValid(10);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return If it is currently connected
     */
    @SneakyThrows
    public boolean isConnected() {
        return (this.connection != null && !connection.isClosed() && connection.isValid(10));
    }

    /**
     *
     * @return If it could successfully disconnect
     */
    public boolean disconnect() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param content Content to send
     */
    public void sendUpdate(String content) {
        if(!isConnected() || getConnection().isEmpty()) return;

        try(PreparedStatement statement = getConnection().get().prepareStatement(content)) {
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @apiNote This does not auto-close the request. Please close it yourself using result.close()
     * @param content Content to send
     * @return Nullable ResultSet of the request
     */
    public Optional<ResultSet> sendQuery(String content) {
        if(!isConnected() || getConnection().isEmpty()) return Optional.empty();

        try {
            return Optional.ofNullable(getConnection().get().prepareStatement(content).executeQuery());
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     *
     * @return The current connection which could be null if it is not connected
     */
    public Optional<Connection> getConnection() {
        return Optional.ofNullable(connection);
    }
}
