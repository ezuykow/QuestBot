package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Update;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.documents.DocumentDownloader;
import ru.coffeecoders.questbot.documents.QuestionsFromExcelParser;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.msg.senders.MessageSender;
import ru.coffeecoders.questbot.validators.ChatAndUserIdValidator;

/**
 * @author ezuykow
 */
@Component
public class DocumentsManager {

    private final MessageSender msgSender;
    private final QuestionsFromExcelParser parser;
    private final DocumentDownloader downloader;
    private final ChatAndUserIdValidator validator;
    private final Environment env;

    public DocumentsManager(MessageSender msgSender, QuestionsFromExcelParser parser, DocumentDownloader downloader, ChatAndUserIdValidator validator, Environment env) {
        this.msgSender = msgSender;
        this.parser = parser;
        this.downloader = downloader;
        this.validator = validator;
        this.env = env;
    }

    /**
     * Принимает апдейт с документом для добавления с него вопросов,
     * проверяет, что он пришел от админа в админском чате и является
     * файлом Excel, пробует скачать его с сервера используя {@link DocumentDownloader}
     * и пробрасывает скачанный файл в {@link QuestionsFromExcelParser}
     * @param update апдейт с документом
     */
    public void manageDocument(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);
        long chatId = exUpdate.getMessageChatId();

        if (validate(exUpdate)) {
            parser.parse(downloader.downloadDocument(exUpdate.getDocument()), chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.documents.wrongDocumentType"));
        }
    }

    private boolean validate(ExtendedUpdate exUpdate) {
        return validator.isAdmin(exUpdate.getMessageFromUserId())
                && validator.isAdminChat(exUpdate.getMessageChatId())
                && isExcelFile(exUpdate.getDocument());
    }

    private boolean isExcelFile(Document doc) {
        return doc.mimeType()
                .equals(env.getProperty("document.excel.mimeType"));
    }
}