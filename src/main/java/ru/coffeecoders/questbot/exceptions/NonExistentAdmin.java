package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentAdmin extends RuntimeException{

    public NonExistentAdmin() {
        super("Admin not found!");
    }

}
