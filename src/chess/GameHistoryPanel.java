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

    public GameHistoryPanel(ChessDB db, User userAuth) {
        this.db = db;
        this.user = userAuth;
        this.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton loadButton = new JButton("Load Selected Game");
        JButton refreshButton = new JButton("Refresh List");
        JPanel buttonPanel = new JPanel();
        
        loadButton.addActionListener(e -> loadSelectedGame());
        refreshButton.addActionListener(e -> refreshGameList());

        add(new JScrollPane(gameList), BorderLayout.CENTER);
        buttonPanel.add(loadButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshGameList();
    }

    private void refreshGameList() {
        try {
            listModel.clear();
            ResultSet rs = db.getGameList(user);
            while (rs.next()) {
                listModel.addElement(new GameList(
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

    private void loadSelectedGame() {
        GameList selected = gameList.getSelectedValue();
        if (selected != null) {
            User whitePlayer = new User(db);
            User blackPlayer = new User(db);
            whitePlayer.login(selected.whitePlayer);
            blackPlayer.login(selected.blackPlayer);
            JFrame gameFrame = new JFrame("Chess Game: " + selected.gameId);
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            GamePanel gamePanel = new GamePanel(whitePlayer, blackPlayer, db);
            gamePanel.loadGame(selected.gameId);
            gameFrame.add(gamePanel);
            gameFrame.setSize(1000, 1000);
            gameFrame.setVisible(true);
        }
    }
}
