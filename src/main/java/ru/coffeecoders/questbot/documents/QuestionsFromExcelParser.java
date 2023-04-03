package ru.coffeecoders.questbot.documents;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
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

    //TODO private final QuestionsFromExcelParserMsgSender msgSender;
    private final QuestionService questionService;

    private List<Question> newQuestions;
    boolean blankQuestionsPresent;

    public QuestionsFromExcelParser(QuestionService questionService) {
        this.questionService = questionService;
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
            //TODO проверка есть ли вопросы
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
            case 2 -> newQuestion.setQuestion(cell.getStringCellValue());
            case 3 -> newQuestion.setAnswerFormat(cell.getStringCellValue());
            case 4 -> newQuestion.setAnswer(cell.getStringCellValue());
            case 5 -> newQuestion.setMapUrl(cell.getStringCellValue());
            case 6 -> newQuestion.setGroup(cell.getStringCellValue());
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
        if (blankQuestionsPresent) {
            //TODO msgSender.blankQuestionInTable(chatId) "Не забывайте обязательно заполнять текст вопроса и ответ"
        }
        if (newQuestions.isEmpty()) {
            //TODO msgSender.emptyNewQuestionsList(chatId)
        } else {
            //TODO questionService.saveAll(newQuestions);
            //TODO msgSender.newQuestionsAdded(chatId, newQuestions);
        }
    }
}
