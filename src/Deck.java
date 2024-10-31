import java.util.ArrayList;

public class Deck {
    private int deckNumber;
    private ArrayList<Card> deck = new ArrayList<>();

    public Deck(int deckNumber) {
        this.deckNumber = deckNumber;
    }

    public int getDeckNumber() {
        return deckNumber;
    }

    public void addToDeck(Card card) {
        deck.add(card);
    }

    public void printDeck() { // TODO Remove testing method
        for (Card card : deck) {
            System.out.println("Deck " + deckNumber + ": " + card.getValue());
        }
    }
}
