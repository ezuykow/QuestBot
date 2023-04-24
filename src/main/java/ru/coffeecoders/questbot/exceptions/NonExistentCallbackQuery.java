package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentCallbackQuery extends RuntimeException{

    public NonExistentCallbackQuery() {
        super("CallbackQuery not found!");
    }

}
