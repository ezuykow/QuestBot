package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentReplyToMessage extends RuntimeException{

    public NonExistentReplyToMessage() {
        super("ReplyToMessage not found!");
    }

}
