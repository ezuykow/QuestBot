package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentNewCreatingGameState extends RuntimeException{

    public NonExistentNewCreatingGameState() {
        super("NewCreatingGameState not found!");
    }

}
