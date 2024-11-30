import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerTest {

    private CardGame game;
    private Player player;

    @BeforeEach
    public void setUp() {
        game = CardGame.getInstance();
        player = new Player(1, game);
        deletePlayerOutputFiles();
    }

    @AfterEach
    public void tearDown() {
        deletePlayerOutputFiles();
    }

    private static void deletePlayerOutputFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.matches("player\\d+_output\\.txt"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName()); // TODO: Remove this line after testing
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }
        } else {
            System.out.println("No files found.");
        }
    }

    @Test
    public void testRunInitialHandLogging() {
        player.addToHand(new Card(1));
        player.addToHand(new Card(2));
        player.addToHand(new Card(3));
        player.addToHand(new Card(4));

        Thread playerThread = new Thread(player);
        playerThread.start();

        try {
            playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File("player1_output.txt");
        assertTrue(file.exists(), "Log file should be created");

        // Check if the log file contains the initial hand
        String expected = "player 1 initial hand: 1 2 3 4 ";
        String actual = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            actual = br.readLine();
        } catch (Exception e) {
            fail("Error finding/reading file.");
        }
        assertEquals(expected, actual, "Log file should contain the initial hand");
        
        //file.delete(); // Clean up after test
    }

    @Test
    public void testRunWinCondition() {
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));

        Thread playerThread = new Thread(player);
        playerThread.start();

        try {
            playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, Player.getWinner(), "Player 1 should be the winner");
    }

    @Test
    public void testRunGameOverLogging() {
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));
        player.addToHand(new Card(1));

        Thread playerThread = new Thread(player);
        playerThread.start();

        try {
            playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File("player1_output.txt");
        assertTrue(file.exists(), "Log file should be created");

        // Check if the log file contains the win message
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean winMessageFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("player 1 wins")) {
                    winMessageFound = true;
                    break;
                }
            }
            assertTrue(winMessageFound, "Log file should contain the win message");
        } catch (Exception e) {
            e.printStackTrace();
        }

        file.delete(); // Clean up after test
    }

    @Test
    public void testRunLogCurrentHand() {
        game = CardGame.getInstance();
        int numberOfPlayers = 4;
        ArrayList<Integer> pack = new ArrayList<>(Arrays.asList(
        2, 1, 6, 7, 1, 6, 7, 2, 6, 8, 8, 7, 3, 7, 1, 8, 6, 2, 2, 6, 
        5, 1, 3, 3, 5, 5, 8, 5, 2, 8, 7, 3
        )); // Test pack with 32 cards (4 players)

        try {
            Method startGameMethod = CardGame.class.getDeclaredMethod("startGame", int.class, ArrayList.class);
            startGameMethod.setAccessible(true);
            startGameMethod.invoke(game, numberOfPlayers, pack);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting startGame method");
        }

        File file = new File("player1_output.txt");
        assertTrue(file.exists(), "Log file should be created");

        // Check if the log file contains the current hand
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean handLogged = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("player 1 current hand:")) {
                    handLogged = true;
                    break;
                }
            }
            assertTrue(handLogged, "Log file should contain the output of the current hand");
        } catch (Exception e) {
            e.printStackTrace();
        }

        file.delete(); // Clean up after test
    }
}