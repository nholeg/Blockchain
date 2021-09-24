package blockchain;

import java.util.concurrent.ConcurrentHashMap;

public class Blockchain {

    private static volatile int n = 0;

    protected static ConcurrentHashMap<Integer, Block> chainMap;

    private static Blockchain instance;

    private Blockchain () { }

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
        }
        return instance;
    }

    protected static synchronized void addBlockToChain(Block block) {
        chainMap.put(block.getBlockId(), block);
    }

    public synchronized static int getN() {
        return n;
    }

    public static synchronized void incrementN() {
        n++;
    }
    public static synchronized void decrementN() {
        n--;
    }
}
