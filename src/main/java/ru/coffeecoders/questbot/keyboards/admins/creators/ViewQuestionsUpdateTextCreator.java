package ru.coffeecoders.questbot.keyboards.admins.creators;

import ru.coffeecoders.questbot.entities.Question;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ViewQuestionsUpdateTextCreator {
    private static TreeMap<Integer, Question> questionMap = new TreeMap<Integer, Question>();

    private static int pageCounter = 0;

    // TODO метод будет вызываться при вызове InlineKB

    public static TreeMap<Integer, Question> getQuestionsFromIndex(int pageCounter) {
        TreeMap<Integer, Question> result = new TreeMap<>();
        int i = 0;
        for (Map.Entry<Integer, Question> entry : questionMap.entrySet()) {
            if (i >= pageCounter && i < pageCounter + 5) {
                result.put(entry.getKey(), entry.getValue());
            }
            i++;
        }

        return result;
    }

    private void addQuestions(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            int questionNumber = questionMap.size() + 1;
            questionMap.put(questionNumber, questions.get(i));
        }
    }

}