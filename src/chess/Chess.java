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
//        Game game = new Game(); // create game
//        game.startGame(); // start game
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel panel = new Panel();
        frame.add(panel);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }
}
