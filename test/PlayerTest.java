import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.lang.reflect.Field;

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
        game = null;
        player = null;
        deletePlayerOutputFiles();
    }


    /**
     * Delete output files matching a specific regex pattern.
     */
    private static void deletePlayerOutputFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.matches("player\\d+_output\\.txt"));

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        } else {
            System.out.println("No files found.");
        }
    }


    @Test
    public void testRunInitialHandLogging() {
        // Setup the player's initial hand and run the player thread
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
        
        file.delete(); // Clean up file after test
    }


    @Test
    public void testRunWinCondition() {
        // Setup the player's initial hand and run the player thread
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
        // Setup the player's initial hand and run the player thread
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

        file.delete(); // Clean up file after test
    }


    @Test
    public void testRunLogCurrentHand() {
        // Use reflection to call the private logCurrentHand method
        try {
            Method logCurrentHandMethod = Player.class.getDeclaredMethod("logCurrentHand", String.class);
            logCurrentHandMethod.setAccessible(true);
            logCurrentHandMethod.invoke(player, "current");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting logCurrentHand method");
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


    @Test
    public void testGetPlayerNumber() {
        assertEquals(1, player.getPlayerNumber(), "Player number should be 1");
    }


    @Test
    public void testGetWinner() {
        // Use reflection to set the winner to -1 before running the test (reset the winner)
        try {
            Field winnerField = Player.class.getDeclaredField("winner");
            winnerField.setAccessible(true);
            winnerField.set(null, -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting winner field");
        }

        // Force a player 2 win 
        Player player2 = new Player(2, game);
        
        player2.addToHand(new Card(2));
        player2.addToHand(new Card(2));
        player2.addToHand(new Card(2));
        player2.addToHand(new Card(2));

        Thread playerThread = new Thread(player2);
        playerThread.start();

        try {
            playerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
        assertEquals(2, Player.getWinner(), "Winner should be 2");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testAddToHand() {
        // Use reflection to add cards to the player's hand
        player.addToHand(new Card(1));
        player.addToHand(new Card(2));
        player.addToHand(new Card(3));
        player.addToHand(new Card(4));

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(new Card(1).getValue());
        expected.add(new Card(2).getValue());
        expected.add(new Card(3).getValue());
        expected.add(new Card(4).getValue());

        ArrayList<Card> hand = null;
        try {
            Field handField = player.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            hand = (ArrayList<Card>) handField.get(player);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Loop through hand and add assert the values are correct
        ArrayList<Integer> actual = new ArrayList<>();
        for (Card card : hand) {
            actual.add(card.getValue());
        }
        
        assertEquals(expected, actual, "Hand should contain 4 cards");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveFromHand() {
        // Use reflection to add cards to the player's hand
        Card card1 = new Card(1);
        Card card2 = new Card(2);
        Card card3 = new Card(3);
        Card card4 = new Card(4);

        player.addToHand(card1);
        player.addToHand(card2);
        player.addToHand(card3);
        player.addToHand(card4);

        try {
            Method removeFromHandMethod = player.getClass().getDeclaredMethod("removeFromHand", Card.class);
            removeFromHandMethod.setAccessible(true);
            removeFromHandMethod.invoke(player, card2);
            removeFromHandMethod.invoke(player, card4);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(card1.getValue());
        expected.add(card3.getValue());

        ArrayList<Card> hand = null;
        try {
            Field handField = player.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            hand = (ArrayList<Card>) handField.get(player);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Loop through hand and add assert the values are correct
        ArrayList<Integer> actual = new ArrayList<>();
        for (Card card : hand) {
            actual.add(card.getValue());
        }
        
        assertEquals(expected, actual, "Hand should contain 2 cards");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testDrawCard() {
        // Create a deck and add cards to it
        Deck deck = new Deck(1);
        deck.addToDeck(new Card(5));
        deck.addToDeck(new Card(6));
        deck.addToDeck(new Card(7));
        deck.addToDeck(new Card(8));

        // Add the deck to the game
        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(deck);
        try {
            Field decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            decksField.set(game, decks);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting deck field");
        }

        // Reflect drawCard method
        try {
            Method drawCardMethod = player.getClass().getDeclaredMethod("drawCard");
            drawCardMethod.setAccessible(true);
            drawCardMethod.invoke(player);
            drawCardMethod.invoke(player);
            drawCardMethod.invoke(player);
            drawCardMethod.invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting drawCard method");
        }

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(8);

        // Reflect hand field and check the values
        ArrayList<Card> hand = null;
        try {
            Field handField = player.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            hand = (ArrayList<Card>) handField.get(player);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        ArrayList<Integer> actual = new ArrayList<>();
        for (Card card : hand) {
            actual.add(card.getValue());
        }

        assertEquals(expected, actual, "Hand should be drawn from deck");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testDiscardCard() {
        // Create a first player 
        Player player1 = new Player(1, game);
        player1.addToHand(new Card(1));
        player1.addToHand(new Card(1));
        player1.addToHand(new Card(3));
        player1.addToHand(new Card(1));
        player1.addToHand(new Card(5));

        // Create a second player
        Player player2 = new Player(2, game);
        player2.addToHand(new Card(2));
        player2.addToHand(new Card(5));
        player2.addToHand(new Card(6));
        player2.addToHand(new Card(1));

        // Create a first deck
        Deck deck1 = new Deck(1);
        deck1.addToDeck(new Card(1));
        deck1.addToDeck(new Card(2));
        deck1.addToDeck(new Card(4));

        // Create a second deck
        Deck deck2 = new Deck(2);
        deck2.addToDeck(new Card(5));
        deck2.addToDeck(new Card(6));
        deck2.addToDeck(new Card(7));
        deck2.addToDeck(new Card(8));

        // Add the 2 players to the game
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        try {
            Field playersField = game.getClass().getDeclaredField("players");
            playersField.setAccessible(true);
            playersField.set(game, players);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting players field");
        }

        // Add the 2 decks to the game
        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(deck1);
        decks.add(deck2);
        try {
            Field decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            decksField.set(game, decks);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting deck field");
        }

        // Reflect discardCard method
        try {
            Method discardCardMethod = player1.getClass().getDeclaredMethod("discardCard");
            discardCardMethod.setAccessible(true);
            discardCardMethod.invoke(player1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting discardCard method");
        }

        // Check the hand of player1
        ArrayList<Integer> expectedPlayer1 = new ArrayList<>();
        expectedPlayer1.add(1);
        expectedPlayer1.add(1);
        expectedPlayer1.add(1);
        expectedPlayer1.add(5);

        // Check the contents of deck2
        ArrayList<Integer> expectedDeck2 = new ArrayList<>();
        expectedDeck2.add(5);
        expectedDeck2.add(6);
        expectedDeck2.add(7);
        expectedDeck2.add(8);
        expectedDeck2.add(3);

        // Reflect player1 hand field
        ArrayList<Card> player1Hand = null;
        try {
            Field handField = player1.getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            player1Hand = (ArrayList<Card>) handField.get(player1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Reflect deck2 deck field
        ArrayList<Card> deck2Value = new ArrayList<>();
        try {
            Field deck2Field = deck2.getClass().getDeclaredField("deck");
            deck2Field.setAccessible(true);
            deck2Value = (ArrayList<Card>) deck2Field.get(deck2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting deck field");
        }

        // Loop through player1 hand and add the values to an arraylist
        ArrayList<Integer> actualPlayer1 = new ArrayList<>();
        for (Card card : player1Hand) {
            actualPlayer1.add(card.getValue());
        }

        // Loop through deck2 and add the values to an arraylist
        ArrayList<Integer> actualDeck2 = new ArrayList<>();
        for (Card card : deck2Value) {
            actualDeck2.add(card.getValue());
        }

        assertEquals(expectedPlayer1, actualPlayer1, "Hand should have discarded the 3");
        assertEquals(expectedDeck2, actualDeck2, "Deck 2 should have received the 3 on the bottom");
    }


    @Test
    public void testCheckWin() {
        // Create a player and add 4 cards of the same value
        Player player3 = new Player(3, game);

        player3.addToHand(new Card(3));
        player3.addToHand(new Card(3));
        player3.addToHand(new Card(3));
        player3.addToHand(new Card(3));

        // Use reflection to call the private checkWin method and check the return value
        boolean actual = false;
        try {
            Method checkWinMethod = player3.getClass().getDeclaredMethod("checkWin");
            checkWinMethod.setAccessible(true);
            actual = (boolean) checkWinMethod.invoke(player3);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting checkWin method");
        }

        assertEquals(true, actual, "A player should have won the game");
    } 
}