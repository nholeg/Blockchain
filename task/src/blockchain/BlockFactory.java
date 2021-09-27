package blockchain;

public class BlockFactory {

    private final IdGenerator idGenerator;
    private final BlockChain blockChain;

    public BlockFactory(IdGenerator idGenerator, BlockChain blockChain) {
        this.idGenerator = idGenerator;
        this.blockChain = blockChain;
    }

    public Block createBlock() {
        int id = idGenerator.getId();

        return blockChain.size() == 0 && id == Block.FIRST_ID ?
                new Block(id, "0") :
                new Block(id, blockChain.getBlocks().get(blockChain.size() - 1).getHash());
    }
}