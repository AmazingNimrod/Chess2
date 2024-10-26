/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import javax.swing.*;

/**
 *
 * @author nyima
 */
public class StartGamePanel extends JPanel {

    private User white;
    private User black;
    private boolean currentIsWhite = true;

    public StartGamePanel(ChessDB db, User current) {
        JButton button = new JButton("Start");
        JCheckBox box = new JCheckBox("Start As Black?");
        JTextField username = new JTextField(15);

        box.addItemListener(e -> {
            currentIsWhite = e.getStateChange() != 1;
        });

        button.addActionListener(e -> {
            if (username.getText().matches("[a-zA-Z0-9]+")) {
                if (currentIsWhite) {
                    white = current;
                    black = new User(db);
                    black.login(username.getText());
                } else {
                    black = current;
                    white = new User(db);
                    white.login(username.getText());
                }
                System.out.println("White player: " + white.username);
                System.out.println("Black player: " + black.username);

                JFrame gameFrame = new JFrame("Chess Game");
                gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                gameFrame.add(new GamePanel(white, black, db));
                gameFrame.setSize(1000, 1000);
                gameFrame.setVisible(true);
            }
        });

        add(box);
        add(new JLabel("Opponent Username:"));
        add(username);
        add(button);
    }

}
