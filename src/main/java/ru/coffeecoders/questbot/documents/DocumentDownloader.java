package ru.coffeecoders.questbot.documents;

import com.pengrad.telegrambot.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author ezuykow
 */
@Component
public class DocumentDownloader {

    Logger logger = LoggerFactory.getLogger(DocumentDownloader.class);

    /**
     * Принимает документ из {@link com.pengrad.telegrambot.model.Update},
     * создает временный файл в файловой системе (удаляет его при остановке JVM),
     * в который пытается скачать документ с серверов Telegram
     * @param document документ из апдейта. Не может быть {@code NULL}
     * @return временный файл, эквивалентный файлу на серверах Telegram
     */
    public File downloadDocument(Document document) {
        try {
            return null; //TODO !!!
        } /*catch (IOException e) {
            logger.error("Не получилось стянуть файл \uD83E\uDD74");
            throw new RuntimeException(e);
        }*/
        finally { //TODO remove!
            return null;
        }
    }

    private File downloadFile(/*URL fileURL, File tempFile*/) {
        return null; //TODO !!!
    }
}
