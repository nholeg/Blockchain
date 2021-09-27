package blockchain;

import java.io.*;
import java.util.*;

public class Blockchain implements Serializable {
    private static final long serialVersionUID = 1L;
    private long counter;
    private final List<Block> blocks;
    private byte zerosNumber;
    private int sessionBlocks;
    private final List<Message> messages;
    private long messageCounter;

    private Blockchain() {
        counter = 1;
        messageCounter = 0;
        sessionBlocks = 0;
        blocks = new ArrayList<>();
        messages = new ArrayList<>();
        zerosNumber = 0;
    }

    public synchronized void addBlock(Block block, long start, long end, int number) {
        blocks.add(block);
        if (isValid()) {
            block.setMessages(messages.toArray(new Message[0]));
            messages.clear();
            counter++;
            long seconds = (end - start) / 1000;
            sessionBlocks++;
            System.out.println("Block:");
            System.out.println("Created by miner # " + number);
            System.out.println(block);
            System.out.print("Block data: ");
            if (block.getMessages().length > 0) {
                System.out.println();
                Arrays.stream(block.getMessages()).forEach(
                        message -> System.out.println(message.getText())
                );
            } else {
                System.out.println("no messages");
            }
            System.out.printf("Block was generating for %d seconds\n", seconds);
            if (seconds == 0 && zerosNumber < 5) {
                zerosNumber++;
                System.out.println("N was increased to " + zerosNumber);
            } else if (seconds == 1) {
                System.out.println("N stays the same");
            } else if (zerosNumber > 0) {
                zerosNumber--;
                System.out.println("N was decreased by 1");
            }
            System.out.println();
            // Write to file
            try {
                SerializationUtils.serialize(this, PathsConstants.BLOCKCHAIN_INFO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            blocks.remove(block);
        }
    }

    public static Blockchain getInstance() {
        try {
            Blockchain blockchain = (Blockchain) SerializationUtils.deserialize(PathsConstants.BLOCKCHAIN_INFO);
            blockchain.setSessionBlocks(0);
            return blockchain;
        } catch (ClassNotFoundException | IOException e) {
            return new Blockchain();
        }
    }

    public synchronized void addMessage(Message message) {
        long id = message.getId();
        for (Message m : messages) {
            if (m.getId() > id) {
                return;
            }
        }
        for (Message m : getLastBlock().getMessages()) {
            if (m.getId() > id) {
                return;
            }
        }
        if (Cryptography.verifySignature(message.getText().getBytes(), message.getSignature(), message.getPublicKey()))
            messages.add(message);
    }

    public boolean isValid() {
        try {
            if (blocks.size() > 1) {
                HashSet<Long> ids = new HashSet<>();
                ids.add(blocks.get(0).getId());
                for (int i = 1; i < blocks.size(); i++) {
                    if (!blocks.get(i).getHashPrevBlock().equals(blocks.get(i - 1).getHash())) {
                        return false;
                    }
                    long id = blocks.get(i).getId();
                    if (ids.contains(id)) {
                        return false;
                    }
                    ids.add(id);
                }
                List<Long> list = new ArrayList<>();
                list.add(-1L);
                for (Block block : blocks) {
                    if (block.getMessages() == null)
                        continue;
                    for (Message message : block.getMessages()) {
                        if (message.getId() > list.get(list.size() - 1)) {
                            list.add(message.getId());
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // getters
    public Block getLastBlock() {
        return blocks.size() > 0 ? blocks.get(blocks.size() - 1) : null;
    }

    public synchronized boolean continueWorking() {
        return sessionBlocks < 5;
    }

    public long getCounter() {
        return counter;
    }

    public byte getZerosNumber() {
        return zerosNumber;
    }

    public long getAndIncreaseMessageCounter() {
        return ++messageCounter;
    }

    // setters
    public void setSessionBlocks(int sessionBlocks) {
        this.sessionBlocks = sessionBlocks;
    }
}