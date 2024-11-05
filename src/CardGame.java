import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CardGame {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Thread> playerThreads = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    public static void main(String[] args) {
        deletePlayerOutputFiles();

        CardGame game = new CardGame(); // Create game object
        game.initialisePlayers(game);
        game.initialisePack();

        int winnerNumber = game.winOnStart();
        if (winnerNumber != -1) {
            System.out.println("player " + winnerNumber + " wins");
        } else {
            game.startGame();
        }
    }

    public static void deletePlayerOutputFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.matches("player\\d+_output\\.txt"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName());
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }
        } else {
            System.out.println("No files found.");
        }
    }

    private void initialisePlayers(CardGame game) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int intInput;

        // Ask for number of players
        while (true) {
            System.out.println("Enter number of players: ");
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

    private void createPlayers(int numOfPlayers, CardGame game) {
        for (int i = 1; i <= numOfPlayers; i++) {
            System.out.println("Creating Player: " + i);
            Player player = new Player(i, game);
            players.add(player);
        }
    }

    private void initialisePack() {
        ArrayList<Integer> pack = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter the name of the pack file: ");
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

        splitPackIntoDecks(pack);
    }

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

    private void splitPackIntoDecks(ArrayList<Integer> pack) {
        int packCounter = 0;

        for (Player player : players) {
            for (int i = 0; i < 4; i++) { // Fill players
                player.addToHand(new Card(pack.get(packCounter + i)));
            }
            packCounter += 4;
        }
        for (int i = 0; i < players.size(); i++) { // Make blank decks
            Deck deck = new Deck(i + 1);
            decks.add(deck);
        }
        for (Deck deck : decks) { // Fill decks
            for (int i = 0; i < 4; i++) {
                deck.addToDeck(new Card(pack.get(packCounter + i)));
            }
            packCounter += 4;
        }
    }

    private int winOnStart() {
        // Check all players hands for a winning hand on start
        for (Player player : players) {
            if (player.checkWin()) {
                return player.getPlayerNumber();
            }
        }
        return -1;
    }

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
        System.out.println("player " + Player.getWinner() + " wins");
    }

    public synchronized Card drawCard(int playerNumber) {
        // Return top card from deck i , where i is playerNumber
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

    public synchronized void discardCard(int playerNumber, Card card) {
        // Place card at bottom of deck i, where i is the next player on from playerNumber
        int nextPlayerNumber = (playerNumber % players.size()) + 1;
        Deck deck = decks.get(nextPlayerNumber - 1);
        deck.addToDeck(card);
        // Notify all threads that a card has been discarded
        notifyAll();
    }
}