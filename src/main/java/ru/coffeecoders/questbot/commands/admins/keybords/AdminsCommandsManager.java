package ru.coffeecoders.questbot.commands.admins.keybords;

import org.springframework.stereotype.Component;

@Component
public class AdminsCommandsManager {


    private final DefaultAdminKeyboard defaultAmKb;
    private final NewGameAdminKeyboard newGameAmKb;
    private final QuestionsCommandsAdminKeyboard questionsCommandAmKb;

    public AdminsCommandsManager(DefaultAdminKeyboard defaultAmKb, NewGameAdminKeyboard newGameAmKb, QuestionsCommandsAdminKeyboard questionsCommandAmKb) {
        this.defaultAmKb = defaultAmKb;
        this.newGameAmKb = newGameAmKb;
        this.questionsCommandAmKb = questionsCommandAmKb;
    }
        public void handleUserRequest(String userRequest, long chatId, int userId) {
        if (isAdmin(userId)) {
            switch (userRequest) {
                case "case1"-> questionsCommandAmKb.allRunningGamesCommand(chatId, userId);

                case "case2"-> questionsCommandAmKb.questionsCommand(chatId, userId);

                case "case3" -> defaultAmKb.exitToKeyboardCommand(chatId);

                case "case4" ->newGameAmKb.newGameCommand(chatId, userId);

                default -> defaultAmKb.defaultKeyboardNotAdmin(chatId);

            }
        } else {
            defaultAmKb.defaultKeyboardNotAdmin(chatId);
        }

    }




}
