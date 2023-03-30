package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.allQuestions;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class AllQuestionKeyboardMetods {

    public enum allQuestionsCommands{DELETE_FROM_BANK, EDITE_FROM_BANK}

    private List<Question> allQuestions = new ArrayList<>();
    private List<List<Question>> paginatedQuestions = new ArrayList<>();
    private int currentPage = 0;

    public void resizeQuestionsList() {
        paginatedQuestions = QuestionService.allQuestionsBySix(allQuestions);
    }

    //TODO 2 создание первой клавиатуры class AllQuestionsKeyboardCreator+
    // TODO 2.1 создание второй клавиатуры  class AllQuestionsKeyboardRECreator +

    //TODO 3 пагинация allQuestionsBySix(List<Question> allQuestion) -> List<List<Question>> paginatedQuestions


    public List<List<Question>>allQuestionsBySix(List<Question> allQuestion) {
        allQuestions = QuestionRepository.getAllQuestion();
        List<List<Question>> paginatedQuestions = new ArrayList<>();
        int page = 0;
        while (page * 6 < allQuestions.size()) {
            int fromIndex = page * 6;
            int toIndex = Math.min((page + 1) * 6, allQuestions.size());
            paginatedQuestions.add(allQuestions.subList(fromIndex, toIndex));
            page++;
        }

        return paginatedQuestions;
    }



    // TODO 4 метод замены сообщения - перенести
    public void editMessage(long chatId, int messageId, String newText) {
        messageSender.editerMes(new EditMessageText(chatId, messageId, newText));
    }
    public void editerMes(long chatId, int messageId, String newText) {
        telegramBot.execute(new EditMessageText(chatId, messageId, newText));
    }

    // TODO 4.1 метод замены сообщения уточнить что будем передавать (текст должен быть из свича или метода)
    // / уточнить что будет если апдэйт уже не существует...

    // TODO 5 метод замены клавиатуры

    public void switchKeyboard(Update update, InlineKeyboardMarkup newKeyboard) {
        Message message = update.getMessage();
        if (message != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(message.getText());
            sendMessage.setReplyMarkup(newKeyboard);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    //


    //  TODO метод удаления сущности из листа
    public void deleteQuestion(List<List<Question>> paginatedQuestions, int pageNumber, int questionNumber) {
        int index = (pageNumber - 1) * 6 + questionNumber - 1;
        if (index >= 0 && index < allQuestions.size()) {
            paginatedQuestions.get(pageNumber - 1).get(questionNumber - 1).setDeleted(true);
            QuestionRepository.deleteQuestion(paginatedQuestions.get(pageNumber - 1).get(questionNumber - 1));
        }
    }
    // TODO метод изменения сущности из листа
    public void editQuestion(List<List<Question>> paginatedQuestions, int pageNumber, int questionNumber, String newQuestionText) {
        int index = (pageNumber - 1) * 6 + questionNumber - 1;
        if (index >= 0 && index < paginatedQuestions.size()) {
            Question questionToEdit = paginatedQuestions.get(pageNumber - 1).get(questionNumber - 1);
            questionToEdit.setQuestion(newQuestionText);
            // Дополнительно, если нужно сохранить изменения в базу данных:
            QuestionRepository.updateQuestion(questionToEdit);
        }
    }

}
