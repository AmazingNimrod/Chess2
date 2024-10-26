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

public class GamePanel extends JPanel {

    final int scale = 100; // square size
    public Board board = new Board();
    public int selectedRow = 0;
    public int selectedCol = 0;
    public String selectedSquare = "";
    public String prevSquare = "";
    public String gameState = "none";
    private final Color chessGreen = Color.decode("#779556");
    private final Color chessWhite = Color.decode("#EBECD0");
    public ChessDB db;
    public User whitePlayer;
    public User blackPlayer;
    public int currentGameId = -1;

    public GamePanel(User white, User black, ChessDB db) {
        this.whitePlayer = white; //get player
        this.blackPlayer = black; //get player
        this.db = db; // get db
        Mouse mouseHandler = new Mouse(this);
        addMouseListener(mouseHandler); // mouse

    }

    public void loadGame(int gameId) { //load game
        board = new Board();
        this.currentGameId = db.loadGame(gameId, board);
        repaint();
    }

    public void saveGame() { // save game
        db.saveGame(board.moveList, this, currentGameId);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBoard(g); // draw board
        paintPieces(g); // draw pieces
        showMoves(g); // highlight valid moves
        showCheck(g); // highlight king in check

    }

    //methods to convert chess move to x,y
    public int x(String pos) {
        return MoveHelper.moveToInt(pos)[1] + 1;
    }

    public int y(String pos) {
        return MoveHelper.moveToInt(pos)[0] + 1;
    }

    public void showMoves(Graphics g) {
        if (board.getPieceAt(selectedSquare) != null) { // check for peice
            StringBuilder moves = MoveHelper.valid(board.boardMap, selectedSquare, board); // gave valid moves 
            String[] movearr = moves.toString().split(" ");
            System.out.println("valid: " + moves); // show valid in console
            for (String move : movearr) { //for every valid move
                if (move.length() < 2) {
                    continue; // check it is the right length
                }
                g.setColor(new Color(255, 255, 0, 100)); // highlight yellow
                g.fillRect(scale * x(move), scale * y(move), 100, 100); // highling square
            }
        }
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
            boolean moved = board.movePieceSucess(prevSquare, selectedSquare); // if the move was successful
            if (moved) {
                this.gameState = board.endCondition(board.whiteTurn); // check for game end
                db.saveGame(board.moveList, this, currentGameId); // autosave game
                System.out.println(this.gameState);
                if (!"none".equals(this.gameState)) {
                    endGame(); // check for game end and end game
                }
            }
            if (board.getPieceAt(selectedSquare) != null && board.getPieceAt(prevSquare) == null) { // clear selected squares
                selectedSquare = "";
                prevSquare = "";
            }
            repaint(); //reload board
        }
    }

    //end game update db
    public void endGame() {
        String result = null;
        if ("Checkmate".equals(this.gameState)) {
            if (!board.whiteTurn) {
                System.out.println("WHITE WINS");
                result = whitePlayer.username + " wins";

            } else {
                System.out.println("BLACK WINS");
                result = blackPlayer.username + " wins";
            }
        }
        if ("Stalemate".equals(this.gameState)) {
            System.out.println("DRAW");
            result = "Draw";
        }
        db.endGame(currentGameId, result);
    }

}
