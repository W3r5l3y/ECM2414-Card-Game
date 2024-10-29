import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private static int numOfPlayers;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Deck> decks = new ArrayList<>();

    public static void main(String[] args) {
        
        Scanner console = new Scanner(System.in);  // Create a Scanner object

        // Ask for number of players
        System.out.println("Enter number of players: ");
        numOfPlayers = Integer.parseInt(console.nextLine());  // Read user input
        System.out.println("Number of players is: " + numOfPlayers);
        console.close();

        // Ask for deck input

        // Start Game

    }
}