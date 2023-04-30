package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentPlayer extends RuntimeException{

    public NonExistentPlayer() {
        super("Player not found!");
    }

}
