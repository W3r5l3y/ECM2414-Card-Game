import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class Player implements Runnable {
    private int playerNumber;
    private volatile ArrayList<Card> hand = new ArrayList<>();
    private CardGame game;
    private static volatile int winner = -1;


    /**
     * Constructor for the Player class.
     * Initializes the player number and the game.
     * @param playerNumber
     * @param game
     */
    public Player(int playerNumber, CardGame game) {
        this.playerNumber = playerNumber;
        this.game = game;
    }


    /**
     * Runs the player thread.
     * This method is called when the thread is started.
     * The player thread will run until a winner is declared.
     */
    @Override
    public void run() {
        // Thread logic for player
        logCurrentHand("initial");
        if (checkWin() == true) {
            winner = playerNumber;
        }
        while (winner == -1) {
            drawCard();
            discardCard();
            logCurrentHand("current");

            if (checkWin()) {
                winner = playerNumber;
            }
        }
        // Game over logic
        if (winner == playerNumber) {
            logMessage("player " + playerNumber + " wins");
        } else {
            logMessage("player " + winner + " has informed player " + playerNumber + " that player " + winner + " has won");
        }
        logMessage("player " + playerNumber + " exits");
        logCurrentHand("final");
    }


    /**
     * Returns the player number of the player.
     * @return The player number of the player.
     */
    public int getPlayerNumber() {
        return playerNumber;
    }


    /**
     * Returns the player number of the winning player.
     * @return The player number of the winning player.
     */
    public static int getWinner() {
        return winner;
    }


    /**
     * Adds a card to the player's hand.
     * @param card
     */
    public void addToHand(Card card) {
        hand.add(card);
    }


    /**
     * Adds a card to the player's hand.
     * @param card
     */
    private void removeFromHand(Card card) {
        hand.remove(card);
    }


    /**
     * Logs the current hand of the player to a file.
     *
     * @param handState A string representing the state of the hand (e.g., "initial", "current", "final").
     */
    private void logCurrentHand(String handState) {
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
            e.printStackTrace();
        }
    }


    /**
     * Logs a message to the player's output file.
     *
     * @param message The message to log.
     */
    private void logMessage(String message) {
        String fileName = "player" + playerNumber + "_output.txt";
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            writer.write(message + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Draws a card from the deck and adds it to the player's hand.
     */
    private void drawCard() {
        Card drawnCard = game.drawCard(playerNumber);
        addToHand(drawnCard);
        String message = "player " + playerNumber + " draws a " + drawnCard.getValue() + " from deck " + playerNumber;
        logMessage(message);
    }


    /**
     * Discards a card from the player's hand.
     */
    private void discardCard() {
        // Logic to check which card to discard
        Card discardedCard = null;
        for (Card card : hand) {
            if (card.getValue() != playerNumber) {
                discardedCard = card;
                break;
            }
        }
        
        game.discardCard(playerNumber, discardedCard);
        removeFromHand(discardedCard);
        String message = "player " + playerNumber + " discards a " + discardedCard.getValue() + " to deck " + ((playerNumber % game.numberOfPlayers()) + 1);
        logMessage(message);
    }


    /**
     * Checks if the player has won the game.
     * @return
     */
    private boolean checkWin() {
        int firstValue = hand.get(0).getValue();
        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getValue() != firstValue) {
                return false;
            }
        }
        return true;
    }
}
