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

    private final Panel panel;

    public Mouse(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int row = x / 100;
        int col = y / 100;

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

    private String toChessNotation(int row, int col) {
        char file = (char) ('a' + row - 1);
        int rank = 9 - col;
        return "" + file + rank;
    }
}
