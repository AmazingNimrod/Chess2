/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

/**
 *
 * @author nyima
 */
import java.io.IOException;
import java.util.Scanner;

public class Game {
    final Scanner scan = new Scanner(System.in); // scanner
    final Board board = new Board(); // board
    final FileIO io; // io

    {
        try {
            io = new FileIO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidInput(String input) { //check if it is a valid input string
        return input.length() == 2 &&
                input.charAt(0) >= 'a' && input.charAt(0) <= 'h' &&
                input.charAt(1) >= '1' && input.charAt(1) <= '8';
    }

    public void startGame() throws IOException { // start game / game loading
        System.out.println("load previous game (y/n)");
        while (true) { // loops until it get y/n
            try {
                String userInput = scan.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    io.loadGame(board);
                    break;
                } else if (userInput.equalsIgnoreCase("n")) {
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input");
                System.out.println("load previous game (y/n)");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        playGame(); // start chess game loop
    }

    public void playGame() throws IOException {
        while (true) {
            board.printBoard(); // display board
            String pos;
            if (!board.endCondition(board.whiteTurn).equals("none")){ //check for end condition
                System.out.println(board.endCondition(board.whiteTurn)+" Game over");
                io.saveGame(board); //save game
                return; // exit
            }
            if (board.isInCheck(board.whiteTurn)){ // tell player if in check
                System.out.println("check");
            }
            System.out.println("'exit' to exit and save or select");
            while (true) { // check for exit or get a valid piece
                System.out.print("Piece: ");
                pos = scan.next();
                if (pos.equalsIgnoreCase("exit")) { // save and exit game
                    io.saveGame(board);
                    return;
                }
                if (board.boardMap.containsKey(pos)) {
                    if (MoveHelper.valid(board.boardMap, pos, board).length() > 0){
                        break;
                    }
                    System.out.println("no possible moves"); // reprompt player for piece if selected piece has no moves
                }
                System.out.println("Invalid position"); // reprompt player for piece if selected piece does not exist
            }
            System.out.println("Available moves: ");
            System.out.println(MoveHelper.valid(board.boardMap, pos, board)); // show player all valid moves for the piece
            String move;

            while (true) { //get valid move
                System.out.print("Square: ");
                move = scan.next();
                if (isValidInput(move)) {
                    break;
                }
                System.out.println("Invalid move");
            }

            board.movePiece(pos, move); // move piece
        }
    }
}


