/**
 * Card class represents a card in the game.
 * 
 * @author Sam McMullen
 * @author James Worley
 */
public class Card {
    private int value;

    /**
     * Constructor for Card class
     * @param value
     */
    public Card(int value) {
        this.value = value;
    }

    /**
     * Getter for value
     * @return value
     */
    public int getValue() {
        return value;
    }
}
