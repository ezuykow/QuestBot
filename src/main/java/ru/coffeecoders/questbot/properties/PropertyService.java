package ru.coffeecoders.questbot.properties;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author ezuykow
 */
@Service
public class PropertyService {

    private final PropertyRepository repository;
    private final Map<String, PropertySeed> properties;

    public PropertyService(PropertyRepository repository, Map<String, PropertySeed> properties) {
        this.repository = repository;
        this.properties = properties;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public void fillProperties() {
        repository.findAll().forEach(row ->
                properties.put(
                        row.getKey(),
                        new PropertySeed(
                                row.getDescription(),
                                row.getActualProperty().replace("\\n", "\n"),
                                row.getDefaultProperty().replace("\\n", "\n")
                        )
                )
        );
    }

    /**
     * @author ezuykow
     */
    public int getDefaultPageSize() {
        return Integer.parseInt(properties.get("viewer.questions.page.size").getActualProperty());
    }

    //-----------------API END-----------------

}
