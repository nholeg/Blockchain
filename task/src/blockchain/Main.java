package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int numOfZeros = 0;

        ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        BlockValidator validator = new BlockWithProofValidator(numOfZeros);
        EventManager eventManager = new EventManager(Event.values());
        BlockChain blockChain = new BlockChain(executors, eventManager, validator);
        MinerFactory minerFactory = new MinerFactory(new IdGenerator(), blockChain);
        MinersCentral minersCentral = new MinersCentral(executors);

        List<Miner> miners = getMiners(minerFactory);
        eventManager.subscribe(minersCentral, Event.values());
        minersCentral.subscribe(miners.toArray(new Miner[0]));

        minersCentral.startMining();
        boolean terminated = executors.awaitTermination(10, TimeUnit.SECONDS);
        if (terminated) {
            System.out.println(blockChain);
        }
    }

    public static List<Miner> getMiners(MinerFactory minerFactory) {
        int minerCount = Runtime.getRuntime().availableProcessors();
        List<Miner> miners = new ArrayList<>();
        for (int i = 0; i < minerCount; i++) {
            miners.add(minerFactory.createMiner());
        }
        return miners;
    }
}