//package ru.coffeecoders.questbot.keyboards.admins.creators;
//
//import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
//import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
//import org.springframework.stereotype.Component;
//import ru.coffeecoders.questbot.entities.Question;
//
//import java.util.Map;
//import java.util.TreeMap;
//@Component
//public class ViewQuestionsUpdateAssembly {
//
//    private static TreeMap<Integer, Question> questionMap = new TreeMap<Integer, Question>();
//    private static int pageCounter = 0;
//    private static int questionsInMap = 0;
//
//    public void refreshQuestions (){
//
//    }
//    public static String getQuestionsFromIndex() {
//        TreeMap<Integer, Question> result = new TreeMap<>();
//        int i = 0;
//        for (Map.Entry<Integer, Question> entry : questionMap.entrySet()) {
//            if (i >= pageCounter && i < pageCounter + 5) {
//                result.put(entry.getKey(), entry.getValue());
//            }
//            i++;
//        }
//        questionsInMap = questionMap.size();
//        String resultString = result.toString();
//        return resultString;
//    }
//
//
//
//
//    public static InlineKeyboardMarkup pager() {
//        InlineKeyboardMarkup keyboard = inlineKeyboardCreate();
//        pageCounter += 5;
//        return keyboard;
//    }
//
//    public static InlineKeyboardMarkup lastPager() {
//        pageCounter -= 10;
//        return pager();
//    }
//
//
//}