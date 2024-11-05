import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    private CardGame game;

    @BeforeEach
    public void setUp() {
        game = CardGame.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        CardGame instance1 = CardGame.getInstance();
        CardGame instance2 = CardGame.getInstance();
        assertSame(instance1, instance2, "CardGame instances should be the identical");
    }

    @ParameterizedTest
    @ValueSource(strings = {"3\n", "4\n", "5\n"})
    public void testInitialisePlayers(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Method initialisePlayersMethod = CardGame.class.getDeclaredMethod("initialisePlayers", CardGame.class);
        initialisePlayersMethod.setAccessible(true);
        initialisePlayersMethod.invoke(game, game);

        Field playersField = CardGame.class.getDeclaredField("players");
        playersField.setAccessible(true);
        ArrayList<Player> players = (ArrayList<Player>) playersField.get(game);

        int expectedPlayers = Integer.parseInt(input.trim());
        assertEquals(expectedPlayers, players.size(), "There should be " + expectedPlayers + " players initialized");
    }

    /*
    @Test
    public void testInitialisePack() throws Exception {
        String input = "pack.txt\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Method initialisePackMethod = CardGame.class.getDeclaredMethod("initialisePack");
        initialisePackMethod.setAccessible(true);
        initialisePackMethod.invoke(game);

        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);
        ArrayList<Deck> decks = (ArrayList<Deck>) decksField.get(game);

        assertFalse(decks.isEmpty(), "Decks should be initialized");
    }
    */

    @Test
    public void testDrawCard() throws Exception {
        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);
        ArrayList<Deck> decks = (ArrayList<Deck>) decksField.get(game);

        Deck deck = new Deck(1);
        deck.addToDeck(new Card(5));
        decks.add(deck);

        Method drawCardMethod = CardGame.class.getDeclaredMethod("drawCard", int.class);
        drawCardMethod.setAccessible(true);
        Card card = (Card) drawCardMethod.invoke(game, 1);

        assertNotNull(card, "Card should be drawn from the deck");
        assertEquals(5, card.getValue(), "The drawn card should have the value 5");
    }

    @Test
    public void testDiscardCard() throws Exception {
        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);
        ArrayList<Deck> decks = (ArrayList<Deck>) decksField.get(game);

        Deck deck = new Deck(1);
        decks.add(deck);

        Method discardCardMethod = CardGame.class.getDeclaredMethod("discardCard", int.class, Card.class);
        discardCardMethod.setAccessible(true);
        discardCardMethod.invoke(game, 1, new Card(5));

        assertFalse(deck.isEmpty(), "Deck should have a discarded card");
        assertEquals(5, deck.drawFromTopDeck().getValue(), "The discarded card should have the value 5");
    }
}