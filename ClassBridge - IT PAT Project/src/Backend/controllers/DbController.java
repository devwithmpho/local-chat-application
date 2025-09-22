package Backend.controllers;

import java.sql.*;

public class DbController {
    protected Connection conn;

    public DbController() {}

    // Connecting to database to allow for queries
    public Connection getConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String dbPath = "C://Users//Administrator//Documents//PAT//ClassBridgePAT.accdb";
            String url = "jdbc:ucanaccess://" + dbPath;
            conn = DriverManager.getConnection(url);

            System.out.println("Connected to Database!");
        } catch (Exception e) {
            System.out.println("Error connecting to DB: " + e);
        }

        return conn;
    }
}
