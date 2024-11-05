import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * The CardGame class is a singleton class that represents the card game
 * It contains the main method to start the game, and methods to initialise the players and pack of cards
 * It also contains methods to distribute the pack of cards to the players and decks, and to start the game
 * The CardGame class also contains methods to draw and discard cards from the decks, which are synchronised and used by the Player class
 */
public class CardGame {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Thread> playerThreads = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    /**
     * Singleton constructor for the CardGame class
     */
    private CardGame() {}

    /**
     * Singleton helper class to create the CardGame instance
     */
    private static class CardGameHelper {
        private static CardGame instance = new CardGame();
    }

    /**
     * Get the singleton instance of the CardGame
     */
    public static CardGame getInstance() {
        return CardGameHelper.instance;
    }


    /**
     * Main method to start the game
     */
    public static void main(String[] args) {
        // Delete any existing output files
        deletePlayerOutputFiles();
        deleteDeckOutputFiles();

        CardGame game = CardGame.getInstance(); // Create singleton game object instance
        game.initialisePlayers(game);
        game.initialisePack();
        game.startGame();
    }


    /**
     * Delete all player output files in the current directory
     */
    public static void deletePlayerOutputFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.matches("player\\d+_output\\.txt"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName()); // TODO: Remove this line after testing
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }
        } else {
            System.out.println("No files found.");
        }
    }


    /**
     * Delete all deck output files in the current directory
     */
    public static void deleteDeckOutputFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.matches("deck\\d+_output\\.txt"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName()); // TODO: Remove this line after testing
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }
        } else {
            System.out.println("No files found.");
        }
    }


    /**
     * Get the number of players from a terminal input and create the player objects
     * @param game The game object
     */
    private void initialisePlayers(CardGame game) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int intInput;

        // Ask for number of players
        while (true) {
            System.out.println("Please enter the number of players: ");
            try {
                String input = reader.readLine();
                intInput = Integer.parseInt(input);
                if (intInput < 1) {
                    throw new Exception("Number not greater than or equal to 1");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            } catch (Exception e) {
                System.out.println("Invalid input. " + e.getMessage() + "");
            }
        }
        createPlayers(intInput, game);
    }


    /**
     * Create the player objects and add them to the players list
     * @param numOfPlayers The number of players to create
     * @param game The game object
     */
    private void createPlayers(int numOfPlayers, CardGame game) {
        for (int i = 1; i <= numOfPlayers; i++) {
            System.out.println("Creating Player: " + i);
            Player player = new Player(i, game);
            players.add(player);
        }
    }


    /**
     * Get the pack of cards from a terminal input and distribute them to the players and decks
     * Card pack file should contain 8n rows, where n is the number of players
     * Cards are distributed in a round robin fashion, first to the players, then to the decks
     */
    private void initialisePack() {
        ArrayList<Integer> pack = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Please enter location of pack to load: ");
            pack = new ArrayList<>(); // Wipe pack
            try {
                String fileName = reader.readLine();
                loadPackFromFile(pack, fileName);
                if (pack.size() != 8 * players.size()) {
                    throw new Exception("The pack given is not of size 8n rows, where n is the number of players.");
                }
                break;
            } catch (IOException e) {
                System.out.println("Error finding/reading file. Please try again.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Pack loaded successfully!");
        System.out.println(pack);

        distributePack(pack);
    }


    /**
     * Load the pack of cards from a file
     * @param pack The pack to load the cards into
     * @param fileName The name of the file to load the pack from
     * @throws Exception If the pack contains negative elements, a non-integer element, or is not of size 8n rows
     */
    private void loadPackFromFile(ArrayList<Integer> pack, String fileName) throws Exception {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                
                int number = Integer.parseInt(line);
                if (number < 0) {
                    throw new Exception("The pack given contains negative elements.");
                }
                pack.add(number);
            }
        } catch (IOException e) {
            throw new IOException("Error finding/reading file.");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The pack given contains a non integer element: " + line + "");
        }
    }


    /**
     * Distribute the pack of cards to the players and decks
     * @param pack The pack of cards to distribute
     */
    private void distributePack(ArrayList<Integer> pack) {
        int packCounter = 0;

        for (int i = 0; i < 4; i++) {
            for (Player player : players) { // Fill players in round robin fashion
                player.addToHand(new Card(pack.get(packCounter)));
                packCounter++;
            }
        }
        for (int i = 0; i < players.size(); i++) { // Make blank decks
            Deck deck = new Deck(i + 1);
            decks.add(deck);
        }
        for (int i = 0; i < 4; i++) { // Fill decks in round robin fashion
            for (Deck deck : decks) {
                deck.addToDeck(new Card(pack.get(packCounter)));
                packCounter++;
            }
        }
    }
    

    /**
     * Start the player threads and wait for them to finish
     * Print the winner and log the end-of-game deck contents
     */
    private void startGame() {
        for (Player player : players) {
            Thread playerThread = new Thread(player);
            playerThreads.add(playerThread);
            playerThread.start();
        }
        for (Thread playerThread : playerThreads) {
            try {
                playerThread.join();
            } catch (InterruptedException e) {}
        }

        // Print the winner
        System.out.println("player " + Player.getWinner() + " wins");

        // Log the end-of-game deck contents
        for (Deck deck : decks) {
            deck.logDeck();
        }
    }


    /**
     * Draw a card from the deck i, where i is the player number
     * @param playerNumber The player number to draw from
     * @return The card drawn
     */
    public synchronized Card drawCard(int playerNumber) {
        Deck deck = decks.get(playerNumber - 1);
        // Wait until deck is not empty
        while (deck.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Card card = deck.drawFromTopDeck();
        return card;
    }


    /**
     * Discard a card to the deck i, where i is the next player on from playerNumber
     * @param playerNumber The player number to discard to
     * @param card The card to discard
     */
    public synchronized void discardCard(int playerNumber, Card card) {
        // Calculate next player number given the current player number
        int nextPlayerNumber = (playerNumber % players.size()) + 1;
        Deck deck = decks.get(nextPlayerNumber - 1);
        deck.addToDeck(card);
        // Notify all threads that a card has been discarded
        notifyAll();
    }


    /**
     * Get the number of players in the game
     * @return The number of players
     */
    public int numberOfPlayers() {
        return players.size();
    }
}