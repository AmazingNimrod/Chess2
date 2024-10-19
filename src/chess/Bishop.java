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

class Bishop extends Piece {

    Bishop(boolean isWhite) {
        super(isWhite);
        if (isWhite) {
            pieceImage = new ImageIcon("white_bishop.png").getImage();
        } else {
            pieceImage = new ImageIcon("black_bishop.png").getImage();
        }
        pieceImage = pieceImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
    }

    @Override
    public String toString() {
        if (isWhite) {
            return ANSI_GREEN + "B" + ANSI_RESET;
        }
        return ANSI_RED + "B" + ANSI_RESET;
    }

    // used GPT to help generate method
    @Override
    public boolean testValid(Map<String, Piece> boardMap, String pos, String move) {
        int[] posCoords = MoveHelper.moveToInt(pos);
        int[] moveCoords = MoveHelper.moveToInt(move);

        if (pos.equals(move)) {
            return false;
        }

        if (Math.abs(posCoords[0] - moveCoords[0]) != Math.abs(posCoords[1] - moveCoords[1])) {
            return false; // Not moving diagonally
        }

        int rowStep = moveCoords[0] > posCoords[0] ? 1 : -1;
        int colStep = moveCoords[1] > posCoords[1] ? 1 : -1;

        int row = posCoords[0] + rowStep;
        int col = posCoords[1] + colStep;

        while (row != moveCoords[0] && col != moveCoords[1]) {
            String currentPos = MoveHelper.intToMove(new int[]{row, col});
            if (boardMap.containsKey(currentPos)) {
                return false; // Blocked by another piece
            }
            row += rowStep;
            col += colStep;
        }

        // Check for same color piece at destination
        return !boardMap.containsKey(move) || boardMap.get(move).isWhite() != this.isWhite(); // Cannot take a piece of the same color
    }

}
