package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentTeam extends RuntimeException{

    public NonExistentTeam() {
        super("Team not found!");
    }

}
