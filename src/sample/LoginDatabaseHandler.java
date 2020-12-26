package sample;

import java.sql.*;

public class LoginDatabaseHandler {
    private static LoginDatabaseHandler loginDatabaseHandler = new LoginDatabaseHandler();
    private Connection con;

    private void getConnection() {
        try {
            if(con==null || con.isClosed()) {
                con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe", "c##test2", "test2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHashedPassword(String username) {
        getConnection();
        String password = null;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT password FROM users WHERE username = ?");
            pStmt.setString(1, username);
            ResultSet res = pStmt.executeQuery();
            if(res.next()) {
                password = res.getString("password");
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return password;
    }

    public boolean checkIfUserExists(String username) {
        getConnection();
        boolean userExists = false;
        try {
            PreparedStatement pStmt = con.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            pStmt.setString(1, username);
            ResultSet res = pStmt.executeQuery();
            if(res.next()) {
                 if(res.getInt(1) == 1)
                 {
                     userExists = true;
                 }
            }
            res.close();
            pStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return userExists;
    }

    private void closeConnection()
    {
        try {
            if(con!=null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LoginDatabaseHandler getInstance() {
        return loginDatabaseHandler;
    }
}
