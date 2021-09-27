package blockchain;

import java.util.Random;

public class Miner {
    private final int id;
    private final BlockChain blockChain;
    private final HashGenerator hashGenerator;
    private final Random random;
    private final BlockFactory blockFactory;
    private boolean continueMining;

    public Miner(int id, BlockChain blockChain, BlockFactory blockFactory, HashGenerator hashGenerator) {
        this.id = id;
        this.blockChain = blockChain;
        this.hashGenerator = hashGenerator;
        this.blockFactory = blockFactory;
        this.random = new Random();
        this.continueMining = true;
    }

    public void mine(Block block) {
        this.continueMining = true;
        block.setMinerId(this.id);

        long startTime = System.nanoTime();
        block.setHash(hashGenerator.createHash(block.toString()));
        while (this.continueMining && !blockChain.isBlockHashProven(block)) {
            block.setMagicNumber(random.nextLong());
            block.setHash(hashGenerator.createHash(block.toString()));
            block.setMessage(MessageGenerator.getMessage(this.id));
        }

        if (!this.continueMining) {
            return;
        }
        long endTime = System.nanoTime();

        block.setProvingDuration((endTime - startTime));

        blockChain.acceptNewBlock(block);
    }

    public int getId() {
        return id;
    }

    public BlockChain getBlockChain() {
        return blockChain;
    }

    public void startMining() {
        Block block = blockFactory.createBlock();
        mine(block);
    }

    public void stopMining() {
        this.continueMining = false;
    }
}