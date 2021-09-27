package blockchain;
public interface BlockValidator {
    boolean isBlockHashProven(String hash);
    boolean isBlockHashProven(Block block);
    boolean isBlockHashValid(Block block);
    void setStartingZeroCount(int countOfZeros);
    int getStartingZeroCount();
}