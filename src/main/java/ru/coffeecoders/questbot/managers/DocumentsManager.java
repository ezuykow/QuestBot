package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.documents.DocumentDownloader;
import ru.coffeecoders.questbot.documents.QuestionsFromExcelParser;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

/**
 * @author ezuykow
 */
@Component
public class DocumentsManager {

    @Value("${document.excel.mimeType}")
    private String excelMimeType;

    private final MessageSender msgSender;
    private final QuestionsFromExcelParser parser;
    private final DocumentDownloader downloader;
    private final ChatAndUserValidator validator;
    private final Messages messages;
    private final MessageBuilder messageBuilder;
    private final LogSender logger;

    public DocumentsManager(MessageSender msgSender, QuestionsFromExcelParser parser, DocumentDownloader downloader,
                            ChatAndUserValidator validator, Messages messages, MessageBuilder messageBuilder, LogSender logger) {
        this.msgSender = msgSender;
        this.parser = parser;
        this.downloader = downloader;
        this.validator = validator;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * Принимает апдейт с документом для добавления с него вопросов,
     * проверяет, что он пришел от админа в админском чате и является
     * файлом Excel, пробует скачать его с сервера используя {@link DocumentDownloader}
     * и пробрасывает скачанный файл в {@link QuestionsFromExcelParser}
     * @param update апдейт с документом
     * @author ezuykow
     */
    public void manageDocument(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();

        if (validator.isAdminChat(chatId) && validate(update)) {
            logger.warn("Добавляю вопросы с файла");
            deleteMessageWithNewQuestionsDocument(update);
            parser.parse(downloader.downloadDocument(update.getDocument()), chatId);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private boolean validate(ExtendedUpdate exUpdate) {
        if (validator.isAdminChat(exUpdate.getMessageChatId())) {
            if (validator.isAdmin(exUpdate.getMessageFromUserId())) {
                if (isExcelFile(exUpdate.getDocument())) {
                    return true;
                } else {
                    msgSender.send(exUpdate.getMessageChatId(),
                            messageBuilder.build(messages.wrongDocumentType(), exUpdate.getMessageChatId()));
                }
            } else {
                msgSender.send(exUpdate.getMessageChatId(),
                        messageBuilder.build(messages.fromNotAdmin(), exUpdate.getMessageChatId()));
            }
        }
        return false;
    }

    /**
     * @author ezuykow
     */
    private boolean isExcelFile(Document doc) {
        return doc.mimeType().equals(excelMimeType);
    }

    /**
     * @author ezuykow
     */
    private void deleteMessageWithNewQuestionsDocument(ExtendedUpdate update) {
        msgSender.sendDelete(update.getMessageChatId(), update.getMessageId());
    }
}