package at.technikum.tour_planner.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty from;
    private final StringProperty to;

    public Tour(String name, String description, String origin, String destination) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.from = new SimpleStringProperty(origin);
        this.to = new SimpleStringProperty(destination);
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty originProperty() {
        return from;
    }

    public StringProperty destinationProperty() {
        return to;
    }
}