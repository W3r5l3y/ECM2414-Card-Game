import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


class CardGameTest {
    
    private CardGame game;
    
    @BeforeEach
    void setUp() {
        game = new CardGame();
    }

    @Test
    void testCreatePlayers() {
        // Redirect system input to simulate user input for initialisePlayers
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        game.initialisePlayers();
    }

    @Test
    void testStartGameOutput() {
        // Test that the startGame method produces the expected output
        // You may consider using a PrintStream to capture System.out if you want to verify console output
    }

    // Additional tests could be added once initialisePack and more game functionality are implemented.
}
