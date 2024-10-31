import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Integer> pack = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    public static void main(String[] args) {
        CardGame game = new CardGame(); // Create game object
        game.initialisePlayers();
        game.initialisePack();
        game.startGame();
    }

    protected void initialisePlayers() {
        Scanner console = new Scanner(System.in);  // Create a Scanner object
        int intInput;

        // Ask for number of players
        while (true) {
            System.out.println("Enter number of players: ");
            String input = console.nextLine();
            
            try {
                intInput = Integer.parseInt(input);
                if (intInput < 1) {throw new Exception("Number not greater than or equal to 1");}
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            } catch (Exception e) {
                System.out.println("Invalid input. " + e.getMessage());
            }
        }
        console.close();
        createPlayers(intInput);
    }

    protected void createPlayers(int numOfPlayers) {
            for (int i = 1; i < numOfPlayers + 1; i++) {
                System.out.println("Creating Player: " + i);
                Player player = new Player(i);
                players.add(player);
            }
        }

    private void initialisePack() {
        Scanner console = new Scanner(System.in);  // Create a Scanner object
        while (true) {
            System.out.println("Enter the name of the pack file: ");
            String fileName = console.nextLine();
            try {
                loadPackFromFile(fileName);
                break;
            } catch (Exception e) {
                System.out.println("Error finding/reading file. Please try again.");
            }
        }
        console.close();
        System.out.println("Pack loaded successfully!");
        System.out.println(pack);
    }

    private void loadPackFromFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int number = Integer.parseInt(line);
                pack.add(number);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    private void startGame() {
        System.out.println("Game starts!");
    }
}