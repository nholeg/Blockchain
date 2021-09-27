package blockchain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Cryptography {

    public static byte[] sign(String data, String filename) {
        try {
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(getPrivate(filename));
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return null;
        }
    }

    private static PrivateKey getPrivate(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            return null;
        }
    }

    public static PublicKey getPublic(String filename) {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return false;
        }
    }
}