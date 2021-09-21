package blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter how many zeros the hash must start with:");
        int numberOfZeroes = scanner.nextInt();
        StringBuilder zeroes = new StringBuilder();
        for (int i = 0; i < numberOfZeroes; i++) {
            zeroes.append(0);
        }
        List<Block> list = new ArrayList<>();
        Blockchain blockchain = new Blockchain(zeroes.toString());
        Block oldBlock = blockchain.getBlock();
        list.add(oldBlock);
        for (int i = 0; i < 4; i++) {
            Block newBlock = blockchain.createBlock(zeroes.toString());
            if (blockchain.validate(list.get(i), newBlock)){
                list.add(newBlock);
            } else {
                System.out.println("HASH ERROR");
            }
        }
        for (Block block : list) {
            System.out.println(block.toString());
        }
        
    }


}
