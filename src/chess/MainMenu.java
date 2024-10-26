/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author nyima
 */
class MainMenu extends JFrame {

    //size
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 200;

    public MainMenu(User userAuth, ChessDB db) {
        setTitle("Chess Game Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(new BorderLayout());
        //user and rating information lables
        JLabel userLabel = new JLabel("User: " + userAuth.username);
        JLabel ratingLabel = new JLabel("Rating: " + userAuth.rating);

        //buttons
        JButton newGameButton = new JButton("Start New Game");
        JButton previousGamesButton = new JButton("Previous Games");
        JButton leaderboardButton = new JButton("Leaderboard");

        newGameButton.addActionListener(e -> {
            // basic code for new jframe
            JFrame gameFrame = new JFrame("Chess Game");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.add(new StartGamePanel(db, userAuth));
            gameFrame.setSize(450, 100);
            gameFrame.setVisible(true);
            gameFrame.setLocationRelativeTo(null);
            
            // code to update user rating after game ends on window close
            gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    userAuth.reloadRating();
                    ratingLabel.setText("Rating: " + userAuth.rating);
                }
            });
        });

        previousGamesButton.addActionListener(e -> {
            // basic code for new jframe
            JFrame historyFrame = new JFrame("Game History");
            historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            historyFrame.add(new GameHistoryPanel(db, userAuth));
            historyFrame.setSize(500, 400);
            historyFrame.setVisible(true);
            historyFrame.setLocationRelativeTo(null);
        });

        leaderboardButton.addActionListener(e -> {
            // basic code for new jframe
            JFrame leaderFrame = new JFrame("Leaderboard");
            leaderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            leaderFrame.add(new LeaderboardPanel(db));
            leaderFrame.setSize(300, 200);
            leaderFrame.setLocationRelativeTo(null);
            leaderFrame.setVisible(true);
        });
        // add labels at top and bottom
        add(userLabel, BorderLayout.NORTH);
        add(ratingLabel, BorderLayout.SOUTH);
        //buttons in the middle
        add(newGameButton, BorderLayout.CENTER);
        add(previousGamesButton, BorderLayout.WEST);
        add(leaderboardButton, BorderLayout.EAST);
        //centre frame
        setLocationRelativeTo(null);
    }
    
}
