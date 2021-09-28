package blockchain;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Block implements Serializable {
    private final long serialVersionUID = 13L;

    private Integer id;
    private Long timeStamp;
    private String previousHash;
    private String curHash;
    // дополнительные данные блока
    private transient Integer magicNumber;
    private final int numOfZeros;
    private final String minerName;
    private LocalTime generatedTime;
    private List<Message> messages;
    private String specialMessage;
    private int newNumOfZeros;
    private String numOfZerosChangeString;

    public void setMagicNumber(Integer magicNumber) {
        this.magicNumber = magicNumber;
        generateHash();
    }

    public int getNewNumOfZeros() {
        return newNumOfZeros;
    }
    public String getMinerName() {
        return minerName;
    }
    private String prepareForHash() {
        return id.toString() +
                timeStamp.toString() +
                previousHash +
                magicNumber.toString();
    }

    private void generateHash() {
        boolean isStopGenerating = false;
        String hash = "";
        LocalTime startTime = LocalTime.now();

        while (!isStopGenerating) {
            magicNumber = new Random().nextInt();
            hash = StringUtil.applySha256(prepareForHash());
            boolean b = true;
            for (int i = 0; i < this.numOfZeros; i++) {
                b = b && hash.charAt(i) == '0';
                if (!b) {
                    break;
                }
            }
            isStopGenerating = b;
        }
        if (isStopGenerating) {
            curHash = hash;
        }
        LocalTime stopTime = LocalTime.now();
        generatedTime = stopTime.minusSeconds(startTime.toSecondOfDay());
        // рассчитаем, надо ли менять число нулей
        int time = generatedTime.toSecondOfDay();
        if (time < 0) {
            newNumOfZeros = numOfZeros + 1;
            numOfZerosChangeString = "N increased to " + this.newNumOfZeros;
        } else if (time > 2) {
            newNumOfZeros = numOfZeros - 1;
            numOfZerosChangeString = "N was decreased by 1";
        } else {
            newNumOfZeros = numOfZeros;
            numOfZerosChangeString = "N stays the same";
        }
    }

    public Block(Integer id,
                 String previousHash,
                 Integer numOfZeros,
                 String minerName) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.numOfZeros = numOfZeros;
        this.minerName = minerName;
        this.messages = null;
        this.specialMessage = null;
        generateHash();
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getCurHash() {
        return curHash;
    }

    public synchronized void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public synchronized void setSpecialMessage(String message) {
        this.specialMessage = message;
    }

    public boolean verifyTransactions() {
        if (this.messages == null) {
            return true;
        }
        return this.messages.stream()
            .filter(message -> {
                try {
                    return message.verifyMessage();
                } catch (Exception e) {
                    return false;
                }
            })
            .count() == this.messages.size();
    }

    public int getMaxMessageId() {
        return this.messages.stream()
                .map(Message::getMessageId)
                .max(Integer::compareTo)
                .orElse(0);
    }

    @Override
    public synchronized String toString() {
        String transactions;
        if (this.messages == null) {
            transactions = "No transactions\n";
        } else {
            List<String> transList = this.messages.stream()
                    .map(m -> {
                        try {
                            return m.getMessage();
                        } catch (Exception e) {
                            return "";
                        }
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            transactions = transList.size() == 0 ? "No transactions\n" : transList.stream().collect(Collectors.joining("\n"));
        }
        this.specialMessage = String.format("%s%c", specialMessage, '\n');
        return "Block:\n" +
            "Created by " + minerName + '\n' +
            this.specialMessage +
            "Id: " + id + "\n" +
            "Timestamp: " + timeStamp + "\n" +
            "Magic number: " + magicNumber + "\n" +
            "Hash of the previous block:\n" +
            previousHash + "\n" +
            "Hash of the block:\n" +
            curHash + "\n" +
            "Block data:" + "\n" + transactions +
            "Block was generating for " + generatedTime.toSecondOfDay() + " seconds\n" +
            numOfZerosChangeString + "\n";
    }
}