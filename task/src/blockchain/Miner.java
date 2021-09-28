package blockchain;

public class Miner implements Runnable {
    private final BlockChain blockChain;

    public Miner(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public void run() {
        while (true) {
            int id = blockChain.count() + 1;
            String previousHash = blockChain.count() == 0 ? "0" : blockChain.getLastBlock().getCurHash();
            Block block = new Block(id, previousHash, blockChain.getNumOfZeros(), "miner # " + String.valueOf(Thread.currentThread().getId()));
            if (blockChain.addBlock(block)) {
                break;
            }
        }
    }
}