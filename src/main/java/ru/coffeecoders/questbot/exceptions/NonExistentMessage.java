package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentMessage extends RuntimeException{

    public NonExistentMessage() {
        super("Message not found!");
    }

}
