/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.awt.Image;
import java.util.Map;
import javax.swing.ImageIcon;

class Queen extends Piece {
    Queen(boolean isWhite) {
        super(isWhite);
                if (isWhite) {
            pieceImage=new ImageIcon("white_queen.png").getImage();
        } else {
            pieceImage = new ImageIcon("black_queen.png").getImage();
        }
        pieceImage = pieceImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
    }

    @Override
    public String toString() {
        if (isWhite) {
            return ANSI_GREEN + "Q" + ANSI_RESET;
        }
        return ANSI_RED + "Q" + ANSI_RESET;
    }

    @Override
    public boolean testValid(Map<String, Piece> boardMap, String pos, String move) {
        return new Rook(isWhite).testValid(boardMap, pos, move) || new Bishop(isWhite).testValid(boardMap, pos, move);
    }
}


