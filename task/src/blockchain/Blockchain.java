package blockchain;

import java.util.concurrent.ConcurrentHashMap;

public class Blockchain {
    protected static ConcurrentHashMap<Integer, Block> chainMap;

    protected static Block createNewBlock(String zerosPrefix, int blockId) {
        return new Block(blockId, zerosPrefix);
    }

    protected static synchronized void addBlockToChain(Block block) {
        chainMap.put(block.getBlockId(), block);
    }
}
