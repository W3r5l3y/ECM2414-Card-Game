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

    public synchronized void addToDeck(Card card) {
        deck.add(card);
    }

    public synchronized Card drawFromTopDeck() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void printDeck() { // TODO Remove testing method
        for (Card card : deck) {
            System.out.println("Deck " + deckNumber + ": " + card.getValue());
        }
    }
}
