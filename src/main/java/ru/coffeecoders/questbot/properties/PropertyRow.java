package ru.coffeecoders.questbot.properties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "properties")
public class PropertyRow {

    @Id
    @Column(name = "key")
    private String key;

    @Column(name = "description")
    private String description;

    @Column(name = "actual_property")
    private String actualProperty;

    @Column(name = "default_property")
    private String defaultProperty;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setDefaultProperty(String defaultProperty) {
        this.defaultProperty = defaultProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyRow that = (PropertyRow) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "PropertyRow{" +
                "key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", actualProperty='" + actualProperty + '\'' +
                ", defaultProperty='" + defaultProperty + '\'' +
                '}';
    }
}
