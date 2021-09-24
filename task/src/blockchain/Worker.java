package blockchain;

public class Worker extends Thread {
    private String workerName;
    private String zerosPrefix;
    private int blockId;

    public Worker(String zerosPrefix, int blockId) {
        this.blockId = blockId;
        this.zerosPrefix = zerosPrefix;
    }

    @Override
    public void run() {
        Blockchain blockchain = Blockchain.getInstance();
        String[] arr = Thread.currentThread().getName().split("");
        workerName =arr[arr.length - 1];
        Block block = new Block(blockId, zerosPrefix, workerName);
            if (block.getHashingTime() < 15) {
                blockchain.incrementN();
                block.setStatementOfN("N was increased to " + blockchain.getN());
            } else if (block.getHashingTime() > 60) {
                blockchain.decrementN();
                block.setStatementOfN("N was decreased by 1");
            } else {
                block.setStatementOfN("N stays the same");
            }
        blockchain.addBlockToChain(block);

    }
}
