package ru.coffeecoders.questbot.exceptions;

/**
 * @author ezuykow
 */
public class NonExistentTask extends RuntimeException{

    public NonExistentTask() {
        super("Task not found!");
    }

}
