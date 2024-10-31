package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class CardGameTest {

    private CardGame game;

    @BeforeEach
    public void setUp() {
        game = new CardGame();
    }

    @Test
    public void testCreatePlayers() {
        game.createPlayers(3);
        ArrayList<Player> players = game.getPlayers();
        assertEquals(3, players.size());
        assertEquals("Player 1", players.get(0).getName());
        assertEquals("Player 2", players.get(1).getName());
        assertEquals("Player 3", players.get(2).getName());
    }

    @Test
    public void testInitialisePlayers() {
        // Simulate user input
        String input = "3\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        game.initialisePlayers();
        ArrayList<Player> players = game.getPlayers();
        assertEquals(3, players.size());
    }
}