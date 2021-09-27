package blockchain;

public class MessageGenerator {
    private static int messageNumber = 0;
    private static StringBuilder stringBuilder = new StringBuilder();

    public static synchronized String getMessage(int minerName) {
        // Emulate messages from users, sent in queue While block generated
        messageNumber++;
        stringBuilder.append(messageNumber + " ");
        return stringBuilder.toString();
    }
    public static synchronized void deleteMessagesFromTheQueue() {
        stringBuilder = new StringBuilder();
    }

}
