package johnmichaelwoodrow.java3.JDBC.Intro;

import ca.nl.cna.joshtaylor.java3.JDBC.DBProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple example that creates a Database called "STUDENTS".
 */
public class FunWithCreatingDBs {

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(
                DBProperties.DATABASE_URL, DBProperties.DATABASE_USER, DBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE DATABASE STUDENTS";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
