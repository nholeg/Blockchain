package blockchain;


import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage().initializeStorage();
        Blockchain blockchain =Blockchain.getInstance();
        if (!Validator.validateChain(blockchain.chainMap)) {
            System.out.println("Blockchain content different than expected.");
        } else {
            do {
                for (int i = 0; i < 5; i++) {
                    String zerosPrefix = "0".repeat(blockchain.getN());
                    //making workers
                    Thread worker = new Worker(zerosPrefix, storage.getBlockId());
                    worker.start();
                    try {
                        worker.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (blockchain.chainMap.size() % 5 != 0);

            blockchain.chainMap.values().stream()
                    .sorted(Comparator.comparingInt(Block::getBlockId).reversed())
                    .limit(5)
                    .sorted(Comparator.comparingInt(Block::getBlockId))
                    .forEach(System.out::println);
            storage.serializeData();
        }
    }
}