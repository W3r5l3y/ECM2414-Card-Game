import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    private CardGame game;

    @BeforeEach
    public void setUp() {
        game = CardGame.getInstance();

        // Reflect player field set to new ArrayList
        ArrayList<Player> players = new ArrayList<>();
        try {
            Field playersField = game.getClass().getDeclaredField("players");
            playersField.setAccessible(true);
            playersField.set(game, players);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Reflect playerThreads field set to new ArrayList
        ArrayList<Thread> playerThreads = new ArrayList<>();
        try {
            Field playerThreadsField = game.getClass().getDeclaredField("playerThreads");
            playerThreadsField.setAccessible(true);
            playerThreadsField.set(game, playerThreads);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Reflect decks field and sets to new ArrayList
        ArrayList<Deck> decks = new ArrayList<>();
        try {
            Field decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            decksField.set(game, decks);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        // Reflect player field set and reset winner field to -1
        try {
            Field winnerField = Player.class.getDeclaredField("winner");
            winnerField.setAccessible(true);
            winnerField.set(null, -1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }
    }

    @AfterEach
    public void tearDown() {
        game = null;
    }

    // Writes each string to a new line in a .txt file called "filename".txt
    // Overwrites the file each time it's called
    public void packFileCreator(ArrayList<String> input, String filename) {
        try {
            // Open the file in overwrite mode (default behavior)
            PrintWriter printWriter = new PrintWriter(filename);
            for (String line : input) {
                printWriter.println(line);
            }
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test void testGetInstance() {
        CardGame game1 = CardGame.getInstance();
        CardGame game2 = null;
        assertNotEquals(game1, game2, "Game objects are not the same");
        game2 = CardGame.getInstance();
        assertEquals(game1, game2, "Game objects are the same");
    }
    

    @Test
    public void testDeleteOutputFiles() {
        File file = new File("output.txt");
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(file.exists());

        // Reflect deleteOutputFiles method
        Method method = null;
        try {
            method = CardGame.class.getDeclaredMethod("deleteOutputFiles", String.class);
            method.setAccessible(true);
            method.invoke(game, "output.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        assertFalse(file.exists());
    }
    
    @Test
    public void testGetNumberOfPlayers() {
        // Simulated inputs (invalid, invalid, invalid, invalid, valid)
        // -5: Out of bounds (too low)
        // 0: Edge case out of bounds (boundary test, just below valid range)
        // ABC: Non-integer input
        // 1: Edge case in bounds (minimum valid input)
        String simulatedInput = "-5\n0\nABC\n1\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Reflect the method
        Method method = null;
        try {
            method = CardGame.class.getDeclaredMethod("getNumberOfPlayers");
            method.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Invoke the method
        int result = 0;
        try {
            result = (int) method.invoke(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert correct behavior
        assertEquals(1, result); // Should return 2 after invalid inputs

        // Check output for correctness (optional)
        String output = outputStream.toString();
        assert(output.contains("Please enter the number of players:"));
        assert(output.contains("Invalid input. Please enter a positive integer."));
        assert(output.contains("Number not greater than or equal to 1"));

        // Reset System.in and System.out
        System.setIn(System.in);
        System.setOut(System.out);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreatePlayers(){
        // Expected players
        ArrayList<Player> expectedPlayers = new ArrayList<>();
        expectedPlayers.add(new Player(1, game));
        expectedPlayers.add(new Player(2, game));

        ArrayList<Integer> expected = new ArrayList<>();
        for (Player player:expectedPlayers) {
            expected.add(player.getPlayerNumber());
        }

        // Reflect createPlayers method
        Method method = null;
        try {
            method = CardGame.class.getDeclaredMethod("createPlayers", int.class);
            method.setAccessible(true);
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reflect players field
        ArrayList<Player> actualPlayers = null;
        try {
            Field playerField = game.getClass().getDeclaredField("players");
            playerField.setAccessible(true);
            actualPlayers = (ArrayList<Player>) playerField.get(game);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }

        ArrayList<Integer> actual = new ArrayList<>();
        for (Player player:actualPlayers) {
            actual.add(player.getPlayerNumber());
        }
        
        // Assert correct behavior
        assertEquals(expected, actual, "Players not created correctly");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetPack() {
        // Tests both getPack and loadPackFromFile methods
        // loadPackFromFile method is only called in getPack so fully tested
        
        // Simulated inputs (invalid, invalid, invalid, invalid, invalid, valid)
        // 1: Invalid input (non-existent file)
        // 2: Invalid input (empty file)
        // 3: Pack not 8n rows (invalid pack)
        // 4: Pack not all integers (invalid pack)
        // 5: Pack not all positive integers (invalid pack)
        // 6: Valid Pack

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Reflect the method
        Method method = null;
        try {
            method = CardGame.class.getDeclaredMethod("getPack", int.class);
            method.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a valid pack
        ArrayList<String> pack = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            pack.add(String.valueOf(i));
        }
        packFileCreator(pack, "validPack.txt");
        
        // Invoke the method separately for each input
        // packFileCreator helper function used

        // 1: Invalid input (non-existent file)
        String simulatedInput = "nonExistentPack.txt\nvalidPack.txt\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        try {
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String output = outputStream.toString();
        assert(output.contains("Please enter the location of the pack to load: "));
        assert(output.contains("File not found."));
        
        // 2: Invalid input (empty file)
        packFileCreator(new ArrayList<>(), "pack.txt");
        simulatedInput = "pack.txt\nvalidPack.txt\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        try {
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        output = outputStream.toString();
        assert(output.contains("The pack given is not of size 8n rows, where n is the number of players."));

        // 3: Pack not 8n rows (invalid pack)
        ArrayList<String> pack3 = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            pack3.add(String.valueOf(i));
        }
        packFileCreator(pack3, "pack.txt");
        simulatedInput = "pack.txt\nvalidPack.txt\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        try {
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        output = outputStream.toString();
        assert(output.contains("The pack given is not of size 8n rows, where n is the number of players."));

        // 4: Pack not all integers (invalid pack)
        ArrayList<String> pack4 = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            pack4.add(String.valueOf(i));
        }
        pack4.add("A");
        packFileCreator(pack4, "pack.txt");
        simulatedInput = "pack.txt\nvalidPack.txt\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        try {
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        output = outputStream.toString();
        assert(output.contains("The pack given contains a non-integer element: A"));

        // 5: Pack not all positive integers (invalid pack)
        ArrayList<String> pack5 = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            pack5.add(String.valueOf(i));
        }
        pack5.add("-1");
        packFileCreator(pack5, "pack.txt");
        simulatedInput = "pack.txt\nvalidPack.txt\n";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        try {
            method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        output = outputStream.toString();
        assert(output.contains("The pack given contains negative elements."));

        // 6: Valid Pack
        ArrayList<Integer> expected = new ArrayList<>();
        ArrayList<String> pack6 = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            pack6.add(String.valueOf(i));
            expected.add(i);
        }
        packFileCreator(pack6, "pack.txt");
        simulatedInput = "pack.txt";
        inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        ArrayList<Integer> actual = new ArrayList<>();
        try {
            actual = (ArrayList<Integer>) method.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, actual, "Pack not loaded correctly");

        // Delete the pack files ("pack.txt" and "validPack.txt")
        File file = new File("pack.txt");
        file.delete();
        file = new File("validPack.txt");
        file.delete();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDistributePack() {
        // Create a valid pack
        ArrayList<Integer> pack = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            pack.add(i);
        }
        
        // Set up expected hands and decks
        ArrayList<Integer> player1Expected = new ArrayList<>();
        ArrayList<Integer> player2Expected = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            player1Expected.add(i);
            player2Expected.add(i + 1);
            i++; // To double increment i
        }
        ArrayList<Integer> deck1Expected = new ArrayList<>();
        ArrayList<Integer> deck2Expected = new ArrayList<>();
        for (int i = 9; i < 16; i++) {
            deck1Expected.add(i);
            deck2Expected.add(i + 1);
            i++; // To double increment i
        }

        // Reflect createPlayers method to create players in game object
        Method createPlayers = null; 
        try {
            createPlayers = game.getClass().getDeclaredMethod("createPlayers", int.class);
            createPlayers.setAccessible(true);
            createPlayers.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reflect distributePack method to distribute pack to players and decks
        Method distributePackMethod = null;
        try {
            distributePackMethod = game.getClass().getDeclaredMethod("distributePack", ArrayList.class);
            distributePackMethod.setAccessible(true);
            distributePackMethod.invoke(game, pack);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reflect players field from game object
        Field playersField = null;
        ArrayList<Player> players = null;
        try {
            playersField = game.getClass().getDeclaredField("players");
            playersField.setAccessible(true);
            players = (ArrayList<Player>) playersField.get(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reflect hand field from player objects and compare with expected
        Field handField = null;
        ArrayList<Card> hand = null;
        try {
            handField = players.get(0).getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            hand = (ArrayList<Card>) handField.get(players.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }
        ArrayList<Integer> actual = new ArrayList<>();
        for (Card card:hand) {
            actual.add(card.getValue());
        }
        assertEquals(player1Expected, actual, "Expected player 1 hand not equal to actual player 1 hand");

        // Reflect hand field from player objects and compare with expected
        handField = null;
        hand = null;
        try {
            handField = players.get(1).getClass().getDeclaredField("hand");
            handField.setAccessible(true);
            hand = (ArrayList<Card>) handField.get(players.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }
        actual = new ArrayList<>();
        for (Card card:hand) {
            actual.add(card.getValue());
        }
        assertEquals(player2Expected, actual, "Expected player 2 hand not equal to actual player 2 hand");
        
        // Reflect decks field from game object
        Field decksField = null;
        ArrayList<Deck> decks = null;
        try {
            decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            decks = (ArrayList<Deck>) decksField.get(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Reflect deck field from deck objects and compare with expected
        Field deckField = null;
        ArrayList<Card> cards = null;
        try {
            deckField = decks.get(0).getClass().getDeclaredField("deck");
            deckField.setAccessible(true);
            cards = (ArrayList<Card>) deckField.get(decks.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }
        actual = new ArrayList<>();
        for (Card card:cards) {
            actual.add(card.getValue());
        }
        assertEquals(deck1Expected, actual, "Expected deck 1 cards not equal to actual deck 1 cards");

        // Reflect deck field from deck objects and compare with expected
        deckField = null;
        cards = null;
        try {
            deckField = decks.get(1).getClass().getDeclaredField("deck");
            deckField.setAccessible(true);
            cards = (ArrayList<Card>) deckField.get(decks.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error reflecting hand field");
        }
        actual = new ArrayList<>();
        for (Card card:cards) {
            actual.add(card.getValue());
        }
        assertEquals(deck2Expected, actual, "Expected deck 2 cards not equal to actual deck 2 cards");
    }

    @Test
    public void testStartGame() {
        // Create a valid pack for two players
        ArrayList<Integer> packP1Wins = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5));
        ArrayList<Integer> packP2Wins = new ArrayList<>(Arrays.asList(1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5));

        // ----- Player 1 wins -----
        
        // Reflect startGame method to start the game
        Method startGameMethod = null;
        try {
            startGameMethod = game.getClass().getDeclaredMethod("startGame", int.class, ArrayList.class);
            startGameMethod.setAccessible(true);
            startGameMethod.invoke(game, 2, packP1Wins);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check output for correctness
        String expected = "player 1 wins";
        File file = new File("player1_output.txt");
        assertTrue(file.exists());
        // Read the file and check the contents
        try {
            BufferedReader br = new BufferedReader(new FileReader("player1_output.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.contains(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clean up generated output.txt files
        file = new File("player1_output.txt");
        file.delete();
        file = new File("player2_output.txt");
        file.delete();
        file = new File("deck1_output.txt");
        file.delete();
        file = new File("deck2_output.txt");
        file.delete();

        // Reset the game object
        setUp();

        // ----- Player 2 wins -----
        
        // Reflect startGame method to start the game
        startGameMethod = null;
        try {
            startGameMethod = game.getClass().getDeclaredMethod("startGame", int.class, ArrayList.class);
            startGameMethod.setAccessible(true);
            startGameMethod.invoke(game, 2, packP2Wins);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check output for correctness
        expected = "player 2 wins";
        file = new File("player2_output.txt");
        assertTrue(file.exists());
        // Read the file and check the contents
        try {
            BufferedReader br = new BufferedReader(new FileReader("player2_output.txt"));
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.contains(expected)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clean up generated output.txt files
        file = new File("player1_output.txt");
        file.delete();
        file = new File("player2_output.txt");
        file.delete();
        file = new File("deck1_output.txt");
        file.delete();
        file = new File("deck2_output.txt");
        file.delete();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDrawCard() {
        // Create a valid pack for two players
        ArrayList<Integer> pack = new ArrayList<>(Arrays.asList( 1, 1, 1, 2, 2, 2, 3, 4));

        // Reflect createPlayers method to create 1 player in game object
        Method createPlayers = null; 
        try {
            createPlayers = game.getClass().getDeclaredMethod("createPlayers", int.class);
            createPlayers.setAccessible(true);
            createPlayers.invoke(game, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reflect distributePack method to distribute pack to players and decks
        Method distributePackMethod = null;
        try {
            distributePackMethod = game.getClass().getDeclaredMethod("distributePack", ArrayList.class);
            distributePackMethod.setAccessible(true);
            distributePackMethod.invoke(game, pack);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert contents of player 1 hand and deck 1 before draw
        Player player = null;
        Deck deck = null;
        try {
            Field playersField = game.getClass().getDeclaredField("players");
            playersField.setAccessible(true);
            ArrayList<Player> players = (ArrayList<Player>) playersField.get(game);
            player = players.get(0);
            Field decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            ArrayList<Deck> decks = (ArrayList<Deck>) decksField.get(game);
            deck = decks.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Field playerHand = null;
        Field deckCards = null;
        ArrayList<Card> hand = null;
        ArrayList<Card> cards = null;
        try {
            playerHand = player.getClass().getDeclaredField("hand");
            playerHand.setAccessible(true);
            hand = (ArrayList<Card>) playerHand.get(player);
            deckCards = deck.getClass().getDeclaredField("deck");
            deckCards.setAccessible(true);
            cards = (ArrayList<Card>) deckCards.get(deck);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Integer> expectedHand = new ArrayList<>(Arrays.asList(1, 1, 1, 2));
        ArrayList<Integer> expectedDeck = new ArrayList<>(Arrays.asList(2, 2, 3, 4));

        ArrayList<Integer> actualHand = new ArrayList<>();
        ArrayList<Integer> actualDeck = new ArrayList<>();
        

        for (Card card:hand) {
            actualHand.add((card.getValue()));
        }
        assertEquals(expectedHand, actualHand, "Expected player 1 hand not equal to actual player 1 hand");

        for (Card card:cards) {
            actualDeck.add((card.getValue()));
        }
        assertEquals(expectedDeck, actualDeck, "Expected deck 1 cards not equal to actual deck 1 cards");

        // Reflect drawCard method to draw a card for player 1
        Method drawCardMethod = null;
        try {
            drawCardMethod = game.getClass().getDeclaredMethod("drawCard", int.class);
            drawCardMethod.setAccessible(true);
            Card drawnCard = (Card) drawCardMethod.invoke(game, 1);
            assertEquals(2, drawnCard.getValue(), "Expected drawn card value does not equal actual drawn card value");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert contents of player 1 hand and deck 1 after draw
        
        expectedHand = new ArrayList<>(Arrays.asList(1, 1, 1, 2));
        expectedDeck = new ArrayList<>(Arrays.asList(2, 3, 4));

        actualHand = new ArrayList<>();
        actualDeck = new ArrayList<>();

        for (Card card:hand) {
            actualHand.add((card.getValue()));
        }
        assertEquals(expectedHand, actualHand, "Expected player 1 hand equal to actual player 1 hand");

        for (Card card:cards) {
            actualDeck.add((card.getValue()));
        }
        assertEquals(expectedDeck, actualDeck, "Expected deck 1 cards not equal to actual deck 1 cards");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDiscardCard() {
        // Reflect createPlayers method to create 1 player in game object
        Method createPlayers = null; 
        try {
            createPlayers = game.getClass().getDeclaredMethod("createPlayers", int.class);
            createPlayers.setAccessible(true);
            createPlayers.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Field decksField = null;
        ArrayList<Deck> decks = new ArrayList<>();
        decks.add(new Deck(1));
        decks.add(new Deck(2));
        try {
            decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            decksField.set(game, decks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Method discardCardMethod = null;
        try {
            discardCardMethod = game.getClass().getDeclaredMethod("discardCard", int.class, Card.class);
            discardCardMethod.setAccessible(true);
            discardCardMethod.invoke(game, 1, new Card(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Deck deck2 = null;
        try {
            decksField = game.getClass().getDeclaredField("decks");
            decksField.setAccessible(true);
            deck2 = ((ArrayList<Deck>) decksField.get(game)).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        int deck2Card = -1;
        Field deckField = null;
        try {
            deckField = deck2.getClass().getDeclaredField("deck");
            deckField.setAccessible(true);
            deck2Card = ((ArrayList<Card>) deckField.get(decks.get(1))).get(0).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(3, deck2Card, "Expected discarded card value not equal to actual discarded card value");
    }

    @Test
    public void testNumberOfPlayers() {
        assertEquals(0, game.numberOfPlayers());
        Method createPlayers = null;
        try {
            createPlayers = game.getClass().getDeclaredMethod("createPlayers", int.class);
            createPlayers.setAccessible(true);
            createPlayers.invoke(game, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2, game.numberOfPlayers());
    }
}