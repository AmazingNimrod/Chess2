/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chess;

import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author nyima
 */
public class Chess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ChessDB db = new ChessDB();
        User userAuth = new User(db);

        JFrame frame = new JFrame();
        LoginPanel loginDialog = new LoginPanel(frame, userAuth);
        loginDialog.setVisible(true);

        if (loginDialog.isSucceeded()) {
            MainMenu menu = new MainMenu(userAuth, db);
            menu.setVisible(true);
        } else{
            System.exit(0);
        }
    }
}
