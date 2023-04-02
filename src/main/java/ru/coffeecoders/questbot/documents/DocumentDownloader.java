package ru.coffeecoders.questbot.documents;

import com.pengrad.telegrambot.model.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author ezuykow
 */
@Component
public class DocumentDownloader {

    Logger logger = LoggerFactory.getLogger(DocumentDownloader.class);

    @Value("${telegram.bot.api.prefix}")
    private String botApiUrlPrefix;
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.api.method.getFile}")
    private String apiGetFileMethod;
    @Value("${telegram.file.api.prefix}")
    private String fileApiPrefix;
    @Value("${document.temp.file.prefix}")
    private String tempFileNamePrefix;
    @Value("${document.temp.file.suffix}")
    private String tempFileNameSuffix;

    /**
     * Принимает документ из {@link com.pengrad.telegrambot.model.Update},
     * создает временный файл в файловой системе (не забудьте его удалить),
     * в который пытается скачать документ с серверов Telegram
     * @param doc документ из апдейта. Не может быть {@code NULL}
     * @return временный файл, эквивалентный файлу на серверах Telegram
     */
    public File downloadDocument(Document doc) {
        try {
            return downloadFile(fileUrl(doc), newTempFile());
        } catch (IOException e) {
            logger.error("Ошибка загрузки файла \uD83E\uDD74");
            throw new RuntimeException(e);
        }
    }

    private File downloadFile(URL fileURL, File tempFile) throws IOException {
        try (ReadableByteChannel rbc = Channels.newChannel(fileURL.openStream());
             FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return tempFile;
        }
    }

    private URL fileUrl(Document doc) throws IOException {
        return new URL(fileApiPrefix + botToken + "/" + getFilePath(doc));
    }

    private String getFilePath(Document doc) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(fileInfoUrl(doc).openStream()))) {
            return new JSONObject(in.readLine()).getJSONObject("result").getString("file_path");
        } catch (JSONException e) {
            logger.error("Вернулся кривой JSON при запросе информации о документе");
            throw new RuntimeException(e);
        }
    }

    private URL fileInfoUrl(Document doc) throws IOException {
        return new URL(botApiUrlPrefix + botToken + apiGetFileMethod + doc.fileId());
    }

    private File newTempFile() throws IOException {
        return File.createTempFile(tempFileNamePrefix, tempFileNameSuffix);
    }
}
