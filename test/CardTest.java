import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    
    private Card card;

    @Test
    public void testGetValue() {
        card = new Card(5);
        assertEquals(5, card.getValue(), "Card value should be 5");
    }
}
