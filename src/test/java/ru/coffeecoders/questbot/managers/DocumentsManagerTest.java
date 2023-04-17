package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import ru.coffeecoders.questbot.documents.DocumentDownloader;
import ru.coffeecoders.questbot.documents.QuestionsFromExcelParser;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.validators.ChatAndUserIdValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class DocumentsManagerTest {

    @Mock
    private QuestionsFromExcelParser parser;
    @Mock
    private DocumentDownloader downloader;
    @Mock
    private ChatAndUserIdValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Environment env;
    @Mock
    private ExtendedUpdate update;
    @Mock
    private Document doc;

    @InjectMocks
    private DocumentsManager manager;

    @Test
    public void shouldDownloadAndParseDocumentWhenAllCorrect() {
        when(update.getDocument()).thenReturn(doc);
        when(update.getMessageChatId()).thenReturn(-1L);
        when(update.getMessageFromUserId()).thenReturn(-1L);

        when(downloader.downloadDocument(any())).thenReturn(null);

        doNothing().when(parser).parse(any(), anyLong());

        when(env.getProperty("document.excel.mimeType")).
                thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        when(validator.isAdminChat(anyLong())).thenReturn(true);
        when(validator.isAdmin(anyLong())).thenReturn(true);
        when(doc.mimeType()).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        manager.manageDocument(update);
        verify(downloader).downloadDocument(any());
        verify(parser).parse(any(), anyLong());
    }

    @Test
    public void shouldDoNothingWhenChatIsNotAdmin() {
        when(validator.isAdminChat(anyLong())).thenReturn(false);

        manager.manageDocument(update);
        verifyNoInteractions(downloader);
        verifyNoInteractions(parser);
        verifyNoInteractions(msgSender);
    }

    @Test
    public void shouldSendMsgWhenUserIsNotAdmin() {
        when(validator.isAdminChat(anyLong())).thenReturn(true);
        when(validator.isAdmin(anyLong())).thenReturn(false);

        String msg = "Добавлять вопросы может только администратор";
        when(env.getProperty("messages.documents.fromNotAdmin")).thenReturn(msg);

        manager.manageDocument(update);
        verifyNoInteractions(downloader);
        verifyNoInteractions(parser);
        verify(msgSender).send(anyLong(), eq(msg));
    }

    @Test
    public void shouldSendMsgWhenFileIsNotExcel() {
        when(validator.isAdminChat(anyLong())).thenReturn(true);
        when(validator.isAdmin(anyLong())).thenReturn(true);

        when(update.getDocument()).thenReturn(doc);
        when(doc.mimeType()).thenReturn("incorrect mime-Type");
        when(env.getProperty("document.excel.mimeType")).
                thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String msg = "Поддерживается добавление вопросов только с файла Excel";
        when(env.getProperty("messages.documents.wrongDocumentType")).thenReturn(msg);

        manager.manageDocument(update);
        verifyNoInteractions(downloader);
        verifyNoInteractions(parser);
        verify(msgSender).send(anyLong(), eq(msg));
    }
}