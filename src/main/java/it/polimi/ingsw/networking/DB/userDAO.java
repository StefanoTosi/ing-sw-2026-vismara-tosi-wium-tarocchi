package it.polimi.ingsw.networking.DB;

import it.polimi.ingsw.networking.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class userDAO {

    public static void loadUser(Map<String, User> users){
        String sql = "SELECT * FROM users";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String nickname = rs.getString("nickname");
                String password = rs.getString("psw");

                User user = new User(nickname,password);

                users.put(nickname,user);

                System.out.println("Loaded user from the DB\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addUsers(User user){
        String sql = "INSERT INTO users (nickname,psw) VALUES (?,?)";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getNickname());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addRankings(String nickname, int score, int num_player){
        String sql = "INSERT INTO leaerboard (score,num_player,nickname) VALUES (?,?,?)";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, score);
            ps.setInt(2, num_player);
            ps.setString(3, nickname);

            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void printLeaerBoard(){
        String sql = "SELECT nickname, num_player, SUM(score) AS total_score, played_at FROM leaerboard"
                +"GROUP BY nickname, num_player"
                +"ORDER BY num_player ASC, total_score DESC";

        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int cur_players = 0;

            while(rs.next()){
                String nickname = rs.getString("nickname");
                int num_player = rs.getInt("num_player");
                int score = rs.getInt("total_score");
                String played_at = rs.getString("played_at");
                //TODO capiamo dove vogliamo salvarlo
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
