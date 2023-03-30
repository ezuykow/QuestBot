package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

@Service
public class QuestionGroupService {
    private final QuestionGroupRepository questionGroupRepository;

    public QuestionGroupService(QuestionGroupRepository questionGroupRepository) {
        this.questionGroupRepository = questionGroupRepository;
    }
}
