package ru.coffeecoders.questbot.actions.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.viewers.GamesViewer;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminsCommandsActionsTest {

    @Mock
    private GamesViewer gamesViewer;
    @Mock
    private QuestionsViewer questionsViewer;
    @Mock
    private BlockingManager blockingManager;
    @Mock
    private RestrictingManager restrictingManager;
    @Mock
    private ApplicationShutdownManager applicationShutdownManager;
    @Mock
    private GlobalChatService globalChatService;
    @Mock
    private ChatAndUserValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;

    long senderAdminId = 1L;
    long chatId = 22L;

    @InjectMocks
    private AdminsCommandsActions actions;

    @Test
    void performShowGamesCmdTest() {
        String msg = " начал работать со списком игр, остальные пользователи временно заблокированы";
        when(messages.startGamesView()).thenReturn(msg);
        actions.performShowGamesCmd(senderAdminId, chatId);
        verify(gamesViewer).viewGames(chatId);
        verify(blockingManager).blockAdminChatByAdmin(chatId, senderAdminId, msg);
        verify(restrictingManager).restrictMembers(chatId, senderAdminId);
    }

    @Test
    void performShowQuestionsCmdTest() {
        String msg = " начал работать с вопросами, остальные пользователи временно заблокированы";
        when(messages.startQuestionView()).thenReturn(msg);
        actions.performShowQuestionsCmd(senderAdminId, chatId);
        verify(questionsViewer).viewQuestions(chatId);
        verify(blockingManager).blockAdminChatByAdmin(chatId, senderAdminId, msg);
        verify(restrictingManager).restrictMembers(chatId, senderAdminId);
    }

    @Test
    void performDeleteChatCmdIfAdminChatTest() {
        String msg = "Эту команду можно использовать только в не администраторском чате";
        when(validator.isGlobalChat(chatId)).thenReturn(false);
        when(messages.cmdForGlobalChat()).thenReturn(msg);
        actions.performDeleteChatCmd(chatId);
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void performDeleteChatCmdIfGlobalChatTest() {
        String msg = "Этот чат больше не в Игре";
        when(validator.isGlobalChat(chatId)).thenReturn(true);
        when(messages.chatNotInGame()).thenReturn(msg);
        actions.performDeleteChatCmd(chatId);
        verify(msgSender).send(chatId, msg);
        verify(msgSender).sendLeaveChat(chatId);
        verify(globalChatService).deleteById(chatId);
    }

    @Test
    void performStopBotCmdTest() {
        actions.performStopBotCmd();
        verify(msgSender).sendStopBot();
        verify(applicationShutdownManager).stopBot();
    }
}
