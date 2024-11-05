import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Deck {
    private int deckNumber;
    private volatile ArrayList<Card> deck = new ArrayList<>();

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
