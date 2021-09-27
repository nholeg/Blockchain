package blockchain;

import java.util.Date;

public class Block {
    public static final int FIRST_ID = 1;

    private final int id;
    private final long createdAt;
    private int minerId;
    private String hash;
    private String prevHash;
    private long magicNumber;
    private long provingDuration;
    private String changeInN;

    public Block(int id, String prevHash) {
        this.createdAt = new Date().getTime();
        this.id = id;
        this.magicNumber = 0;
        this.setPrevHash(prevHash);
    }

    public int getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = id == FIRST_ID ? "0" : prevHash;
    }

    public boolean isFirstBlock() {
        return this.id == FIRST_ID;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(long magicNumber) {
        this.magicNumber = magicNumber;
    }

    public long getMinerId() {
        return minerId;
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }

    public void setProvingDuration(long provingDuration) {
        this.provingDuration = provingDuration;
    }

    public long getProvingDuration() {
        return this.provingDuration;
    }

    public long getProvingDurationInSeconds() {
        return this.provingDuration / 1_000_000_000;
    }

    public String getChangeInN() {
        return changeInN;
    }

    public void setChangeInN(String changeInN) {
        this.changeInN = changeInN;
    }

    @Override
    public String toString() {
        return this.id + this.createdAt + this.prevHash + this.magicNumber;
    }
}