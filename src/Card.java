public class Card {
    private static int ID = 0;
    private int value;

    public Card(int value) {
        ID = ID++;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
