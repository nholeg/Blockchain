package blockchain.crypto;

import java.security.*;

// класс, подписывающий сообщение
public class DigitalSigner {
    public static byte[] signMessage(String message, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(key);
        rsa.update(message.getBytes());
        return rsa.sign();
    }
}
