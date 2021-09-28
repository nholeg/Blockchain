package blockchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageGenerator implements Runnable {
    final BufferedReader scanner =  new BufferedReader(new InputStreamReader(System.in));
    final BlockChain blockChain;
    private static volatile boolean isStopGeneration;

    public MessageGenerator(BlockChain chain) {
        blockChain = chain;
        isStopGeneration = false;
    }

    public void setStopGeneration() {
        isStopGeneration = true;
    }
    @Override
    public void run() {
        byte[] bytes = new byte[20];
        while (!isStopGeneration) {
            try {
                if (scanner.ready()) {
                    blockChain.addTransaction(new Transaction(Integer.parseInt(scanner.readLine()), scanner.readLine(), scanner.readLine()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
