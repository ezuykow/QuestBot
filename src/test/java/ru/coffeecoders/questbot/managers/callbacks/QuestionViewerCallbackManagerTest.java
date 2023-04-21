package ru.coffeecoders.questbot.managers.callbacks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class QuestionViewerCallbackManagerTest {

    @Mock
    QuestionsViewer questionsViewer;

    @InjectMocks
    QuestionViewerCallbackManager questionViewerCallbackManager;

    private long chatId;
    private int msgId;

    @BeforeEach
    public void setUp() {
        chatId = 1L;
        msgId = 1;
    }

    @Test
    public void shouldCallSwitchPageToPreviousMethod() {
        final String data = "QuestionViewer.Switch page to previous....";
        questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        verify(questionsViewer).switchPageToPrevious(chatId, msgId, data);
    }

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
    }
}