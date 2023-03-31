package ru.coffeecoders.questbot.commands.manager;

import org.springframework.stereotype.Component;

@Component
public class Reports {
    public final String NOT_GLOBAL_CHAT = "Вы находитесь в админском чате";
    public final String NOT_ADMIN_CHAT = "Вы находитесь в игровом чате";
    public final String INVALID_COMMAND = "Такой команды не существует";
    public final String NOT_ADMIN = "Вы не являетесь администратором";
}
