package blockchain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Storage {
    private static final String STORAGE_FILENAME = "storage.data";
    private static final String STORAGE_DATA_PATH = "src/blockchain/";
    private static File blockchainFile = Path.of(STORAGE_FILENAME).toFile();
    private AtomicInteger blockID;

    protected synchronized Storage initializeStorage() {
        if (!blockchainFile.exists()) {
            blockchainFile = new File(STORAGE_DATA_PATH + STORAGE_FILENAME);
            blockID = new AtomicInteger(1);
            Blockchain.chainMap = new ConcurrentHashMap<>();
        } else {
            try (InputStream inputStream = Files.newInputStream(blockchainFile.toPath());
                 ObjectInput objectInput = new ObjectInputStream(inputStream)) {
                blockID = new AtomicInteger(objectInput.readInt());
                Blockchain.chainMap = (ConcurrentHashMap<Integer, Block>) objectInput.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    protected void serializeData() {
        try (OutputStream outputStream = Files.newOutputStream(blockchainFile.toPath());
             ObjectOutput objectOutput = new ObjectOutputStream(outputStream)) {
            objectOutput.writeInt(blockID.get());
            objectOutput.writeObject(Blockchain.chainMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected synchronized int getBlockId() {
        return blockID.getAndIncrement();
    }
}
