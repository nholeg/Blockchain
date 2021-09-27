package blockchain;

public class IdGenerator {
    private int idCounter = 0;

    public int getId() {
        return ++idCounter;
    }
}