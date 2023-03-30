package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.request.EditMessageText;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class AllQuestionKeyboardMetods {

    private List<Question> allQuestions = new ArrayList<>();
    private List<List<Question>> paginatedQuestions = new ArrayList<>();
    private int currentPage = 0;

    public void resizeQuestionsList() {

        paginatedQuestions = QuestionService.allQuestionsBySix(allQuestions);
    }
    //TODO 1 Распарсить на поля для представления до попадания в лист
    //TODO 2 создание первой клавиатуры class AllQuestionsKeyboardCreator

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



    //TODO 4 метод замены сообщения - перенести
    public void editMessage(long chatId, int messageId, String newText) {
        messageSender.editerMes(new EditMessageText(chatId, messageId, newText));
    }
    public void editerMes(long chatId, int messageId, String newText) {
        telegramBot.execute(new EditMessageText(chatId, messageId, newText));
    }

    //TODO 4.1 метод замены сообщения уточнить что будем передавать (текст должен быть из свича или метода)
    // / уточнить что будет если апдэйт уже не существует

    //TODO 5 метод замены клавиатуры


}
