package ru.coffeecoders.questbot.managers.callbacks;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class QuestionViewerCallbackManagerTest {

    @Mock
    QuestionsViewer questionsViewer;

    @InjectMocks
    QuestionViewerCallbackManager questionViewerCallbackManager;

    @Test
    public void shouldCallSwitchPageToPreviousMethod() {
        final String data = "QuestionViewer.Switch page to previous....";
        doNothing().when(questionsViewer).switchPageToPrevious(any(), any());

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verify(questionsViewer).switchPageToPrevious(any(), eq(data));
    }

    @Test
    public void shouldCallSwitchPageToNextMethod() {
        final String data = "QuestionViewer.Switch page to next....";
        doNothing().when(questionsViewer).switchPageToNext(any(), any());

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verify(questionsViewer).switchPageToNext(any(), eq(data));
    }

    @Test
    public void shouldCallShowQuestionInfoMethod() {
        final String data = "QuestionViewer.Taken index.....";
        doNothing().when(questionsViewer).showQuestionInfo(any(), any());

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verify(questionsViewer).showQuestionInfo(any(), eq(data));
    }

    @Test
    public void shouldCallDeleteViewMethod() {
        final String data = "QuestionViewer.Delete message";
        doNothing().when(questionsViewer).deleteView(any());

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verify(questionsViewer).deleteView(any());
    }

    @Test
    public void shouldCallBackFromQuestionInfoMethod() {
        final String data = "QuestionViewer.QuestionInfo.Back";
        doNothing().when(questionsViewer).backFromQuestionInfo(any());

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verify(questionsViewer).backFromQuestionInfo(any());
    }

    @Test
    public void shouldDoNothingWhenUnknownCallbackData() {
        final String data = "Some data!";

        questionViewerCallbackManager.manageCallback(new ExtendedUpdate(new Update()), data);
        verifyNoInteractions(questionsViewer);
    }
}