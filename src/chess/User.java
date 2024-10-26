/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
public class User {
    
    private final ChessDB db;
    public int id = -1;
    public String username = "";
    public int rating = -1;

    public User(ChessDB db) {
        this.db = db;
    }

    public boolean login(String username) {
        if (userExists(username)) { // if user is existing
            //set id username and rating
            this.id = db.getIntWhere("id", "UserProfile", "username", username);
            this.username = db.getStringWhere("username", "UserProfile", "username", username);
            this.rating = db.getIntWhere("rating", "UserProfile", "username", username);
            return true; // login sucess
        } else { //if user does not exist
            id = db.createUser(username); // create user
            // set user data
            this.username = username;
            this.rating = db.getIntWhere("rating", "UserProfile", "username", username);
            return true;
        }
    }
    
    public boolean userExists(String username){
        return db.checkIfEntryExists("UserProfile", username, "username");
    }
    
    public void reloadRating(){
        this.rating = db.getIntWhere("rating", "UserProfile", "username", username);
    }
}
