package ru.coffeecoders.questbot.documents;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
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


    private final MessageSender msgSender;
    private final QuestionService questionService;
    private final QuestionGroupService questionGroupService;
    private final Messages messages;
    private final MessageBuilder messageBuilder;
    private final LogSender logger;

    private List<Question> newQuestions;
    boolean blankQuestionsPresent;

    public QuestionsFromExcelParser(MessageSender msgSender, QuestionService questionService,
                                    QuestionGroupService questionGroupService, Messages messages,
                                    MessageBuilder messageBuilder, LogSender logger) {
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.questionGroupService = questionGroupService;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
        this.logger = logger;
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
            if (cell.getCellType() == CellType.NUMERIC && cellNo == 4) {
                fillQuestionFieldFromCell(cellNo++, cell, newQuestion);
            }
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
            case 4 -> {
                String answer;
                if (cell.getCellType() == CellType.STRING) {
                    answer = cell.getStringCellValue().trim();
                } else {
                    answer = String.valueOf((int) cell.getNumericCellValue()).trim();
                }
                newQuestion.setAnswer(answer);
            }
            case 5 -> newQuestion.setAdditional(cell.getStringCellValue().trim());
            case 6 -> newQuestion.setGroup(cell.getStringCellValue().trim());
        }
    }

    /**
     * @author ezuykow
     */
    private Optional<Question> validateAndReturnNewQuestion(Question newQuestion) {
        if (newQuestion.getQuestion() == null || newQuestion.getQuestion().isBlank()
                || newQuestion.getAnswer() == null || newQuestion.getAnswer().isBlank()) {
            blankQuestionsPresent = true;
            return Optional.empty();
        }
        if (newQuestion.getAnswerFormat().equalsIgnoreCase("нет")) {
            newQuestion.setAnswerFormat(null);
        }
        if (newQuestion.getAdditional().equalsIgnoreCase("нет")) {
            newQuestion.setAdditional(null);
        }
        if (newQuestion.getGroup().equalsIgnoreCase("нет")) {
            newQuestion.setGroup(messages.defaultQuestionGroup());
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
            msgSender.send(chatId,
                    messageBuilder.build(messages.emptyQuestionList(), chatId));
        } else {
            StringBuilder msgSB = checkBlankAndRemoveEqualsQuestions();
            questionService.saveAll(newQuestions);
            msgSender.send(chatId,
                    messageBuilder.build(createNewQuestionsMsg(msgSB), chatId));
        }
    }

    /**
     * @author ezuykow
     */
    private String createNewQuestionsMsg(StringBuilder sb) {
        if (newQuestions.isEmpty()) {
            sb.append(messages.noOneQuestionAdded());
        } else {
            sb.append("(").append(newQuestions.size()).append(") ").append(messages.questionsAdded());
            for (Question q : newQuestions) {
                if (sb.length() <= 3000) {
                    sb.append(Character.toString(0x2714)).append(" ")
                            .append(q.getQuestion()).append("\n");
                } else {
                    sb.append("И так далее...");
                    break;
                }
            }
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
            msgSB.append(messages.emptyQuestionsNotAdded());
        }
        if (findAndRemoveEqualsQuestions()) {
            msgSB.append(messages.equalsQuestionsUpdated());
        }
        return msgSB;
    }

    /**
     * @author ezuykow
     */
    private boolean findAndRemoveEqualsQuestions() {
        boolean hasEquals = false;
        List<String> questionsText = questionService.getQuestionsTexts();
        for (Question newQuestion : newQuestions) {
            String text = newQuestion.getQuestion();
            if (questionsText.contains(text)) {
                Question oldQuestion = questionService.findByText(text);
                newQuestion.setQuestionId(oldQuestion.getQuestionId());
                newQuestion.setLastUsage(oldQuestion.getLastUsage());
                hasEquals = true;
            }
        }
        return hasEquals;
    }
}
