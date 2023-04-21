package ru.coffeecoders.questbot.managers.callbacks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class QuestionViewerCallbackManagerTest {

    @Mock
    QuestionsViewer questionsViewer;
    @Mock
    AdminChatService adminChatService;
    @Mock
    ChatAndUserValidator validator;
    @Mock
    AdminChat adminChat;

    @InjectMocks
    QuestionViewerCallbackManager questionViewerCallbackManager;

    private long senderId;
    private long blockedAdminId;
    private long ownerId;
    private long chatId;
    private int msgId;

    @BeforeEach
    public void setUp() {
        senderId = 1L;
        blockedAdminId = senderId;
        ownerId = 2L;
        chatId = 1L;
        msgId = 1;

        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getBlockedByAdminId()).thenReturn(blockedAdminId);
    }

    @Test
    public void shouldCallSwitchPageToPreviousMethod() {
        final String data = "QuestionViewer.Switch page to previous....";
        questionViewerCallbackManager.manageCallback(senderId, chatId, msgId, data);
        verify(questionsViewer).switchPageToPrevious(chatId, msgId, data);
    }
/*
    @Test
    public void shouldCallSwitchPageToNextMethod() {
        final String data = "QuestionViewer.Switch page to next....";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verify(questionsViewer).switchPageToNext(chatId, msgId, data);
    }

    @Test
    public void shouldCallShowQuestionInfoMethod() {
        final String data = "QuestionViewer.Taken index.....";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verify(questionsViewer).showQuestionInfo(chatId, msgId, data);
    }

    @Test
    public void shouldCallDeleteViewMethod() {
        final String data = "QuestionViewer.Delete message";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verify(questionsViewer).deleteView(chatId, msgId);
    }

    @Test
    public void shouldCallBackFromQuestionInfoMethod() {
        final String data = "QuestionViewer.QuestionInfo.Back";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verify(questionsViewer).backFromQuestionInfo(chatId, msgId);
    }

    @Test
    public void shouldDoNothingWhenUnknownCallbackData() {
        final String data = "Some data!";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verifyNoInteractions(questionsViewer);
    }*/
}