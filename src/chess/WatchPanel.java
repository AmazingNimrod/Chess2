/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author nyima
 */
public class WatchPanel extends JPanel {

    final int scale = 100; // square size
    public Board board = new Board();
    private final Color chessGreen = Color.decode("#779556");
    private final Color chessWhite = Color.decode("#EBECD0");
    public ChessDB db;
    public int currentGameId = -1;
    public int currentIndex = 0;

    public WatchPanel(ChessDB db) {
        this.db = db; // get db
        this.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        JButton next = new JButton("NEXT");
        JButton prev = new JButton("PREV");

        next.addActionListener(e -> next());
        prev.addActionListener(e -> prev());
        buttonPanel.add(prev);
        buttonPanel.add(next);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void next() {
        if (currentIndex < board.moveList.size()) {
            String move = board.moveList.get(currentIndex);
            String[] split = move.split(" ");
            
            Piece piece = board.getPieceAt(split[0]);
            if (piece != null) {
                // store taken piece if any
                Piece takenPiece = board.getPieceAt(split[1]);
                if (takenPiece != null) {
                    board.takenPieces.put(currentIndex, takenPiece);
                }
                
                // move piece
                board.boardMap.remove(split[0]);
                board.boardMap.put(split[1], piece);
            }
            
            currentIndex++;
            repaint();
        }
    }

    public void prev() {
        if (currentIndex > 0) {
            currentIndex--;
            String move = board.moveList.get(currentIndex);
            String[] split = move.split(" ");
            
            // get the piece that moved
            Piece piece = board.getPieceAt(split[1]);
            if (piece != null) {
                // move piece back
                board.boardMap.remove(split[1]);
                board.boardMap.put(split[0], piece);
                
                // restore taken piece
                if (board.takenPieces.containsKey(currentIndex)) {
                    Piece takenPiece = board.takenPieces.get(currentIndex);
                    if (takenPiece != null) {
                        board.boardMap.put(split[1], takenPiece);
                    }
                }
            }
            
            repaint();
        }
    }

    public void loadGame(int gameId) {
        board = new Board();
        this.currentGameId = db.loadGame(gameId, board);
        this.currentIndex = board.moveList.size();
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBoard(g); // draw board
        paintPieces(g); // draw pieces
        showCheck(g); // highlight king in check
    }

    //methods to convert chess move to x,y
    public int x(String pos) {
        return MoveHelper.moveToInt(pos)[1] + 1;
    }

    public int y(String pos) {
        return MoveHelper.moveToInt(pos)[0] + 1;
    }

    public void showCheck(Graphics g) {
        String kingPos;
        if (board.isInCheck(board.whiteTurn)) { // if king in check
            for (Map.Entry<String, Piece> entry : board.boardMap.entrySet()) { //loop over all pieces
                if (entry.getValue() instanceof King && entry.getValue().isWhite() == board.whiteTurn) { // look for king of right colour
                    kingPos = entry.getKey(); // get pos of king
                    g.setColor(new Color(255, 0, 0, 100)); //highlight red
                    g.fillRect(scale * x(kingPos), scale * y(kingPos), 100, 100); //highlight king
                }
            }
        }
    }

    public void paintPieces(Graphics g) {
        for (char c = 'a'; c <= 'h'; c++) {
            for (int a = 1; a <= 8; a++) { // loop through board
                String pos = c + Integer.toString(a);
                if (board.getPieceAt(pos) != null) { // if square has peice
                    Piece piece = board.getPieceAt(pos);
                    g.drawImage(piece.pieceImage, scale * x(pos), scale * y(pos), this); // put image on board
                }
            }
        }
    }

    public void paintBoard(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, this.getWidth(), this.getHeight()); // red outline

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(chessWhite); // white square
                } else {
                    g.setColor(chessGreen); // black square
                }
                g.fillRect(scale * row, scale * col, 100, 100); // paint squares
            }
        }
    }

}
