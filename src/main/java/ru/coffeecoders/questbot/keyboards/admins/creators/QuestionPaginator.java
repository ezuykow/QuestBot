package ru.coffeecoders.questbot.keyboards.admins.creators;

import ru.coffeecoders.questbot.entities.Question;

import java.util.*;

public class QuestionPaginator {
    private static List<Question> questionsList = new ArrayList<>();
    private static Map<Integer, List<Integer>> parameters = new HashMap<>();

    public static void setQuestionsList(List<Question> questionsList) {
        QuestionPaginator.questionsList = new ArrayList<>(questionsList);
    }

    public static void setParameters(int messageId, int firstItem, int numberOfItem) {
        parameters.put(messageId, Arrays.asList(firstItem, numberOfItem));
    }

    public static List<Question> getPaginatedQuestionList(int messageId) {
        List<Integer> userParams = parameters.getOrDefault(messageId, Arrays.asList(0, 1));
        int firstItem = userParams.get(0);
        int numberOfItem = userParams.get(1);

        if (firstItem < 0 || numberOfItem <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }

        int size = questionsList.size();
        int lastItem = Math.min(firstItem + numberOfItem, size);

        return questionPaginator(firstItem, lastItem);
    }

    public static String getPaginatedQuestionString(List<Question> paginatedQuestions) {
        StringBuilder sb = new StringBuilder();
        for (Question q : paginatedQuestions) {
            sb.append(q.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    private static List<Question> questionPaginator(int firstItem, int lastItem) {
        int size = questionsList.size();
        if (firstItem >= size || firstItem >= lastItem) {
            return Collections.emptyList();
        }
        if (lastItem > size) {
            lastItem = size;
        }
        return new ArrayList<>(questionsList.subList(firstItem, lastItem));
    }

    public static String updateQuestions(int messageId) {
        List<Question> paginatedQuestions = getPaginatedQuestionList(messageId);
        String text = getPaginatedQuestionString(paginatedQuestions);

        return text;
    }
}