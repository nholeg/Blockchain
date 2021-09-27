package blockchain;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class BlockChain {
    private final BlockValidator validator;
    private final Hashtable<Integer, Block> blocksHT;
    private final List<Block> blockList;
    private final ExecutorService executors;
    private final EventManager eventManager;

    public BlockChain(ExecutorService executors, EventManager eventManager, BlockValidator validator) {
        this.validator = validator;
        this.blocksHT = new Hashtable<>();
        this.blockList = new ArrayList<>();
        this.executors = executors;
        this.eventManager = eventManager;
    }

    public final boolean isValid() {
        return true;
    }

    public synchronized void acceptNewBlock(Block block) {
        if (containsBlock(block)) {
            return;
        }

        if (!validator.isBlockHashValid(block)) {
            return;
        }

        blocksHT.put(block.getId(), block);
        blockList.add(block);
        eventManager.notify(Event.CURRENT_BLOCK_MINED);
        tweakNumberOfStartingZeros();

        if (blockList.size() < 5) {
            eventManager.notify(Event.NEW_BLOCK_OPEN);
        } else {
            executors.shutdownNow();
        }
    }

    private void tweakNumberOfStartingZeros() {
        if (this.blockList.isEmpty()) {
            return;
        }

        Block lastBlock = this.blockList.get(this.blockList.size() - 1);
        long lastProvingDuration = lastBlock.getProvingDurationInSeconds();
        int numberOfZeros = this.validator.getStartingZeroCount();

        if (lastProvingDuration >= 0 && lastProvingDuration <= 10) {
            this.validator.setStartingZeroCount(numberOfZeros + 1);
            lastBlock.setChangeInN("N was increased to " + (numberOfZeros + 1));
        } else if (lastProvingDuration > 60) {
            this.validator.setStartingZeroCount(numberOfZeros - 1);
            lastBlock.setChangeInN("N was decreased by " + (numberOfZeros - 1));
        } else {
            lastBlock.setChangeInN("N stays the same");
        }
    }

    public boolean isBlockHashProven(Block block) {
        return this.validator.isBlockHashProven(block);
    }

    public List<Block> getBlocks() {
        return blockList;
    }
    public int size() {
        return blockList.size();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        for (var block : blockList) {
            strBuilder.append("Block:\n")
                    .append("Created by miner # ")
                    .append(block.getMinerId())
                    .append("\nId: ")
                    .append(block.getId())
                    .append("\nTimestamp: ")
                    .append(block.getCreatedAt())
                    .append("\nMagic number: ")
                    .append(block.getMagicNumber())
                    .append("\nHash of the previous block:\n")
                    .append(block.getPrevHash())
                    .append("\nHash of the block:\n")
                    .append(block.getHash())
                    .append("\nBlock was generating for ")
                    .append(block.getProvingDurationInSeconds())
                    .append(" seconds\n")
                    .append(block.getChangeInN())
                    .append("\n\n");
        }
        return strBuilder.toString();
    }

    private boolean containsBlock(Block block) {
        return blocksHT.containsKey(block.getId());
    }
}