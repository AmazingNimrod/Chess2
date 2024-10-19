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

class Rook extends Piece {
    Rook(boolean isWhite) {
        super(isWhite);
        if (isWhite) {
            pieceImage=new ImageIcon("white_rook.png").getImage();
        } else {
            pieceImage = new ImageIcon("black_rook.png").getImage();
        }
        pieceImage = pieceImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
    }

    @Override
    public String toString() {
        if (isWhite) {
            return ANSI_GREEN + "R" + ANSI_RESET;
        }
        return ANSI_RED + "R" + ANSI_RESET;
    }

    // used GPT to help generate method
    @Override
    public boolean testValid(Map<String, Piece> boardMap, String pos, String move) {
        int[] posCoords = MoveHelper.moveToInt(pos);
        int[] moveCoords = MoveHelper.moveToInt(move);

        if (posCoords[0] != moveCoords[0] && posCoords[1] != moveCoords[1]) {
            return false;
        }

        if (posCoords[0] == moveCoords[0]) {  // Horizontal move
            int colStep = moveCoords[1] > posCoords[1] ? 1 : -1;
            for (int col = posCoords[1] + colStep; col != moveCoords[1]; col += colStep) {
                String currentPos = MoveHelper.intToMove(new int[]{posCoords[0], col});
                if (boardMap.containsKey(currentPos)) {
                    return false;
                }
            }
        } else {  // Vertical move
            int rowStep = moveCoords[0] > posCoords[0] ? 1 : -1;
            for (int row = posCoords[0] + rowStep; row != moveCoords[0]; row += rowStep) {
                String currentPos = MoveHelper.intToMove(new int[]{row, posCoords[1]});
                if (boardMap.containsKey(currentPos)) {
                    return false;
                }
            }
        }

        // Check for same color piece at destination
        return !boardMap.containsKey(move) || boardMap.get(move).isWhite() != this.isWhite(); // Cannot take a piece of the same color
    }
}
