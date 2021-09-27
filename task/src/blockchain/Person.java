package blockchain;

import java.util.Random;

public class Person {
    private final Blockchain blockchain;
    private final String name;
    private final Random random;
    private final String[] sentences = new String[]{
            "I love hyperskill!!!",
            "Java and Python are my favourite languages",
            "Blockchain is a great technology",
            "Hi, what is situating here?",
            "Spam spam spam spam"
    };

    public Person(Blockchain blockchain, String name) {
        this.blockchain = blockchain;
        this.name = name;
        random = new Random();
    }

    public void sendMessage() {
        Message message = new Message(name, sentences[random.nextInt(sentences.length)]);
        message.setId(blockchain.getAndIncreaseMessageCounter());
        blockchain.addMessage(message);
    }

    public void sleep() throws InterruptedException {
        Thread.sleep(200);
    }
}