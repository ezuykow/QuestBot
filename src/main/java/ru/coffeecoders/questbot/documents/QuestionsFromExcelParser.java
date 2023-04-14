package ru.coffeecoders.questbot.documents;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.QuestionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author ezuykow
 */
@Component
public class QuestionsFromExcelParser {

    Logger logger = LoggerFactory.getLogger(QuestionsFromExcelParser.class);

    private final MessageSender msgSender;
    private final QuestionService questionService;
    private final Environment env;

    private List<Question> newQuestions;
    boolean blankQuestionsPresent;

    public QuestionsFromExcelParser(MessageSender msgSender, QuestionService questionService, Environment env) {
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.env = env;
    }

    /**
     * Принимает excel-файл, вытаскивает из него вопросы.
     * Если вопросы есть, передает их в {@link QuestionService}
     * для сохранения в БД
     * @param excelFile excel-файл. Не может быть {@code NULL}
     */
    public void parse(File excelFile, long chatId) {
        try (XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(excelFile))) {
            newQuestions = new ArrayList<>();
            blankQuestionsPresent = false;
            searchQuestionsInSheet(workBook.getSheetAt(0)); //0 - берем только первую страницу
            saveQuestionsIfPresent(chatId);
        } catch (IOException e) {
            logger.error("Excel-файл не найден");
            throw new RuntimeException(e);
        }

    }

    private void searchQuestionsInSheet(Sheet sheet) {
        StreamSupport.stream(((Iterable<Row>) sheet).spliterator(), false) //Разбираю страницу на ряды
                .skip(1) //Пропускаю первый ряд (шапка таблицы)
                .forEach(this::searchQuestionInRow);
    }

    private void searchQuestionInRow(Row row) {
        Optional<Question> questionOpt = searchQuestionFieldsInRowCells(row, new Question());
        questionOpt.ifPresent(newQuestions::add);
    }

    private Optional<Question> searchQuestionFieldsInRowCells(Row row, Question newQuestion) {
        int cellNo = 1;
        for (Cell cell : row) {
            if (cell.getCellType() == CellType.STRING) {
                if ((cellNo == 1) && (cell.getStringCellValue().equalsIgnoreCase("да"))) {
                    return Optional.empty(); //Если в таблице стоит, что этот вопрос уже добавлялся ("да")
                }
                fillQuestionFieldFromCell(cellNo++, cell, newQuestion);
            }
        }
        return validatedNewQuestion(newQuestion);
    }

    private void fillQuestionFieldFromCell(int cellNo, Cell cell, Question newQuestion) {
        switch (cellNo) {
            case 2 -> newQuestion.setQuestion(cell.getStringCellValue().trim());
            case 3 -> newQuestion.setAnswerFormat(cell.getStringCellValue().trim());
            case 4 -> newQuestion.setAnswer(cell.getStringCellValue().trim());
            case 5 -> newQuestion.setMapUrl(cell.getStringCellValue().trim());
            case 6 -> newQuestion.setGroup(cell.getStringCellValue().trim());
        }
    }

    private Optional<Question> validatedNewQuestion(Question newQuestion) {
        if (newQuestion.getQuestion() == null || newQuestion.getAnswer() == null) {
            blankQuestionsPresent = true;
            return Optional.empty();
        }
        return Optional.of(newQuestion);
    }

    private void saveQuestionsIfPresent(long chatId) {
        StringBuilder msgSB = new StringBuilder();

        if (blankQuestionsPresent) {
            msgSB.append("Пустые вопросы не добавлены! (Без текста вопроса или ответа)\n\n");
        }

        if (newQuestions.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.documents.emptyQuestionList"));
        } else {
            //TODO questionService.saveAll(newQuestions);
            msgSender.send(chatId, createNewQuestionsMsg(msgSB));
        }
    }

    private String createNewQuestionsMsg(StringBuilder sb) {
        sb.append(String.format("Добавлены вопросы (%d):\n\n", newQuestions.size()));

        newQuestions.forEach(question -> {
            sb.append(Character.toString(0x2714)).append(" ")
                    .append(question.getQuestion()).append("\n");
        });
        return sb.toString();
    }
}
