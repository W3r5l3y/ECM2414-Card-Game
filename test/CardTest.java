import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    
    private Card card;

    @Test
    public void testGetValue() {
        card = new Card(5);
        assertEquals(5, card.getValue(), "Card value should be 5");

        card = new Card(10);
        assertEquals(10, card.getValue(), "Card value should be 10");

        card = new Card(1);
        assertEquals(1, card.getValue(), "Card value should be 1");
    }
}
