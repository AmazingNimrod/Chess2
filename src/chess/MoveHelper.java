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

public class MoveHelper {
    public static int[] moveToInt(String move) { //convert move to int coordinates
        int[] coords = new int[2];
        coords[0] = 8 - Character.getNumericValue(move.charAt(1));
        coords[1] = move.charAt(0) - 'a';
        return coords;
    }

    public static String intToMove(int[] coords) { // convert int coordinates to chess move
        return "" + (char)('a' + coords[1]) + (8 - coords[0]);
    }

    public static StringBuilder valid(Map<String, Piece> boardMap, String pos, Board board) { //generates string for all valid moves
        StringBuilder valid = new StringBuilder();
        Piece piece = boardMap.get(pos); //get piece

        if (piece == null) {
            return valid; // if no piece return
        }

        for (char col = 'a'; col <= 'h'; col++) {
            for (int a = 1; a <= 8; a++) { // loop over every position on the board
                String move = "" + col + a;

                if (piece.testValid(boardMap, pos, move)) { // use pieces logic to test if move is valid

                    Piece originalPiece = boardMap.get(move); // piece to revert move
                    board.setPieceAt(move, piece);
                    board.setPieceAt(pos, null); // simulate move


                    boolean putsInCheck = board.isInCheck(piece.isWhite()); // check if move will cause own king to be in check


                    board.setPieceAt(pos, piece);
                    board.setPieceAt(move, originalPiece); // restore original board state


                    if (!putsInCheck) { // if move is valid and does not put own king in check add to valid moves string
                        valid.append(move).append(" ");
                    }
                }
            }
        }
        return valid; // return all valid moves
    }
}

