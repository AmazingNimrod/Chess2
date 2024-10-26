/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.sql.Timestamp;

/**
 *
 * @author nyima
 */
public class GameList {
    
    // object to store game data
    final int gameId;
    final Timestamp date;
    final String whitePlayer;
    final String blackPlayer;
    final String result;

    GameList(int gameId, Timestamp date, String whitePlayer, String blackPlayer, String result) {
        this.gameId = gameId;
        this.date = date;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.result = result;
    }

    @Override
    public String toString() {
        return "Game ID: "+gameId+": " + whitePlayer + " vs " + blackPlayer + " (" + date.toString() +") "+ result;
    }
}
