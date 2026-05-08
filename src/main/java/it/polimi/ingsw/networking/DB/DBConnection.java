package it.polimi.ingsw.networking.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DB_NAME = "gc13_mesos_db";
    private static final String DB_URL = HOST + DB_NAME;

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
            //create table leaerboard
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS classifica (\n"
                    + " id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n"
                    + " score int(11) DEFAULT NULL,\n"
                    + " played_at datetime DEFAULT current_timestamp(),\n"
                    + " num_player int(11) DEFAULT NULL,\n"
                    + " nickname varchar(35) DEFAULT NULL,\n"
                    + " FOREIGN KEY (nickname) REFERENCES users(nickname)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try{
            System.out.println("Connected to the database");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
