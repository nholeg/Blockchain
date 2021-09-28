package blockchain;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        final String FILE_NAME = "D:\\blockchain.txt";

        BlockChain blockChain;
        // считаем с диска
        try(FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            blockChain = (BlockChain) ois.readObject();
        } catch (IOException e) {
            blockChain = new BlockChain();
        }
        // чтобы шаги быстро выполнялись и с 0 начинались.
        //blockChain = new BlockChain();
        // создаем executors для параллельного выполнения разных операций
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorService messageGenerator = Executors.newSingleThreadExecutor();
        MessageGenerator generator = new MessageGenerator(blockChain);
        messageGenerator.submit(generator);
        // generate 5 blocks
        for (int i = 0; i < 15; i++){
            final Miner miner = new Miner(blockChain);
            executor.submit(miner);
        }
        executor.shutdown();
        messageGenerator.shutdown();

        // проверка остановки - ждем, пока Miner остановится и затем убираем генератор сообщений
        while (true) {
             boolean isTerminated = executor.awaitTermination(1L, TimeUnit.SECONDS);
            if (isTerminated) {
                generator.setStopGeneration();
                break;
            }
        }
        // выведем на экран
        System.out.println(blockChain);
        // сохраним на диск
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(blockChain);
        }
    }
}