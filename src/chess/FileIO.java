/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.io.*;

public class FileIO {
//    BufferedReader br; // reader
//    BufferedWriter bw; // writer

    public FileIO() throws IOException {


    }

    public void saveGame(Board board) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/chess.txt"));
        if (board.whiteTurn){ // save current turn
            bw.write("true\n");
        }
        else {
            bw.write("false\n");
        }
        for (int row = 8; row >= 1; row--) { // iterate from rank 8 to 1
            StringBuilder line = new StringBuilder();
            for (char col = 'a'; col <= 'h'; col++) { // iterate from file 'a' to 'h'
                String position = "" + col + row;
                Piece piece = board.getPieceAt(position);
                if (piece != null) { // if it has piece
                    String pieceString = piece.toString().replaceAll("\\x1B\\[[;\\d]*m", ""); // remove colour
                    String colorIdentifier;
                    if (piece.isWhite){ // store info for white or black piece
                        colorIdentifier = "W";
                    } else {
                        colorIdentifier = "B";
                    }
                    line.append(colorIdentifier).append(pieceString);
                } else {
                    line.append("null");
                }
                line.append(" ");
            }
            line.append("\n");
            bw.write(line.toString()); // write piece
        }
        bw.close();
    }

    public void loadGame(Board board) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/chess.txt"));
        String turn = br.readLine().trim();
        if (turn.equals("true")) {  //load current turn
            board.whiteTurn = true;
        }
        if (turn.equals("false")) {
            board.whiteTurn = false;
        }
        String line;
        int row = 8;
        while ((line = br.readLine()) != null && row >= 1) { //read 8 rows of board
            String[] split = line.trim().split(" ");
            for (int col = 0; col < split.length; col++) { // read each piece in the col
                String pieceString = split[col];
                String position = "" + (char)('a' + col) + row;

                if (!pieceString.equals("null")) { // if it has a piece
                    char colorIdentifier = pieceString.charAt(0);
                    String pieceType = pieceString.substring(1);
                    boolean isWhite;
                    if (colorIdentifier == 'W') { // get colour
                        isWhite = true;
                    } else {
                        isWhite = false;
                    }

                    switch (pieceType) { //place piece on the board
                        case "P":
                            board.setPieceAt(position, new Pawn(isWhite));
                            break;
                        case "R":
                            board.setPieceAt(position, new Rook(isWhite));
                            break;
                        case "H":
                            board.setPieceAt(position, new Knight(isWhite));
                            break;
                        case "B":
                            board.setPieceAt(position, new Bishop(isWhite));
                            break;
                        case "Q":
                            board.setPieceAt(position, new Queen(isWhite));
                            break;
                        case "K":
                            board.setPieceAt(position, new King(isWhite));
                            break;
                    }
                } else {
                    board.setPieceAt(position, null);
                }
            }
            row--;
        }
        br.close();
    }
}

