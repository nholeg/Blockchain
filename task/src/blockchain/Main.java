package blockchain;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Create keys if they are not exist
        try {
            File file1 = new File(PathsConstants.PUBLIC_KEY);
            File file2 = new File(PathsConstants.PRIVATE_KEY);
            if (!(file1.exists() && file2.exists())) {
                GenerateKeys gk = new GenerateKeys(1024);
                gk.createKeys();
                gk.writeToFile(PathsConstants.PUBLIC_KEY, gk.getPublicKey().getEncoded());
                gk.writeToFile(PathsConstants.PRIVATE_KEY, gk.getPrivateKey().getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println(e.getMessage());
        }

        // Create blockchain
        Blockchain blockchain = Blockchain.getInstance();

        // People' names
        String[] people = new String[] {"Tom", "Sarah", "Nick"};

        // Number of miners
        final int MINERS_NUMBER = 10;

        // Blockchain loading
        ExecutorService executor = Executors.newFixedThreadPool(MINERS_NUMBER + people.length);
        for (int i = 1; i <= MINERS_NUMBER; i++) {
            int number = i;
            executor.submit(() -> {
                try {
                    Miner miner = new Miner(blockchain, number);
                    while (blockchain.continueWorking())
                        miner.createBlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        for (String name : people) {
            executor.submit(() -> {
                try {
                    Person person = new Person(blockchain, name);
                    while (blockchain.continueWorking()) {
                        person.sleep();
                        person.sendMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Stop executor and wait for termination
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}