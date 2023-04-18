package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.repositories.AdminRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public List<Admin> findAll() {
        List<Admin> list = repository.findAll();
        logger.info("Admins {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Admin> findById(long id) {
        Optional<Admin> admin = repository.findById(id);
        logger.info("Admin {} with id = {}", admin.isPresent() ? "found" : "not found", id);
        return admin;
    }

    public Admin save(Admin admin) {
        logger.info("Admin = {} has been saved", admin);
        return repository.save(admin);
    }
}