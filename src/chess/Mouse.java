/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {

    private final GamePanel panel;

    public Mouse(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //get x and y from mouse
        int x = e.getX();
        int y = e.getY();
        
        //calculate square
        int row = x / 100;
        int col = y / 100;

        //logic for mouse prev and current square needed to move piece
        if (row > 0 && row <= 8 && col > 0 && col <= 8) {
            String clickedSquare = toChessNotation(row, col);
            if (panel.selectedSquare.isEmpty()) {
                if (panel.board.getPieceAt(clickedSquare) != null) {
                    panel.selectedSquare = clickedSquare;
                }
            } else {
                panel.prevSquare = panel.selectedSquare;
                panel.selectedSquare = clickedSquare;
                panel.movePiece();
            }

            panel.selectedRow = row;
            panel.selectedCol = col;
            panel.repaint();
        }
    }
    
    //convert xy to chess notation
    private String toChessNotation(int row, int col) {
        char file = (char) ('a' + row - 1);
        int rank = 9 - col;
        return "" + file + rank;
    }
}
