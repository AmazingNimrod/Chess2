/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Panel extends JPanel {

    final int scale = 100;
    public Board board = new Board();
    public int selectedRow = 0;
    public int selectedCol = 0;
    public String selectedSquare = "";
    public String prevSquare = "";
    private final Color chessGreen = Color.decode("#779556");
    private final Color chessWhite = Color.decode("#EBECD0");

    public Panel() {
        Mouse mouseHandler = new Mouse(this);
        addMouseListener(mouseHandler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBoard(g);
        paintPieces(g);
        showMoves(g);
        showCheck(g);
    }

    //methods to convert chess move to x,y
    public int x(String pos) {
        return MoveHelper.moveToInt(pos)[1] + 1;
    }

    public int y(String pos) {
        return MoveHelper.moveToInt(pos)[0] + 1;
    }

    public void showMoves(Graphics g) {
        if (board.getPieceAt(selectedSquare) != null) {
            StringBuilder moves = MoveHelper.valid(board.boardMap, selectedSquare, board);
            String[] movearr = moves.toString().split(" ");
            System.out.println("valid: " + moves);
            for (String move : movearr) {
                if (move.length() < 2) {
                    continue;
                }
                g.setColor(new Color(255, 255, 0, 100));
                g.fillRect(scale * x(move), scale * y(move), 100, 100);
            }
        }
    }

    public void showCheck(Graphics g) {
        String kingPos = null;
        if (board.isInCheck(board.whiteTurn)) {
            for (Map.Entry<String, Piece> entry : board.boardMap.entrySet()) { //loop over all pieces
                if (entry.getValue() instanceof King && entry.getValue().isWhite() == board.whiteTurn) { // look for king of right colour
                    kingPos = entry.getKey(); // get pos of king
                    g.setColor(new Color(255, 0, 0, 100));
                    g.fillRect(scale * x(kingPos), scale * y(kingPos), 100, 100);
                }
            }
        }
    }

    public void paintPieces(Graphics g) {
        for (char c = 'a'; c <= 'h'; c++) {
            for (int a = 1; a <= 8; a++) {
                String pos = c + Integer.toString(a);
                if (board.getPieceAt(pos) != null) {
                    Piece piece = board.getPieceAt(pos);
                    g.drawImage(piece.pieceImage, scale * x(pos), scale * y(pos), this);
                }
            }
        }
    }

    public void paintBoard(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(chessWhite);
                } else {
                    g.setColor(chessGreen);
                }
                g.fillRect(scale * row, scale * col, 100, 100); // paint squares

                //paint selected square
                if (!selectedSquare.equals("") && row == x(selectedSquare) && col == y(selectedSquare)) {
                    g.setColor(new Color(255, 255, 0, 100));
                    g.fillRect(scale * x(selectedSquare), scale * y(selectedSquare), 100, 100);
                }
            }
        }
    }

    public void movePiece() {
        if (!selectedSquare.equals("") && !prevSquare.equals("")) {
            board.movePiece(prevSquare, selectedSquare);

            if (board.getPieceAt(selectedSquare) != null && board.getPieceAt(prevSquare) == null) {
                selectedSquare = "";
                prevSquare = "";
            }
            repaint();
        }
    }
}
