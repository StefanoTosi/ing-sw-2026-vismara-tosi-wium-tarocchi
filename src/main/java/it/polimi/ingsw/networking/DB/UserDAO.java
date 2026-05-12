package it.polimi.ingsw.networking.DB;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameDTO;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.networking.JsonUtil;
import it.polimi.ingsw.networking.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO {

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

                System.out.println("Loaded user from the DB");
            }
        }catch(Exception e){
            System.out.println("The DB is not reachable");
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
            System.out.println("The DB is not reachable");
        }
    }

    public static void addRankings(String nickname, int score, int num_player){
        String sql = "INSERT INTO leaderboard (score,num_player,nickname) VALUES (?,?,?)";
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, score);
            ps.setInt(2, num_player);
            ps.setString(3, nickname);

            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("The DB is not reachable");
        }
    }

    public static List<LeaderboardDTO> getLeaderBoard(){
        String sql = "SELECT nickname, num_player, SUM(score) AS total_score FROM leaderboard\n"
                +"GROUP BY nickname, num_player\n"
                +"ORDER BY num_player ASC, total_score DESC";

        List<LeaderboardDTO> leaderboard = new ArrayList<>();

        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String nickname = rs.getString("nickname");
                int num_players = rs.getInt("num_player");
                int score = rs.getInt("total_score");

                leaderboard.add(new LeaderboardDTO(nickname, score, num_players));
            }
        }catch(Exception e){
            System.out.println("The DB is not reachable");
        }
        return leaderboard;
    }

}
