package blockchain;
import java.util.Date;
import java.util.Random;


public class Blockchain {
    static Block block;
    static int blockNumber = 1;
    Blockchain(String zeroes){
        long timestamp = new Date().getTime();
        Random random = new Random();
        String magic = String.valueOf(random.nextInt(99_999_999));
        String hash = "1" + timestamp + "0" + magic;
        long start = System.currentTimeMillis();
        block = new Block(1, timestamp, "0", hash, magic);
        while (!block.getHash().startsWith(zeroes)) {
            timestamp = new Date().getTime();
            magic = String.valueOf(random.nextInt(99_999_999));
            hash = blockNumber + timestamp + "0" + magic;
            block = new Block(1, timestamp, "0", hash, magic);
        }
        long end = System.currentTimeMillis();
        long interval = (end - start) / 1_000_000_000;
        block.setTime(interval);
    }

    static Block createBlock(String zeroes) {
        long timestamp = new Date().getTime();
        Random random = new Random();
        String magic = String.valueOf(random.nextInt(99_999_999));
        String prevHash = block.getHash();
        String hash = "" + (blockNumber +  1) + timestamp + prevHash + magic;
        long start = System.currentTimeMillis();
        block = new Block(blockNumber + 1, timestamp, prevHash, hash, magic);
        while (!block.getHash().startsWith(zeroes)) {
            timestamp = new Date().getTime();
            magic = String.valueOf(random.nextInt(99_999_999));
            hash = "" + (blockNumber + 1) + timestamp + prevHash + magic;
            block = new Block(blockNumber + 1, timestamp, prevHash, hash, magic);
        }
        long end = System.currentTimeMillis();
        long interval = (end - start) / 1_000_000_000;
        block.setTime(interval);
        blockNumber++;
        return block;
    }

    static boolean validate(Block oldBlock, Block newBlock) {
        if (oldBlock.getHash().equals(newBlock.getHashPrev())) {
            return true;
        } else {
            return false;
        }

    }

    public static Block getBlock() {
        return block;
    }
}
