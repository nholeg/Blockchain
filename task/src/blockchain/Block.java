package blockchain;

import java.security.MessageDigest;
import java.util.Date;

public class Block {
    int id;
    long timestamp;
    String hashPrev;
    String hash;

    Block(int id, long timestamp,String hashPrev, String hash) {
        this.id = id;
        this.timestamp = timestamp;
        this.hashPrev = hashPrev;
        this.hash = calulateHash(hash);

    }
    /* Applies Sha256 to a string and returns a hash. */
    public static String calulateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getHashPrev() {
        return hashPrev;
    }

    public String getHash() {
        return hash;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
