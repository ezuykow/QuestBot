package ru.coffeecoders.questbot.properties;

import java.util.Objects;

/**
 * @author ezuykow
 */
public class PropertySeed {

    private final String description;
    private String actualProperty;
    private final String defaultProperty;

    public PropertySeed(String description, String actualProperty, String defaultProperty) {
        this.description = description;
        this.actualProperty = actualProperty;
        this.defaultProperty = defaultProperty;
    }

    public String getDescription() {
        return description;
    }

    public String getActualProperty() {
        return actualProperty;
    }

    public void setActualProperty(String actualProperty) {
        this.actualProperty = actualProperty;
    }

    public String getDefaultProperty() {
        return defaultProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertySeed that = (PropertySeed) o;
        return Objects.equals(description, that.description) && Objects.equals(actualProperty, that.actualProperty) && Objects.equals(defaultProperty, that.defaultProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, actualProperty, defaultProperty);
    }

    @Override
    public String toString() {
        return "PropertySeed{" +
                "description='" + description + '\'' +
                ", actualProperty='" + actualProperty + '\'' +
                ", defaultProperty='" + defaultProperty + '\'' +
                '}';
    }
}
