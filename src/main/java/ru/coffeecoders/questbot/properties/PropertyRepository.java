package ru.coffeecoders.questbot.properties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ezuykow
 */
@Repository
public interface PropertyRepository extends JpaRepository<PropertyRow, String> {
}
