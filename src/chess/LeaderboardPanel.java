/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;


import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author nyima
 */
public class LeaderboardPanel extends JPanel {

    JTextArea leaderText;
    ChessDB db;

    public LeaderboardPanel(ChessDB db) {
        this.db = db;
        setLayout(new BorderLayout());
        leaderText = new JTextArea();
        leaderText.setEditable(false);
        loadLeaderboard();
        add(new JScrollPane(leaderText));
    }

    private void loadLeaderboard() {
        try {
            ResultSet rs = db.getUsers();
            while (rs.next()) {
                leaderText.append(rs.getString("username")
                        + " - Rating: " + rs.getInt("rating") + "\n");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(LeaderboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
