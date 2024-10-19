/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.util.Map;

class Queen extends Piece {
    Queen(boolean isWhite) {
        super(isWhite);
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


