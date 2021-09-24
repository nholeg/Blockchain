package blockchain;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage().initializeStorage();
        if (!Validator.validateChain(Blockchain.chainMap)) {
            System.out.println("Blockchain content different than expected.");
        } else {
            var sc = new Scanner(System.in);
            System.out.print("Enter how many zeros the hash must start with:\n");
            String zerosPrefix = "0".repeat(sc.nextInt());
            do {
                Block block = Blockchain.createNewBlock(zerosPrefix, storage.getBlockId());
                Blockchain.addBlockToChain(block);
            } while (Blockchain.chainMap.size() % 5 != 0);
            Blockchain.chainMap.values().stream()
                    .sorted(Comparator.comparingInt(Block::getBlockId).reversed())
                    .limit(5)
                    .sorted(Comparator.comparingInt(Block::getBlockId))
                    .forEach(System.out::println);
            storage.serializeData();
        }
    }
}