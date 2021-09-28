package blockchain;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockChain implements Serializable {
    private final long serialVersionUID = 9L;
    private int messageId;
    private transient int maxAvailableMessageId = 0;

    public void setNumOfZeros(int numOfZeros) {
        this.numOfZeros = numOfZeros;
    }

    private int numOfZeros;
    List<Block> blocks;
    BlockMessages blockMessages;

    private int currencyAward = 100;

    public BlockChain() {
        blocks = new ArrayList<>();
        numOfZeros = 0;
        blockMessages = new BlockMessages();
    }
    public synchronized int getNumOfZeros() {
        return numOfZeros;
    }

    public synchronized void addTransaction(Transaction transaction) {
        try {
            blockMessages.addMessage(generateMessageId(), maxAvailableMessageId, transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private synchronized int generateMessageId() {
        messageId++;
        return messageId;
    }

    public synchronized boolean addBlock(Block block) {
        if (checkAddingBlock(block) && isValid()) {
            block.setSpecialMessage(block.getMinerName() + " gets " + currencyAward + " VC");
            if (blocks.size() > 0) {
                block.setMessages(blockMessages.getMessages());
            }
            blocks.add(block);
            maxAvailableMessageId = block.getMaxMessageId();
            numOfZeros = block.getNewNumOfZeros();
            return true;
        }
        return false;
    }

    public synchronized Block getLastBlock() {
        if (blocks.size() == 0) {
            return null;
        }
        return blocks.get(blocks.size() -1);
    }

    @Override
    public String toString() {
        return blocks.stream()
                .map(b -> b.toString())
                .collect(Collectors.joining("\n"));
    }

    // проверка добавления блока
    private boolean checkAddingBlock(Block block) {
        if (getLastBlock() == null) {
            return true;
        }
        return getLastBlock().getCurHash().equals(block.getPreviousHash())
                & block.getCurHash().startsWith("0".repeat(numOfZeros));
    }

    public int count() {
        return blocks.size();
    }

    // проверяем цепочку на валидность
    public boolean isValid() {
        boolean isValid = true;
        if (this.count() == 0) {
            return true;
        }
        Block prevBlock = blocks.get(0);
        for (int i = 1; i < this.count(); i++) {
            isValid &= prevBlock.getCurHash().equals(blocks.get(i).getPreviousHash())
                    & blocks.get(i).verifyTransactions();
            prevBlock = blocks.get(i);
            if (!isValid) {
                break;
            }
        }
        return isValid;
    }

    //считаем баланс персоны у нас

}