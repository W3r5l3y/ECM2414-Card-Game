import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;

public class Player implements Runnable {
    private int playerNumber;
    private ArrayList<Card> hand = new ArrayList<>();
    private CardGame game;
    private static volatile int winner = -1;

    public Player(int playerNumber, CardGame game) {
        this.playerNumber = playerNumber;
        this.game = game;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public static int getWinner() {
        return winner;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public void removeFromHand(Card card) {
        hand.remove(card);
    }

    public void printHand() { // TODO REMOVE AFTER TESTING
        for (Card card : hand) {
            System.out.println("Player " + playerNumber + ": " + card.getValue());
        }
    }

    // THREADING LOGIC
    @Override
    public void run() {
        // Thread logic for player
        logCurrentHand("initial");
        while (winner == -1) {
            drawCard();
            discardCard();
            logCurrentHand("current");

            if (checkWin()) {
                winner = playerNumber;
            }
        }
        // Game over logic

    
    }

    /**
     * Logs the current hand of the player to a file.
     *
     * @param handState A string representing the state of the hand (e.g., "initial", "current", "final").
     */
    public void logCurrentHand(String handState) {
        String fileName = "player" + playerNumber + "_output.txt";
        StringBuilder handValues = new StringBuilder();
        for (Card card : hand) {
            handValues.append(card.getValue() + " ");
        }
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            writer.write("player " + playerNumber + " " + handState + " hand: " + handValues + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("CHEESE AND BEANS");
        }
    }

    /**
     * Draws a card from the deck and adds it to the player's hand.
     */
    public void drawCard() {
        Card drawnCard = game.drawCard(playerNumber);
        addToHand(drawnCard);
        // log
    }

    /**
     * Discards a card from the player's hand.
     */
    public void discardCard() {
        // Logic to check which card to discard
        ArrayList<Card> discardList = new ArrayList<>();
        for (Card card : hand) {
            if (card.getValue() != playerNumber) {
                discardList.add(card);
            }
        }
        Random random = new Random();
        Card card = discardList.get(random.nextInt(0, discardList.size()));
        
        game.discardCard(playerNumber, card);
        removeFromHand(card);
        // log 
    }

    public boolean checkWin() {
        int firstValue = hand.get(0).getValue();
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getValue() != firstValue) {
                return false;
            }
        }
        return true;
    }
}
