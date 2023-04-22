package ru.coffeecoders.questbot.validators;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Component
public class QuestionsValidator {

    private final QuestionService questionService;
    private final QuestionGroupService questionGroupService;

    public QuestionsValidator(QuestionService questionService, QuestionGroupService questionGroupService) {
        this.questionService = questionService;
        this.questionGroupService = questionGroupService;
    }

    public boolean isRequestedQuestionCountNotMoreThanWeHaveByGroups(int requestedCount, int[] groupsIds) {
        Set<String> targetGroupsNames = questionGroupService.findAll().stream()
                .filter(g -> ArrayUtils.contains(groupsIds, g.getGroupId()))
                .map(QuestionGroup::getGroupName)
                .collect(Collectors.toSet());
        long count = questionService.findAll().stream()
                .filter(q -> targetGroupsNames.contains(q.getGroup()))
                .count();
        return requestedCount <= count;
    }
}
