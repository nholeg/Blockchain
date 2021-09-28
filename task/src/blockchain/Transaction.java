package blockchain;

import java.io.Serializable;

public class Transaction implements Serializable {
    private final long serialVersionUID = 1L;
    final private int amount;
    final private String fromName;
    final private String toName;

    public int getAmount() {
        return amount;
    }
    public String getAuthor() {return fromName;}

    public String getMessage() {
        return String.format("%s sent %d VC to %s", fromName, amount, toName);
    }

    public Transaction(int amount, String fromName, String toName) {
        this.amount = amount;
        this.fromName = fromName;
        this.toName = toName;
    }
}
