package blockchain;

import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Validator {

    protected static boolean validateChain(ConcurrentMap<Integer, Block> chainMap) {
        String blockchainOne = chainMap.values().stream()
                .map(Block::getBlockHash)
                .collect(Collectors.joining());
        StringBuilder blockchainTwo = new StringBuilder();
        for (var h : chainMap.values()) {
            blockchainTwo.append(h.getBlockHash());
        }
        return blockchainOne.equals(blockchainTwo.toString());
    }
}