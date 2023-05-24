package ru.coffeecoders.questbot.properties;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.properties.viewer.PropertiesViewer;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

/**
 * @author ezuykow
 */
@Component
public class PropertiesViewerCallbackManager {

    private enum Action {
        PREVIOUS_PAGE("PropertiesViewer.Switch page to previous.*"),
        NEXT_PAGE("PropertiesViewer.Switch page to next.*"),
        SHOW_PROPERTY("PropertiesViewer.Taken index.*"),
        DELETE_MESSAGE("PropertiesViewer.Delete message"),
        EDIT_PROPERTY("PropertiesViewer.Property info.Edit.*"),
        RESTORE("PropertiesViewer.Property info.Restore.*"),
        BACK_FROM_PROPERTY_INFO("PropertiesViewer.Property Info.Back"),
        UNKNOWN("");

        private final String dataRegexp;

        Action(String dataRegexp) {
            this.dataRegexp = dataRegexp;
        }
    }

    private final ChatAndUserValidator validator;
    private final PropertiesViewer propertiesViewer;

    public PropertiesViewerCallbackManager(ChatAndUserValidator validator, PropertiesViewer propertiesViewer) {
        this.validator = validator;
        this.propertiesViewer = propertiesViewer;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isOwner(senderUserId)) {
            performCallback(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    private void performCallback(long chatId, int msgId, String data) {
        switch (findAction(data)) {
            case PREVIOUS_PAGE -> propertiesViewer.switchPageToPrevious(chatId, msgId, data);
            case NEXT_PAGE -> propertiesViewer.switchPageToNext(chatId, msgId, data);
            case SHOW_PROPERTY -> propertiesViewer.showPropertyInfo(chatId, msgId, data);
            case DELETE_MESSAGE -> propertiesViewer.deleteView(chatId, msgId);
            case EDIT_PROPERTY -> {
                int propertyId = parsePropertyId(data);
                propertiesViewer.editProperty(chatId, propertyId);
            }
            case RESTORE -> {
                int propertyId = parsePropertyId(data);
                propertiesViewer.restoreValue(chatId, propertyId);
            }
            case BACK_FROM_PROPERTY_INFO -> propertiesViewer.backFromPropertyInfo(chatId, msgId);
            case UNKNOWN -> {} //Игнорируем неизвестный калбак
        }
    }

    private int parsePropertyId(String data) {
         return Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
    }

    private PropertiesViewerCallbackManager.Action findAction(String data) {
        for (PropertiesViewerCallbackManager.Action a : PropertiesViewerCallbackManager.Action.values()) {
            if (data.matches(a.dataRegexp)) {
                return a;
            }
        }
        return PropertiesViewerCallbackManager.Action.UNKNOWN;
    }
}
