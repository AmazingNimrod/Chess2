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

class King extends Piece {

    King(boolean isWhite) {
        super(isWhite);
        if (isWhite) {
            pieceImage = new ImageIcon("white_king.png").getImage();
        } else {
            pieceImage = new ImageIcon("black_king.png").getImage();
        }
        pieceImage = pieceImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
    }

    @Override
    public String toString() {
        if (isWhite) {
            return ANSI_GREEN + "K" + ANSI_RESET;
        }
        return ANSI_RED + "K" + ANSI_RESET;
    }

    // used GPT to help generate method
    @Override
    public boolean testValid(Map<String, Piece> boardMap, String pos, String move) {
        int[] posCoords = MoveHelper.moveToInt(pos);
        int[] moveCoords = MoveHelper.moveToInt(move);

        // Check for same color piece at destination
        if (boardMap.containsKey(move) && boardMap.get(move).isWhite() == this.isWhite()) {
            return false; // Cannot take a piece of the same color
        }

        return Math.abs(posCoords[0] - moveCoords[0]) <= 1 && Math.abs(posCoords[1] - moveCoords[1]) <= 1;
    }

}
