package blockchain;

public class MinerFactory {

    private final IdGenerator idGenerator;
    private BlockChain blockChain;

    public MinerFactory(IdGenerator idGenerator, BlockChain blockChain) {
        this.idGenerator = idGenerator;
        this.blockChain = blockChain;
    }

    public Miner createMiner() {
        return new Miner(
                idGenerator.getId(),
                blockChain,
                new BlockFactory(new IdGenerator(), blockChain),
                new HashGenerator()
        );
    }

    public void setBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }
}