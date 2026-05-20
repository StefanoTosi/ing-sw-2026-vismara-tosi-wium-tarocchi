package it.polimi.ingsw.networking.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Utility class responsible for managing the database connection
 * and initializing the application database structure.
 *
 * This class creates the database and the required tables
 * if they do not already exist.
 */

public class DBConnection {
    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DB_NAME = "gc13_mesos_db";
    private static final String DB_URL = HOST + DB_NAME;

    /**
     * Initializes the database structure.
     * This method:
     *   - Creates the database if it does not exist;
     *    - Selects the database;
     */
    public static void initializeDB(){
        try{
            Connection conn = DriverManager.getConnection(HOST, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            //create DB
            stmt.executeUpdate("CREATE Database IF NOT EXISTS gc13_mesos_db;");
            //use DB
            stmt.executeUpdate("USE gc13_mesos_db;");
            //create table users
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                    "  nickname varchar(35) NOT NULL PRIMARY KEY,\n" +
                    "  psw varchar(35) NOT NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n");
            //create table leaderboard
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS leaderboard (\n"
                    + " id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n"
                    + " score int(11) DEFAULT NULL,\n"
                    + " played_at datetime DEFAULT current_timestamp(),\n"
                    + " num_player int(11) DEFAULT NULL,\n"
                    + " nickname varchar(35) DEFAULT NULL,\n"
                    + " FOREIGN KEY (nickname) REFERENCES users(nickname)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n");
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a connection to the application database.
     *
     * @return a {@link Connection} object if the connection succeeds;
     *         {@code null} otherwise
     */
    public static Connection getConnection(){
        try{
            System.out.println("Connected to the database");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }catch (Exception e){
            System.out.println("Connection Failed!");
            return null;
        }
    }
}
