package blockchain;

import java.io.Serializable;
import java.util.Random;

public class Block implements Serializable {
    private static final long serialVersionUID = 123456789L;
    private final int blockId;
    private final String prevBlockHash;
    private final long timeStamp;
    private long magicNumber;
    private final String blockHash;
    private long hashingTime;

    public Block(int blockId, String zerosPrefix) {
        this.blockId = blockId;
        this.timeStamp = System.currentTimeMillis();
        this.blockHash = generateBlockHash(zerosPrefix);
        this.prevBlockHash = Blockchain.chainMap.isEmpty() ? "0"
                : Blockchain.chainMap.get(getBlockId() - 1).getBlockHash();
    }

    public int getBlockId() {
        return blockId;
    }

    public String getBlockHash() {
        return blockHash;
    }

    private String generateBlockHash(String zerosPrefix) {
        String blockHash;
        long start = System.currentTimeMillis();
        do {
            magicNumber = new Random().nextInt(100_000_000);
            blockHash = Hashing.applySHA256(String.valueOf(blockId + timeStamp + magicNumber));
        } while (!blockHash.startsWith(zerosPrefix));
        long end = System.currentTimeMillis();
        hashingTime = ((end - start) / 1000) % 60;
        return blockHash;
    }

    @Override
    public String toString() {
        return "\nBlock:\n" +
                "Id: " + blockId + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + prevBlockHash + "\n" +
                "Hash of the block:\n" + blockHash + "\n" +
                "Block was generating for " + hashingTime + " seconds";
    }
}