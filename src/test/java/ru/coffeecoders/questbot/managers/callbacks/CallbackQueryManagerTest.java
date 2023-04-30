package ru.coffeecoders.questbot.managers.callbacks;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class CallbackQueryManagerTest {

    @Mock
    QuestionViewerCallbackManager questionViewerCallbackManager;
    @Mock
    ExtendedUpdate exUpdate;

    @InjectMocks
    CallbackQueryManager callbackQueryManager;
/*
    @Test
    public void shouldCallQuestionViewerCallbackManagerThenDataMatch() {
        String data = "QuestionViewer.....";
        long chatId = 1L;
        int msgId = 1;
        when(exUpdate.getCallbackQueryData()).thenReturn(data);
        when(exUpdate.getCallbackMessageChatId()).thenReturn(chatId);
        when(exUpdate.getCallbackMessageId()).thenReturn(msgId);

        callbackQueryManager.manageCallback(exUpdate);
        verify(questionViewerCallbackManager).manageCallback(chatId, msgId, data);
    }*/

}