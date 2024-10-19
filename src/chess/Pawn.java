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

class Pawn extends Piece {
    Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String toString() {
        if (isWhite) {
            return ANSI_GREEN + "P" + ANSI_RESET;
        }
        return ANSI_RED + "P" + ANSI_RESET;
    }

    // used GPT to help generate method
    @Override
    public boolean testValid(Map<String, Piece> boardMap, String pos, String move) {
        int[] posCoords = MoveHelper.moveToInt(pos);

        int direction = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;

        String oneStepForward = MoveHelper.intToMove(new int[]{posCoords[0] + direction, posCoords[1]});
        String twoStepsForward = MoveHelper.intToMove(new int[]{posCoords[0] + 2 * direction, posCoords[1]});
        String diagonalLeft = MoveHelper.intToMove(new int[]{posCoords[0] + direction, posCoords[1] - 1});
        String diagonalRight = MoveHelper.intToMove(new int[]{posCoords[0] + direction, posCoords[1] + 1});

        // Normal move (one square forward)
        if (move.equals(oneStepForward) && !boardMap.containsKey(move)) {
            return true;
        }

        // Double move from start
        if (posCoords[0] == startRow && move.equals(twoStepsForward) && !boardMap.containsKey(oneStepForward) && !boardMap.containsKey(twoStepsForward)) {
            return true;
        }

        // Diagonal capture
        return (move.equals(diagonalLeft) || move.equals(diagonalRight)) && boardMap.containsKey(move) && boardMap.get(move).isWhite() != isWhite;
    }
}

