package net.thisisnico.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    private static Connection con;

    public static void connect(String connectionString, String user, String pass){
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection(connectionString, user, pass);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void update(String statement) {
        try {
            getConnection().prepareStatement(statement).executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ResultSet query(String statement) {
        try {
            return getConnection().prepareStatement(statement).executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    public static Connection getConnection() {
        return con;
    }

}
