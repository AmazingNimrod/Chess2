/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package chess;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nyima
 */
public class BoardTest {

    Board board;

    public BoardTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        board = new Board();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitializeBoard() {
        assertNotNull(board.getPieceAt("a1")); // rook should be initialized
        assertTrue(board.getPieceAt("a2") instanceof Pawn);
    }

    @Test
    public void testMovePieceSuccess() {
        assertTrue(board.movePieceSucess("a2", "a3")); // valid pawn move
        assertNull(board.getPieceAt("a2")); // piece should be moved
    }

    @Test
    public void testMovePieceInvalid() {
        assertFalse(board.movePieceSucess("a2", "d4")); // invalid move for a pawn
    }

    @Test
    public void testCheckDetection() {
        board.setPieceAt("e8", new King(false));  // black king
        board.setPieceAt("d6", new Knight(true));   // white rook places king in check
        assertTrue(board.isInCheck(false));       // black king is in check
    }

    @Test
    public void testCheckmate() {
        board.setPieceAt("e8", new King(false)); // black king
        // double check trapped king with knights
        board.setPieceAt("d6", new Knight(true));
        board.setPieceAt("f6", new Knight(true));
        assertEquals("Checkmate", board.endCondition(false));
    }

    public void testTurn() {
        board.setPieceAt("e2", new Pawn(true)); // white pawn
        assertTrue(board.whiteTurn); // white turn

        // check white can move the pawn
        assertTrue(board.movePieceSucess("e2", "e3"));

        // wlack tries to move during white turn
        board.whiteTurn = true;
        board.setPieceAt("e7", new Pawn(false));
        assertFalse(board.movePieceSucess("e7", "e6"));  // black cannot move
    }
}
