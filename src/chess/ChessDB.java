/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

/**
 *
 * @author nyima
 */
public class ChessDB {

    public final DBManager dbManager;
    private final Connection conn;
    private Statement statement;

    public ChessDB() {
        dbManager = new DBManager();
        conn = dbManager.getConnection();
        createTables();
    }

    private void createTables() { // userprofile table savedgames, and moves table
        try {
            statement = conn.createStatement();

            if (!tableExists("UserProfile")) {
                String createUserProfileTable = "CREATE TABLE UserProfile ("
                        + "id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "username VARCHAR(50), "
                        + "rating INTEGER)";
                statement.executeUpdate(createUserProfileTable);
            }

            if (!tableExists("SavedGames")) {
                String createSavedGamesTable = "CREATE TABLE SavedGames ("
                        + "game_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "white_user_id INTEGER, "
                        + "black_user_id INTEGER, "
                        + "result VARCHAR(10), "
                        + "game_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "FOREIGN KEY (white_user_id) REFERENCES UserProfile(id), "
                        + "FOREIGN KEY (black_user_id) REFERENCES UserProfile(id))";
                statement.executeUpdate(createSavedGamesTable);
            }

            if (!tableExists("Moves")) {
                String createMovesTable = "CREATE TABLE Moves ("
                        + "move_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
                        + "game_id INTEGER, "
                        + "move_number INTEGER, "
                        + "color VARCHAR(5), "
                        + "move VARCHAR(10), "
                        + "FOREIGN KEY (game_id) REFERENCES SavedGames(game_id))";
                statement.executeUpdate(createMovesTable);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //check for existing table
    public boolean tableExists(String tableName) {
        boolean exists = false;
        ResultSet rs;
        try {
            rs = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null); // check if table with tablename exists
            if (rs.next()) {
                exists = true;
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }

    public boolean checkIfEntryExists(String table, String value, String key) {
        try {
            statement = conn.createStatement();
            String query = "SELECT " + key + " FROM " + table + " WHERE " + key + " = '" + value + "'"; // check if a entry exists in table
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) { // if value is obtained
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //get an int from table where item is equal to a value eg id = 1
    public int getIntWhere(String select, String from, String where, String equals) {
        try {
            statement = conn.createStatement();
            String query = "SELECT " + select + " FROM " + from + " WHERE " + where + " = '" + equals + "'"; // check if a entry exists in table
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(select); // return the int
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    //get an String from table where item is equal to a value eg username = user
    public String getStringWhere(String select, String from, String where, String equals) {
        try {
            statement = conn.createStatement();
            String query = "SELECT " + select + " FROM " + from + " WHERE " + where + " = '" + equals + "'";// check if a entry exists in table
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return rs.getString(select); // return entry
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int createUser(String username) {
        try {
            statement = conn.createStatement();
            statement.executeUpdate(
                    "INSERT INTO UserProfile (username, rating) VALUES ('" + username + "', 1200)", Statement.RETURN_GENERATED_KEYS // create user and generate and save id
            );
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // return the new users id
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; // -1 for failed
    }

    public void saveGame(ArrayList<String> moveList, GamePanel p, int gameId) {
        try {
            statement = conn.createStatement();

            if (gameId == -1) { // if it is a new game that has never been saved
                // create game
                String insertGame = "INSERT INTO SavedGames (white_user_id, black_user_id, result, game_date) "
                        + "VALUES (" + p.whitePlayer.id + ", " + p.blackPlayer.id
                        + ", 'ongoing', CURRENT_TIMESTAMP)";

                // get id
                statement.executeUpdate(insertGame, Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    p.currentGameId = keys.getInt(1); // set gamepanel id to generated id
                    gameId = p.currentGameId;
                }
                keys.close();
            }

            // deletes all moves from game with id, needed for prev saved games being resaved
            statement.executeUpdate("DELETE FROM Moves WHERE game_id = " + gameId);

            for (int i = 0; i < moveList.size(); i++) { // loop through the move list
                String move = moveList.get(i);
                String colour;
                if (i % 2 == 0) {
                    colour = "White"; // set color
                } else {
                    colour = "Black";// set color
                }

               // insert move to table
                String insertMove = "INSERT INTO Moves (game_id, move_number, color, move) "
                        + "VALUES (" + gameId + ", " + (i + 1) + ", '"
                        + colour + "', '" + move + "')";

                statement.executeUpdate(insertMove);
            }
            
            // update the date for the game
            statement.executeUpdate("UPDATE SavedGames SET game_date = CURRENT_TIMESTAMP "
                    + "WHERE game_id = " + gameId);

            System.out.println("Game saved successfully with ID: " + gameId);

        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int loadGame(int gameId, Board board) {
        try {
            statement = conn.createStatement();
            
            //look for game
            String gameQuery = "SELECT * FROM SavedGames WHERE game_id = " + gameId;
            ResultSet gameRS = statement.executeQuery(gameQuery);

            if (!gameRS.next()) { // check for game exists
                System.out.println("Game not found");
                return -1;
            }
            
            //reset board before loading
            board.initializeBoard();

            // get all moves
            String movesQuery = "SELECT move FROM Moves WHERE game_id = " + gameId + " ORDER BY move_number";
            ResultSet movesRS = statement.executeQuery(movesQuery);

            while (movesRS.next()) { // loop through all moves
                String move = movesRS.getString("move");
                String[] moveComponents = move.split(" ");
                if (moveComponents.length == 2) { // check for valid values
                    String from = moveComponents[0];
                    String to = moveComponents[1];
                    board.movePiece(from, to);
                    board.moveList.add(move); // perform move
                }
            }

            gameRS.close();
            movesRS.close();

            System.out.println("Game loaded successfully");
            return gameId;

        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public void endGame(int gameId, String result) {
        try {
            statement = conn.createStatement();
            // update the result 
            String updateQuery = "UPDATE SavedGames SET result = '" + result + "' "
                    + "WHERE game_id = " + gameId;
            statement.executeUpdate(updateQuery);
            // get users from game
            updateQuery = "SELECT white_user_id, black_user_id FROM SavedGames WHERE game_id = " + gameId;
            ResultSet rs = statement.executeQuery(updateQuery);
            if (rs.next()) {
                //get user ids
                int whiteId = rs.getInt("white_user_id");
                int blackId = rs.getInt("black_user_id");

                if (result.contains("wins")) { // adjust score based on who is winner
                    if (result.contains(getUsername(whiteId))) {
                        updatePlayerRating(whiteId, 20);
                        updatePlayerRating(blackId, -20);
                    } else {
                        updatePlayerRating(whiteId, -20);
                        updatePlayerRating(blackId, 20);
                    }
                }
            }
            System.out.println("Game ended successfully: " + result);
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePlayerRating(int userId, int ratingChange) {
        try {
            statement = conn.createStatement();
            // update rating for used by id
            String updateQuery = "UPDATE UserProfile SET rating = rating + " + ratingChange
                    + " WHERE id = " + userId;
            statement.executeUpdate(updateQuery);
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsername(int id) {
        try {
            statement = conn.createStatement();
            // look for username based on id
            ResultSet rs = statement.executeQuery("SELECT username, rating FROM UserProfile WHERE id = "+id);
            if (rs.next()){
                return rs.getString("username");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ResultSet getGameList(User user) {
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
            // get games information id, date, result, players
            rs = statement.executeQuery(
                    "SELECT game_id, game_date, result, "
                    + "(SELECT username FROM UserProfile WHERE id = white_user_id) as white_player, "
                    + "(SELECT username FROM UserProfile WHERE id = black_user_id) as black_player "
                    + "FROM SavedGames "
                    + "WHERE white_user_id = " + user.id + " OR black_user_id = " + user.id + " "
                    + "ORDER BY game_date DESC"
            );
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs; //return the resultset
    }

    public ResultSet getUsers() {
        ResultSet rs = null;
        try {
            //get useername and rating for all users
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT username, rating FROM UserProfile ORDER BY rating DESC");
        } catch (SQLException ex) {
            Logger.getLogger(ChessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs; // return resultset
    }

}
