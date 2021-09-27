package blockchain;

import java.util.Arrays;

public class BlockWithProofValidator implements BlockValidator {
    private final HashGenerator hashGenerator;
    private int countOfZeros;

    public BlockWithProofValidator(int countOfZeros) {
        this.countOfZeros = countOfZeros;
        this.hashGenerator = new HashGenerator();
    }

    @Override
    public boolean isBlockHashProven(String hash) {
        String zeros = getZeroString(countOfZeros);
        return (countOfZeros > 0 && hash.startsWith(zeros))
                || (countOfZeros == 0 && !hash.startsWith("0"));
    }

    @Override
    public boolean isBlockHashProven(Block block) {
        return isBlockHashProven(block.getHash());
    }


    @Override
    public boolean isBlockHashValid(Block block) {
        return hashGenerator.createHash(block.toString()).equals(block.getHash());
    }

    @Override
    public void setStartingZeroCount(int countOfZeros) {
        this.countOfZeros = countOfZeros;
    }

    @Override
    public int getStartingZeroCount() {
        return this.countOfZeros;
    }

    public int getCountOfZeros() {
        return countOfZeros;
    }

    public void setCountOfZeros(int countOfZeros) {
        this.countOfZeros = countOfZeros;
    }

    private String getZeroString(int countOfZeros) {
        char[] zeroArr = new char[countOfZeros];
        Arrays.fill(zeroArr, '0');
        return new String(zeroArr);
    }

}