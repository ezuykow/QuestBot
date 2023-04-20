package ru.coffeecoders.questbot.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.managers.callbacks.CallbackQueryManager;
import ru.coffeecoders.questbot.managers.callbacks.QuestionViewerCallbackManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void shouldCallQuestionViewerCallbackManagerThenDataMatch() {
        when(exUpdate.getCallbackQueryData()).thenReturn("QuestionViewer.....");
        doNothing().when(questionViewerCallbackManager).manageCallback(any(), any());

        callbackQueryManager.manageCallback(exUpdate);
        verify(questionViewerCallbackManager).manageCallback(any(), any());
    }

}