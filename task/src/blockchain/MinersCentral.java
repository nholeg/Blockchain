package blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MinersCentral implements EventListener {

    private final List<Miner> miners;
    private final ExecutorService executors;

    public MinersCentral(ExecutorService executors) {
        this.executors = executors;
        miners = new ArrayList<>();
    }

    public void subscribe(Miner... miners) {
        for (var miner : miners) {
            if (miner != null) {
                this.miners.add(miner);
            }
        }
    }

    public void startMining() {
        for (var miner : miners) {
            this.executors.submit(miner::startMining);
        }
    }

    public void stopCurrentMiners() {
        this.miners.forEach(Miner::stopMining);
    }

    @Override
    public void update(Event event) {
        if (event == Event.CURRENT_BLOCK_MINED) {
            stopCurrentMiners();
        } else if (event == Event.NEW_BLOCK_OPEN) {
            startMining();
        }
    }
}