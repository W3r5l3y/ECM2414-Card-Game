import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Deck class represents a deck of cards and the methods to manipulate the deck.
 * @author Sam McMullen
 * @author James Worley
 */
public class Deck {
    /**The deck number.*/
    private int deckNumber;
    /**The deck of cards.*/
    private volatile ArrayList<Card> deck = new ArrayList<>();


    /**
     * Constructor for the Deck class.
     * @param deckNumber
     */
    public Deck(int deckNumber) {
        this.deckNumber = deckNumber;
    }


    /**
     * Getter for the deck number.
     * @return the deck number
     */
    public int getDeckNumber() {
        return deckNumber;
    }


    /**
     * Adds a card to the deck.
     * @param card the card to add
     */
    public synchronized void addToDeck(Card card) {
        deck.add(card);
    }


    /**
     * Removes a card from the deck and returns it.
     * @return the card removed from the deck
     */
    public synchronized Card drawFromTopDeck() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }


    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }


    /**
     * Logs the deck to a file.
     * The file name is deck{deckNumber}_output.txt.
     */
    public void logDeck() {
        String fileName = "deck" + deckNumber + "_output.txt";
        StringBuilder deckValues = new StringBuilder();
        for (Card card : deck) {
            deckValues.append(card.getValue() + " ");
        }
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            writer.write("deck" + deckNumber + " contents: " + deckValues + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
