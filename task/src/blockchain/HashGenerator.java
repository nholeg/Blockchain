package blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashGenerator {

    public String createHash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (var val : hash) {
                String hex = Integer.toHexString(0xff & val);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}