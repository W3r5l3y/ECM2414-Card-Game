import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private ArrayList<Player> players = new ArrayList<>();
    //private ArrayList<Deck> decks = new ArrayList<>();

    public static void main(String[] args) {
        CardGame game = new CardGame(); // Create game object
        game.initialisePlayers();
        game.initialisePack();
        game.startGame();
    }

    private void createPlayers(int numOfPlayers) {
        for (int i = 1; i < numOfPlayers + 1; i++) {
            System.out.println("Creating Player: " + i);
            Player player = new Player(i);
            players.add(player);
        }
    }

    private void initialisePlayers() {
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

    private void initialisePack() {
        //TODO
    }

    private void startGame() {
        System.out.println("Game starts!");
    }
}