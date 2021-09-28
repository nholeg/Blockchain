package blockchain.crypto;

import javax.net.ssl.KeyStoreBuilderParameters;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class KeysGenerator {
    private static final String RSA_ALGO = "RSA";
    private final KeyPair keyPair;

    private void writeDataToFile(String filePath, byte[] data) {
        try(BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(filePath))
        )) {
            bos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeKeys(String dirName) throws IOException {
        File dir = new File(dirName);
        if (! dir.exists()) {
            dir.mkdir();
        }
        File[] files = dir.listFiles();
        // удалим существующие файлы
        Arrays.stream(files)
                .filter(file -> List.of("secretKey", "publicKey").contains(file.getName()))
                .map(file -> file.delete());
        // запишем их
        Path filePath = Paths.get(dirName, "secretKey");
        writeDataToFile(filePath.toString(), this.keyPair.getPrivate().getEncoded());
        System.out.println("Private: " + filePath.toAbsolutePath().toString());
        Path filepath = Paths.get(dirName, "publicKey");
        writeDataToFile(filepath.toString(), this.keyPair.getPublic().getEncoded());
        System.out.println(filePath.toAbsolutePath().toString());

    }

    public KeysGenerator(int keysLength) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGO);
        generator.initialize(keysLength);
        this.keyPair = generator.generateKeyPair();
    }


}
