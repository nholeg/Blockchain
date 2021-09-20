package blockchain;
import java.util.Date;


public class Blockchain {
    static Block block;
    Blockchain(){
        long timestamp = new Date().getTime();
        String hash = "1" + timestamp + "0";
        block = new Block(1, timestamp, "0", hash);
    }

    static Block createBlock() {
        long timestamp = new Date().getTime();
        String hash = "" + (block.getId() + 1) + timestamp + block.getId();
        block = new Block(block.getId() + 1, timestamp, block.getHash(), hash);
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
