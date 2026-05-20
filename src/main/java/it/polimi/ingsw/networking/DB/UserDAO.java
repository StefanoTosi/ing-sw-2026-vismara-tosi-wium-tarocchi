package it.polimi.ingsw.networking.DB;

import it.polimi.ingsw.networking.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object (DAO) for managing users and leaderboard data in the database.
 *
 * Provides methods to:
 * - Load users from DB into memory
 * - Insert new users
 * - Insert leaderboard entries
 * - Retrieve aggregated leaderboard data
 */
public class UserDAO {

    /**
     * Loads all users from the database and stores them into the provided map.
     *
     * @param users map where key is nickname and value is User object
     */
    public static void loadUser(Map<String, User> users){
        String sql = "SELECT * FROM users";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            // Iterate over all users in DB
            while(rs.next()){
                String nickname = rs.getString("nickname");
                String password = rs.getString("psw");

                // Create domain object from DB record
                User user = new User(nickname,password);

                users.put(nickname,user);

                System.out.println("Loaded user from the DB");
            }
        }catch(Exception e){
            System.out.println("The DB is not reachable");
        }
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user user to be inserted
     */
    public static void addUsers(User user){
        String sql = "INSERT INTO users (nickname,psw) VALUES (?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            // Bind parameters to avoid SQL injection
            ps.setString(1, user.getNickname());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("The DB is not reachable");
        }
    }


    /**
     * Inserts a new leaderboard entry for a user.
     *
     * @param nickname   user nickname
     * @param score      score obtained
     * @param num_player number of players in the match
     */
    public static void addRankings(String nickname, int score, int num_player){
        String sql = "INSERT INTO leaderboard (score,num_player,nickname) VALUES (?,?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, score);
            ps.setInt(2, num_player);
            ps.setString(3, nickname);

            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("The DB is not reachable");
        }
    }

    /**
     * Retrieves the aggregated leaderboard.
     *
     * Scores are grouped by nickname and number of players,
     * and ordered by:
     * - number of players (ascending)
     * - total score (descending)
     *
     * @return list of leaderboard entries
     */
    public static List<LeaderboardDTO> getLeaderBoard(){
        String sql = "SELECT nickname, num_player, SUM(score) AS total_score FROM leaderboard\n"
                +"GROUP BY nickname, num_player\n"
                +"ORDER BY num_player ASC, total_score DESC";

        List<LeaderboardDTO> leaderboard = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            // Build DTO list from query result
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
