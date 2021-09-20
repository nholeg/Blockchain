package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Block> list = new ArrayList<>();
        Blockchain blockchain = new Blockchain();
        Block oldBlock = blockchain.getBlock();
        list.add(oldBlock);
        for (int i = 0; i < 4; i++) {
            Block newBlock = blockchain.createBlock();
            if (blockchain.validate(list.get(i), newBlock)){
                list.add(newBlock);
            } else {
                System.out.println("HASH ERROR");
            }
        }
        for (Block block : list) {
            System.out.println("Block:");
            System.out.println("Id: " + block.getId());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Hash of the previous block:\n" + block.getHashPrev());
            System.out.println("Hash of the block:\n" + block.getHash());
            System.out.println();
        }
        
    }


}
