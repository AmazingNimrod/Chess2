/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.util.HashMap;
import java.util.Map;

public class Board {
    public final Map<String, Piece> boardMap; // hashmap to store piece and pos
    boolean whiteTurn; // whos turn it is

    public Board() { // default board
        boardMap = new HashMap<>();
        initializeBoard();
        whiteTurn = true;
    }

    public Piece getPieceAt(String position) {
        return boardMap.get(position);
    }

    public void setPieceAt(String position, Piece piece) {
        if (piece != null) {
            boardMap.put(position, piece);
        } else {
            boardMap.remove(position);
        }
    }

    void initializeBoard() {
        // Initialize pawns
        for (char col = 'a'; col <= 'h'; col++) {
            boardMap.put(col + "2", new Pawn(true));
            boardMap.put(col + "7", new Pawn(false));
        }
        // initilize backrow
        initializeBackRow("1", true);
        initializeBackRow("8", false);
    }

    void initializeBackRow(String row, boolean isWhite) {
        boardMap.put("a" + row, new Rook(isWhite));
        boardMap.put("b" + row, new Knight(isWhite));
        boardMap.put("c" + row, new Bishop(isWhite));
        boardMap.put("d" + row, new Queen(isWhite));
        boardMap.put("e" + row, new King(isWhite));
        boardMap.put("f" + row, new Bishop(isWhite));
        boardMap.put("g" + row, new Knight(isWhite));
        boardMap.put("h" + row, new Rook(isWhite));
    }

    public void movePiece(String piecePosition, String move) {
        Piece piece = boardMap.get(piecePosition);
        String s = String.valueOf(MoveHelper.valid(boardMap, piecePosition, this));
        String[] split = s.split(" ");
        if (piece == null) { // check if contains piece
            System.out.println("Invalid piece position");
            return;
        }
        if (whiteTurn != piece.isWhite()) { //check piece is color of turn
            System.out.println("Invalid turn");
            return;
        }
        for (String st : split) { // check if is valid move
            if (st.equals(move)){
                boardMap.remove(piecePosition);
                boardMap.put(move, piece);
                whiteTurn = !whiteTurn;
                return;
            }
        }
        System.out.println("Invalid move");

//        if (!piece.testValid(boardMap, piecePosition, move)) { // use piece logic to check if move is valid for piece
//            System.out.println("Invalid move");
//            return;
//        }
//        boardMap.remove(piecePosition);
//        boardMap.put(move, piece);
//        if (isInCheck(whiteTurn)){
//            System.out.println("Invalid move in check");
//            boardMap.remove(move);
//            boardMap.put(piecePosition, piece);
//            return;
//        }
//        whiteTurn = !whiteTurn;
    }

    public boolean isInCheck(boolean whiteKing) {
        String kingPos = null;
        for (Map.Entry<String, Piece> entry : boardMap.entrySet()) { //loop over all pieces
            if (entry.getValue() instanceof King && entry.getValue().isWhite() == whiteKing) { // look for king of right colour
                kingPos = entry.getKey(); // get pos of king
                break;
            }
        }
        if (kingPos == null) {
            throw new IllegalStateException("King not found");
        }
        for (Map.Entry<String, Piece> entry : boardMap.entrySet()) { // loop over all pieces
            if (entry.getValue().isWhite() != whiteKing) { // exclude own king
                if (entry.getValue().testValid(boardMap, entry.getKey(), kingPos)) { // check if piece can take king
                    return true;
                }
            }
        }

        return false;
    }

    public String endCondition(boolean whiteTurn) {
        boolean isInCheck = isInCheck(whiteTurn);
        boolean hasValidMove = false;

        // Create a copy of the board map to stop causing errors
        Map<String, Piece> boardCopy = new HashMap<>(boardMap);

        for (Map.Entry<String, Piece> entry : boardCopy.entrySet()) { // loop over all pieces
            if (entry.getValue().isWhite() == whiteTurn) { //check piece is colour of turn
                String pos = entry.getKey();

                // Generate all possible moves for the current piece
                for (char col = 'a'; col <= 'h'; col++) {
                    for (int row = 1; row <= 8; row++) {
                        String move = "" + col + row;

                        // Check if this move is valid
                        if (entry.getValue().testValid(boardMap, pos, move)) {
                            // Simulate the move
                            Piece movedPiece = boardMap.get(pos);
                            Piece capturedPiece = boardMap.put(move, movedPiece);
                            boardMap.remove(pos);

                            // Check if the move leaves the king in check
                            boolean stillInCheck = isInCheck(whiteTurn);

                            // Revert the move
                            boardMap.put(pos, movedPiece);
                            if (capturedPiece != null) {
                                boardMap.put(move, capturedPiece);
                            } else {
                                boardMap.remove(move);
                            }

                            // If the move gets the king out of check or doesn't leave it in check
                            if (!stillInCheck) {
                                hasValidMove = true;
                                break;
                            }
                        }
                    }
                    if (hasValidMove) break;
                }
            }
            if (hasValidMove) break;
        }

        if (!hasValidMove) { // check if there are no valid moves left
            if (isInCheck) {
                return "Checkmate"; // if no valid moves and in check checkmate
            } else {
                return "Stalemate"; // no valid moves and not in check stalemate
            }
        }

        return "none";
    }



    public void printBoard() {
        System.out.print("    ");
        for (char k = 'A'; k <= 'H'; k++) { // header
            System.out.print(k + " ");
        }
        System.out.println();
        for (int i = 8; i >= 1; i--) { // print board
            System.out.print(i + "   "); // header
            for (char j = 'a'; j <= 'h'; j++) {
                String position = j + Integer.toString(i);
                Piece piece = boardMap.get(position);
                if (piece != null) {
                    System.out.print(piece + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        System.out.print("    ");
        for (char k = 'A'; k <= 'H'; k++) { // header
            System.out.print(k + " ");
        }
        System.out.println();
    }
}