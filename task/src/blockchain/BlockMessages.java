package blockchain;

import blockchain.crypto.DigitalSigner;
import blockchain.crypto.KeysGenerator;
import blockchain.crypto.KeysReader;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// объект сообщение - который хранит некий id + текст, и цифровую подпись
class Message implements Serializable{
    private final long serialVersionUID = 3L;
    // пути к файлам ключей - лежат в классе закрыто
    private final String PATH_TO_PUBLIC = Paths.get("crypto/publicKey").toAbsolutePath().toString();
    private final String PATH_TO_PRIVATE = Paths.get("crypto/secretKey").toAbsolutePath().toString();

    private byte[] sign = null;
    private final Transaction message;

    public int getMessageId() {
        return messageId;
    }

    private final int messageId;

    public String getMessage() throws InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        return verifyMessage() ? message.getMessage() : "cant return not verify transaction";
    }

    public String getAuthor() {
        return this.message.getAuthor();
    }
    private PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        try {
            return KeysReader.readPrivate(PATH_TO_PRIVATE);
        } catch (IOException e) {
            KeysGenerator keygen = new KeysGenerator(1024);
            System.out.println(Paths.get(PATH_TO_PRIVATE).getParent().toString());
            keygen.writeKeys("crypto");
            return KeysReader.readPrivate(PATH_TO_PRIVATE);
        }
    }

    private String generateMessage() {
        return String.format("%d\n%s", this.messageId, this.message.getMessage());
    }

    public Message(int id, Transaction msg) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        // сохраним данные в переменных класса
        this.message = msg;
        this.messageId = id;
        // сгенерим подпись
        sign = DigitalSigner.signMessage(generateMessage(), getPrivateKey());
    };

    public boolean verifyMessage() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublicKey());
        sig.update(generateMessage().getBytes());

        return sig.verify(this.sign);
    };

    public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        try {
            return KeysReader.readPublic(PATH_TO_PUBLIC);
        } catch (IOException e) {
            KeysGenerator keygen = new KeysGenerator(1024);
            keygen.writeKeys("crypto");
            return KeysReader.readPublic(PATH_TO_PUBLIC);
        }
    }
};

public class BlockMessages implements Serializable {
    private final long serialVersionUID = 10L;
    volatile List<Message> messages;

    public BlockMessages() {
        messages = new ArrayList<>();
    }

    public synchronized List<Message> getMessages() {
        // скопируем
        List<Message> msg = messages.stream()
                .collect(Collectors.toList());
        // очистим текущий
        messages.clear();
        return msg;
    }

    public void addMessage(int messageId, int maxAvailableId, Transaction transaction) throws InvalidKeySpecException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        if (messageId > maxAvailableId) {
            this.messages.add(new Message(messageId, transaction));
        }
    }
}