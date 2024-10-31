import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CardGame {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    public static void main(String[] args) {
        CardGame game = new CardGame(); // Create game object
        game.initialisePlayers();
        game.initialisePack();
        game.startGame();
    }

    protected void initialisePlayers() {
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
        createPlayers(intInput);
    }

    protected void createPlayers(int numOfPlayers) {
        for (int i = 1; i < numOfPlayers + 1; i++) {
            System.out.println("Creating Player: " + i);
            Player player = new Player(i);
            players.add(player);
        }
    }

    protected void initialisePack() {
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

    private void startGame() {
        for (Player player : players) {
            player.printHand();
        }
        for (Deck deck : decks) {
            deck.printDeck();
        }
        System.out.println("Game starts!");
    }
}