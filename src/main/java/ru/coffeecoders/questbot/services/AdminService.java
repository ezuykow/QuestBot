package ru.coffeecoders.questbot.services;

import org.jetbrains.annotations.NotNull;
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

    /**
     * @return
     * @author Anatoliy Shikin
     */
    public List<Admin> findAll() {
        List<Admin> list = repository.findAll();
        logger.info("Admins are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     * @param id
     * @return
     * @author Anatoliy Shikin
     */
    public Optional<Admin> findById(long id) {
        Optional<Admin> admin = repository.findById(id);
        logger.info("Admin with id = {} {} found", id, admin.isPresent() ? "" : "not");
        return admin;
    }

    /**
     * @param admin
     * @return
     * @author Anatoliy Shikin
     */
    public Admin save(Admin admin) {
        logger.info("Admin = {} has been saved", admin);
        return repository.save(admin);
    }

    /**
     * @author ezuykow
     */
    public void deleteAll(List<Admin> admins) {
        logger.info("Admins = {} has been deleted", admins);
        repository.deleteAll(admins);
    }

    /**
     * @author ezuykow
     */
    public Admin getOwner() {
        return findAll().stream().filter(Admin::isOwner).findAny()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Этого никогда, конечно, не будет, но...." +
                                        "типа владельца бота нет, лол"
                        )
                );
    }

    /**
     * @author ezuykow
     */
    public void deleteUselessAdmins() {
        logger.info("Admin = {} has been deleted", getListUselessAdmins());
        repository.deleteAll(getListUselessAdmins());
    }

    /**
     * @return
     * @author Anatoliy Shikin
     */
    //TODO вынес
    @NotNull
    private List<Admin> getListUselessAdmins() {
        return findAll().stream()
                .filter(admin -> !admin.isOwner() && admin.getAdminChats().isEmpty())
                .toList();
    }
}