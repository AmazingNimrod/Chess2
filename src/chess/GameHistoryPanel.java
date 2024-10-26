/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class GameHistoryPanel extends JPanel {

    private final ChessDB db;
    private final User user;
    private final DefaultListModel<GameList> listModel;
    private final JList<GameList> gameList;

    public GameHistoryPanel(ChessDB db, User Current) {
        this.db = db; // get db
        this.user = Current; // current user
        this.setLayout(new BorderLayout()); // set layout

        listModel = new DefaultListModel<>(); // list model
        gameList = new JList<>(listModel); // create jlist
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //buttons
        JButton loadButton = new JButton("Load Selected Game");
        JButton refreshButton = new JButton("Refresh List");
        JPanel buttonPanel = new JPanel();

        //add actions for buttons
        loadButton.addActionListener(e -> loadSelectedGame());
        refreshButton.addActionListener(e -> refreshGameList());

        add(new JScrollPane(gameList), BorderLayout.CENTER); // make scrollable\
        //add buttons
        buttonPanel.add(loadButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshGameList();
    }

    private void refreshGameList() {
        try {
            listModel.clear(); // clear list
            ResultSet rs = db.getGameList(user); // get game list from db
            while (rs.next()) {
                listModel.addElement(new GameList( // all all games to list
                        rs.getInt("game_id"),
                        rs.getTimestamp("game_date"),
                        rs.getString("white_player"),
                        rs.getString("black_player"),
                        rs.getString("result")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameHistoryPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //load or watch game
    private void loadSelectedGame() {
        GameList selected = gameList.getSelectedValue(); // get selected game
        if (selected != null) {
            if (gameList.getSelectedValue().result.contains("wins")) {
                watchGame(selected);
            } else {
                playGame(selected);
            }
        }
    }

    // loaded the game to gamepanel
    public void playGame(GameList selected) {
        User whitePlayer = new User(db);
        User blackPlayer = new User(db);
        // set users
        whitePlayer.login(selected.whitePlayer);
        blackPlayer.login(selected.blackPlayer);
        //load game panel
        JFrame gameFrame = new JFrame("Chess Game: " + selected.gameId);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GamePanel gamePanel = new GamePanel(whitePlayer, blackPlayer, db);
        gamePanel.loadGame(selected.gameId);
        gameFrame.add(gamePanel);
        gameFrame.setSize(1000, 1000);
        gameFrame.setVisible(true);
    }

    //load game to watch panel
    public void watchGame(GameList selected) {
        JFrame gameFrame = new JFrame("Chess Game: " + selected.gameId);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        WatchPanel gamePanel = new WatchPanel(db);
        gamePanel.loadGame(selected.gameId);
        gameFrame.add(gamePanel);
        gameFrame.setSize(1000, 1000);
        gameFrame.setVisible(true);
    }
}
