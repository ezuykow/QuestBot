package ru.coffeecoders.questbot.properties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ezuykow
 */
@Repository
public interface PropertyRepository extends JpaRepository<PropertyRow, String> {

    @Modifying
    @Query(value = "UPDATE properties SET actual_property=:newActual WHERE key=:key", nativeQuery = true)
    void setActualByKey(String newActual, String key);
}
