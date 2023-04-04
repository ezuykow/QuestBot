package ru.coffeecoders.questbot.msg.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class HeaderKeyboardSender {
    private final Logger logger = LoggerFactory.getLogger(HeaderKeyboardSender.class);

    private final Environment env;

    private final TelegramBot telegramBot;

    public HeaderKeyboardSender(Environment env, TelegramBot telegramBot){
        this.env = env;
        this.telegramBot = telegramBot;
    }

     public void sendTypeSelectMsg(long chatId){
         checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("keyboards.headers.typeChat"))));
     }

     public void sendActionSelectMsg(long chatId){
        checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("keyboards.headers.action"))));
     }

     public void sendActionQuestionMsg(long chatId){
         checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("keyboards.headers.actionForQuestion"))));
     }

     public void sendSelectNewAdminMsg(long chatId){
         checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("keyboards.headers.selectNewAdmin"))));
     }

     private SendMessage createSendMessage(long chatId, String msg) {return new SendMessage(chatId, msg); }

     private void checkResponse(SendResponse response) {
        if (!response.isOk()) {
            logger.warn("Unsent msg! {}", response.message().text());
        }
     }
}
