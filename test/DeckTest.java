import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class DeckTest {
    
    private Deck deck;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;

    /**
     * Delete output files matching a specific regex pattern.
     */
    private static void deleteOutputFiles(String filename) {
        File[] files = new File(".").listFiles((dir, name) -> name.matches(filename));
        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    //System.out.println("Deleted: " + file.getName()); //TODO REMOVE
                } else {
                    //System.out.println("Failed to delete: " + file.getName()); //TODO REMOVE
                }
            }
        } else {
            System.out.println("No matching files found.");
        }
    }

    @BeforeEach
    public void setUp() {
        deleteOutputFiles("deck\\d+_output\\.txt");
        deck = new Deck(1); // Create empty deck
        card1 = new Card(2); // Initialise cards
        card2 = new Card(5);
        card3 = new Card(1);
        card4 = new Card(7);
    }

    @AfterEach
    public void tearDown() {
        deleteOutputFiles("deck\\d+_output\\.txt");
    }

    @Test
    public void testAddToDeck() {
        deck.addToDeck(card1);
        assertFalse(deck.isEmpty(), "Deck should not be empty after adding a card");
    }

    @Test
    public void testDrawFromTopDeck() {
        deck.addToDeck(card1);
        deck.addToDeck(card2);
        deck.addToDeck(card3);
        deck.addToDeck(card4);
        
        Card drawnCard = deck.drawFromTopDeck();
        assertEquals(card1, drawnCard, "The drawn card should be the first card added");
        
        drawnCard = deck.drawFromTopDeck();
        assertEquals(card2, drawnCard, "The drawn card should be the second card added");

        drawnCard = deck.drawFromTopDeck();
        assertEquals(card3, drawnCard, "The drawn card should be the third card added");

        drawnCard = deck.drawFromTopDeck();
        assertEquals(card4, drawnCard, "The drawn card should be the fourth card added");
        
        assertTrue(deck.isEmpty(), "Deck should be empty after drawing four cards");
    }

    @Test
    public void testIsEmpty() {
        assertTrue(deck.isEmpty(), "New deck should be empty");
        deck.addToDeck(card1);
        assertFalse(deck.isEmpty(), "Deck should now not be empty after adding a card");
    }

    @Test
    public void testGetDeckNumber() {
        assertEquals(1, deck.getDeckNumber(), "Deck number should be 1");
        Deck deck2 = new Deck(13); // Create new deck with deckNumber 13
        assertEquals(13, deck2.getDeckNumber(), "Deck number should be 13");
    }

    @Test
    public void testLogDeck() {
        deck.addToDeck(card1);
        deck.addToDeck(card2);
        deck.addToDeck(card3);
        deck.addToDeck(card4);
        deck.logDeck();
        File file = new File("deck1_output.txt");
        assertTrue(file.exists(), "Log file should be created");

        // Check file contents
        String expected = "deck1 contents: 2 5 1 7 ";
        String actual = "";
        try (BufferedReader br = new BufferedReader(new FileReader("deck1_output.txt"))) {
            actual = br.readLine();
        } catch (Exception e) {
            fail("Error finding/reading file.");
        }
        assertEquals(expected, actual, "File contents should match expected");       

        file.delete(); // Clean up after test
    }
}
