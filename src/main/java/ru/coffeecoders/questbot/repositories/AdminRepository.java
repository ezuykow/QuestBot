package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Admin;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
