package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
}
