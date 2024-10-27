/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package chess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ChessDBTest {

    ChessDB db;

    public ChessDBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        db = new ChessDB();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateUser() {
        int userId = db.createUser("testuser"); // create user
        assertTrue(userId > 0); // check user was created
    }

    @Test
    public void testCheckUserExists() {
        db.createUser("existinguser"); // create user
        assertTrue(db.checkIfEntryExists("UserProfile", "existinguser", "username")); // test is user exists
    }

    @Test
    public void testUserDoesNotExist() {
        assertFalse(db.checkIfEntryExists("UserProfile", "nonexistent", "username")); // check for invalid user
    }

    @Test
    public void testSaveAndLoadGame() {
        // make new board and do a move
        Board board = new Board();
        board.movePieceSucess("e2", "e4");

        User whitePlayer = new User(db);
        User blackPlayer = new User(db);
        whitePlayer.login("WhiteUser");
        blackPlayer.login("BlackUser");

        GamePanel gamePanel = new GamePanel(whitePlayer, blackPlayer, db); // make panel to save game

        // save game
        db.saveGame(board.moveList, gamePanel, -1);
        int savedGameId = gamePanel.currentGameId;
        assertTrue(savedGameId > 0); // check for id

        // load game
        Board loadedBoard = new Board();
        int loadedGameId = db.loadGame(savedGameId, loadedBoard);

        // check loaded game is same as saved game
        assertEquals(savedGameId, loadedGameId);
        assertEquals("e2 e4", loadedBoard.moveList.get(0));
    }

}
