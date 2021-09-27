package blockchain;

import java.util.Date;

public class Miner {
    private final Blockchain blockchain;
    private final int number;

    public Miner(Blockchain blockchain, int number) {
        this.blockchain = blockchain;
        this.number = number;
    }

    public void createBlock() {
        long start = new Date().getTime();
        String hash = blockchain.getLastBlock() == null ? null : blockchain.getLastBlock().getHash();
        Block block = new Block(blockchain.getCounter(), hash, blockchain.getZerosNumber());
        long end = new Date().getTime();
        blockchain.addBlock(block, start, end, number);
    }
}