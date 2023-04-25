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
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.QuestionGroupService;
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
    private final QuestionGroupService questionGroupService;
    private final Environment env;

    private List<Question> newQuestions;
    boolean blankQuestionsPresent;

    public QuestionsFromExcelParser(MessageSender msgSender, QuestionService questionService, QuestionGroupService questionGroupService, Environment env) {
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.questionGroupService = questionGroupService;
        this.env = env;
    }

    //-----------------API START-----------------

    /**
     * Принимает excel-файл, вытаскивает из него вопросы.
     * Если вопросы есть, передает их в {@link QuestionService}
     * для сохранения в БД
     * @param excelFile excel-файл. Не может быть {@code NULL}
     * @author ezuykow
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
        } finally {
            deleteTempFile(excelFile);
        }

    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void searchQuestionsInSheet(Sheet sheet) {
        StreamSupport.stream(((Iterable<Row>) sheet).spliterator(), false) //Разбираю страницу на ряды
                .skip(15) //Пропускаю первые ряды (правила и шапка таблицы)
                .forEach(this::searchQuestionInRow);
    }

    /**
     * @author ezuykow
     */
    private void searchQuestionInRow(Row row) {
        Optional<Question> questionOpt = searchQuestionFieldsInRowCells(row, new Question());
        questionOpt.ifPresent(newQuestions::add);
    }

    /**
     * @author ezuykow
     */
    private Optional<Question> searchQuestionFieldsInRowCells(Row row, Question newQuestion) {
        int cellNo = 1;
        for (Cell cell : row) {
            if (cell.getCellType() == CellType.STRING) {
                if ((cellNo == 1) && !(cell.getStringCellValue().equalsIgnoreCase("нет"))) {
                    return Optional.empty(); //Если в таблице не стоит, что этот вопрос не добавлялся ("нет")
                }
                fillQuestionFieldFromCell(cellNo++, cell, newQuestion);
            }
        }
        return validateAndReturnNewQuestion(newQuestion);
    }

    /**
     * @author ezuykow
     */
    private void fillQuestionFieldFromCell(int cellNo, Cell cell, Question newQuestion) {
        switch (cellNo) {
            case 2 -> newQuestion.setQuestion(cell.getStringCellValue().trim());
            case 3 -> newQuestion.setAnswerFormat(cell.getStringCellValue().trim());
            case 4 -> newQuestion.setAnswer(cell.getStringCellValue().trim());
            case 5 -> newQuestion.setMapUrl(cell.getStringCellValue().trim());
            case 6 -> newQuestion.setGroup(cell.getStringCellValue().trim());
        }
    }

    /**
     * @author ezuykow
     */
    private Optional<Question> validateAndReturnNewQuestion(Question newQuestion) {
        if (newQuestion.getQuestion() == null || newQuestion.getAnswer() == null) {
            blankQuestionsPresent = true;
            return Optional.empty();
        }
        if (newQuestion.getMapUrl().equalsIgnoreCase("нет")) {
            newQuestion.setMapUrl(null);
        }
        if (newQuestion.getGroup().equalsIgnoreCase("нет")) {
            newQuestion.setGroup(env.getProperty("messages.documents.defaultQuestionGroup"));
        }
        String group = newQuestion.getGroup();
        questionGroupService.findByGroupName(group).ifPresentOrElse(g -> {},
                () -> questionGroupService.save(new QuestionGroup(group)));
        return Optional.of(newQuestion);
    }

    /**
     * @author ezuykow
     */
    private void saveQuestionsIfPresent(long chatId) {
        if (newQuestions.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.documents.emptyQuestionList"));
        } else {
            StringBuilder msgSB = checkBlankAndRemoveEqualsQuestions();
            questionService.saveAll(newQuestions);
            msgSender.send(chatId, createNewQuestionsMsg(msgSB));
        }
    }

    /**
     * @author ezuykow
     */
    private String createNewQuestionsMsg(StringBuilder sb) {
        if (newQuestions.isEmpty()) {
            sb.append(env.getProperty("messages.documents.noOneQuestionAdded"));
        } else {
            sb.append(String.format(env.getProperty("messages.documents.questionsAdded", "%d"),
                    newQuestions.size())
            );
            newQuestions.forEach(question -> sb.append(Character.toString(0x2714)).append(" ")
                    .append(question.getQuestion()).append("\n")
            );
        }
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void deleteTempFile(File tempFile) {
        try {
            if (tempFile.delete()) {
                logger.warn("Temp excel-file was deleted");
            } else {
                logger.warn("Temp excel-file was not deleted");
            }
        } catch (SecurityException e) {
            logger.error("Security Manager blocked deleting of temp file!");
        }
    }

    /**
     * @author ezuykow
     */
    private StringBuilder checkBlankAndRemoveEqualsQuestions() {
        StringBuilder msgSB = new StringBuilder();
        if (blankQuestionsPresent) {
            msgSB.append(env.getProperty("messages.documents.emptyQuestionsNotAdded"));
        }
        if (findAndRemoveEqualsQuestions()) {
            msgSB.append(env.getProperty("messages.documents.equalsQuestionsNotAdded"));
        }
        return msgSB;
    }

    /**
     * @author ezuykow
     */
    private boolean findAndRemoveEqualsQuestions() {
        boolean hasEquals = false;
        List<String> questionsText = questionService.findAll().
                stream().map(Question::getQuestion).toList();
        for (int i = 0; i < newQuestions.size(); i++) {
            if (questionsText.contains(newQuestions.get(i).getQuestion())) {
                newQuestions.remove(i--);
                hasEquals = true;
            }
        }
        return hasEquals;
    }
}
