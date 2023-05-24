package ru.coffeecoders.questbot.properties.viewer;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.properties.PropertyRow;
import ru.coffeecoders.questbot.properties.PropertySeed;
import ru.coffeecoders.questbot.properties.PropertyService;

import java.util.List;
import java.util.Map;

import static java.lang.Math.min;

/**
 * @author ezuykow
 */
@Component
public class PropertiesViewer {

    private static final int DEFAULT_PAGE_SIZE = 5;

    private final Map<String, PropertySeed> propertiesMap;
    private final PropertyService propertyService;
    private final MessageSender msgSender;
    private final RestrictingManager restrictingManager;
    private final BlockingManager blockingManager;

    private List<PropertyRow> propertiesList;
    private int lastShowedFirstIndex;
    private int PropertiesViewMsgId;

    public PropertiesViewer(Map<String, PropertySeed> propertiesMap, PropertyService propertyService, MessageSender msgSender, RestrictingManager restrictingManager, BlockingManager blockingManager) {
        this.propertiesMap = propertiesMap;
        this.propertyService = propertyService;
        this.msgSender = msgSender;
        this.restrictingManager = restrictingManager;
        this.blockingManager = blockingManager;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public void viewProperties(long chatId) {
        refreshPropertiesList();
        createView(chatId, -1, DEFAULT_PAGE_SIZE, 0);
    }

    /**
     * @author ezuykow
     */
    public void switchPageToPrevious(long chatId, int msgId, String data) {
        final int firstIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        PropertiesViewerPage newPage = createPage(DEFAULT_PAGE_SIZE, firstIndexShowed - DEFAULT_PAGE_SIZE);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * @author ezuykow
     */
    public void switchPageToNext(long chatId, int msgId, String data) {
        final int lastIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        final int newPageSize = min(DEFAULT_PAGE_SIZE, propertiesList.size() - (lastIndexShowed + 1));
        PropertiesViewerPage newPage = createPage(newPageSize, lastIndexShowed + 1);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * @author ezuykow
     */
    public void showPropertyInfo(long chatId, int msgId, String data) {
        String[] parts = data.split("\\.");

        int targetPropertyIdx;
        if (parts[0].equals("Edited")) {
            targetPropertyIdx = Integer.parseInt(parts[1]);
        } else {
            lastShowedFirstIndex = Integer.parseInt(parts[parts.length - 1]);
            targetPropertyIdx = Integer.parseInt(parts[2]);
        }
        PropertyInfoPage page = PropertyInfoPage.createPage(propertiesList.get(targetPropertyIdx), targetPropertyIdx);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    /**
     * @author ezuykow
     */
    public void backFromPropertyInfo(long chatId, int msgId) {
        refreshPropertiesList();
        int pageSize = min(DEFAULT_PAGE_SIZE, propertiesList.size() - lastShowedFirstIndex);
        createView(chatId, msgId, pageSize, lastShowedFirstIndex);
    }

    /**
     * @author ezuykow
     */
    public void deleteView(long chatId, int msgId) {
        unblockAndUnrestrictChat(chatId);
        msgSender.sendDelete(chatId, msgId);
        PropertiesViewMsgId = 0;
    }

    /**
     * @author ezuykow
     */
    public void editProperty(long chatId, int propertyId) {
        msgSender.sendForceReply(chatId,
                "В ответ на это сообщение введите новое значение параметра " + propertyId);
    }

    /**
     * @author ezuykow
     */
    public void performEditProperty(ExtendedUpdate update, String messageText) {
        String replyText = update.getReplyToMessage().text();
        int propertyId = Integer.parseInt(replyText.substring(replyText.lastIndexOf(" ") + 1));
        confirmEdit(propertyId, messageText);
        endEdit(propertyId, update.getMessageChatId(), update.getMessageId(), update.getReplyToMessage().messageId());
    }

    /**
     * @author ezuykow
     */
    public void restoreValue(long chatId, int propertyId) {
        confirmEdit(propertyId, null);
        showPropertyInfo(chatId, PropertiesViewMsgId, "Edited." + propertyId);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void refreshPropertiesList() {
        propertiesList = propertiesMap.entrySet().stream().map(e ->
                new PropertyRow(
                        e.getKey(),
                        e.getValue().getDescription(),
                        e.getValue().getActualProperty(),
                        e.getValue().getDefaultProperty()
                ))
                .toList();
    }

    /**
     * @author ezuykow
     */
    private void createView(long chatId, int msgId, int pageSize, int startIndex) {
        PropertiesViewerPage page = createPage(pageSize, startIndex);
        if (msgId == -1) {
            PropertiesViewMsgId = msgSender.send(chatId, page.getText(), page.getKeyboard());
        } else {
            msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
        }
    }

    /**
     * @author ezuykow
     */
    private PropertiesViewerPage createPage(int pageSize, int startIndex) {
        int pagesCount = propertiesList.size() / DEFAULT_PAGE_SIZE;
        pagesCount = (propertiesList.size() % DEFAULT_PAGE_SIZE == 0) ? pagesCount : pagesCount + 1;
        return PropertiesViewerPage.createPage(propertiesList, pageSize, startIndex, pagesCount, DEFAULT_PAGE_SIZE);
    }

    /**
     * @author ezuykow
     */
    private void unblockAndUnrestrictChat(long chatId) {
        restrictingManager.unRestrictMembers(chatId);
        blockingManager.unblockAdminChat(chatId, "Владелец закончил работать с параметрами");
    }

    /**
     * @author ezuykow
     */
    private void confirmEdit(int propertyId, String messageText) {
        PropertyRow targetProperty = propertiesList.get(propertyId);

        if (messageText == null) {
            messageText = targetProperty.getDefaultProperty();
        }

        PropertySeed editedProperty = new PropertySeed(
                targetProperty.getDescription(),
                messageText,
                targetProperty.getDefaultProperty()
        );
        propertiesMap.put(targetProperty.getKey(), editedProperty);
        propertyService.setActualByKey(messageText, targetProperty.getKey());
        refreshPropertiesList();
    }

    /**
     * @author ezuykow
     */
    private void endEdit(int propertyId, long messageChatId, int messageId, int replyMessageId) {
        msgSender.sendDelete(messageChatId, messageId);
        msgSender.sendDelete(messageChatId, replyMessageId);
        showPropertyInfo(messageChatId, PropertiesViewMsgId, "Edited." + propertyId);
    }
}
