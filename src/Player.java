import java.util.ArrayList;

public class Player {
    private int playerNumber;
    private ArrayList<Card> hand = new ArrayList<>();

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public void printHand() { // TODO REMOVE AFTER TESTING
        for (Card card : hand) {
            System.out.println("Player " + playerNumber + ": " + card.getValue());
        }
    }
}
