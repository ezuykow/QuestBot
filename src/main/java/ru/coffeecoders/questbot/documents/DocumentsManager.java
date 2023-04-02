package ru.coffeecoders.questbot.documents;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class DocumentsManager {

    Logger logger = LoggerFactory.getLogger(DocumentsManager.class);

    @Value("${document.excel.mimeType}")
    private String excelMimeType;

    //TODO private final DocumentsManagerMsgSender msgSender;
    private final QuestionsFromExcelParser parser;
    private final DocumentDownloader downloader;

    public DocumentsManager(QuestionsFromExcelParser parser, DocumentDownloader downloader) {
        this.parser = parser;
        this.downloader = downloader;
    }

    /**
     * Принимает апдейт с документом для добавления с него вопросов,
     * проверяет, что он пришел от админа в админском чате и является
     * файлом Excel, пробует скачать его с сервера используя {@link DocumentDownloader}
     * и пробрасывает скачанный файл в {@link QuestionsFromExcelParser}
     * @param update апдейт с документом
     */
    public void manageDocument(Update update) {
        if (validate(update)) {
            parser.parse(downloader.downloadDocument(update.message().document()));
            //TODO msgSender.successfulAddQuestions();
        } else {
            //TODO msgSender.wrongDocumentType();
        }
    }

    private boolean validate(Update update) {
        return isFromAdmin(update.message().from())
                && isChatAdmins(update.message().chat())
                && isExcelFile(update.message().document());
    }

    private boolean isFromAdmin(User user) {
        //TODO проверка на админа
        return false;
    }

    private boolean isChatAdmins(Chat chat) {
        //TODO проверка чата на админский
        return false;
    }

    private boolean isExcelFile(Document doc) {
        return doc.mimeType()
                .equals(excelMimeType);
    }
}
