package blockchain;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String text;
    private final byte[] signature;
    private final PublicKey publicKey;
    private long id;

    public Message(String author, String text) {
        this.text = author + ": " + text;
        signature = Cryptography.sign(this.text, PathsConstants.PRIVATE_KEY);
        publicKey = Cryptography.getPublic(PathsConstants.PUBLIC_KEY);
    }

    // getters
    public String getText() {
        return text;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public long getId() {
        return id;
    }

    // setters
    public void setId(long id) {
        this.id = id;
    }
}