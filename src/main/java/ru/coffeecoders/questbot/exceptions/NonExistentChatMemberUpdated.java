package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentChatMemberUpdated extends RuntimeException{

    public NonExistentChatMemberUpdated() {
        super("ChatMemberUpdated not found!");
    }

}
