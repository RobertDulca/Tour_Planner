package at.technikum.tour_planner.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty from;
    private final StringProperty to;
    private final StringProperty transportType;
    private final StringProperty imageUrl;
    private final DoubleProperty distance;
    private final DoubleProperty estimatedTime;

    public Tour(String name, String description, String from, String to, String transportType, String imageUrl) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.transportType = new SimpleStringProperty(transportType);
        this.imageUrl = new SimpleStringProperty(imageUrl);
        this.distance = new SimpleDoubleProperty(0);
        this.estimatedTime = new SimpleDoubleProperty(0);
    }

    //Getters
    public String getName() {
        return name.get();
    }
    public String getDescription() {
        return description.get();
    }
    public String getOrigin() {
        return from.get();
    }
    public String getDestination() {
        return to.get();
    }
    public String getTransportType() {
        return transportType.get();
    }
    public String getImageUrl() {
        return imageUrl.get();
    }
    public double getDistance() {
        return distance.get();
    }
    public double getEstimatedTime() {
        return estimatedTime.get();
    }

    //Setters
    public void setName(String name) {
        this.name.set(name);
    }
    public void setDescription(String description) {
        this.description.set(description);
    }
    public void setOrigin(String origin) {
        this.from.set(origin);
    }
    public void setDestination(String destination) {
        this.to.set(destination);
    }
    public void setTransportType(String transportType) {
        this.transportType.set(transportType);
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
    public void setDistance(double distance) {
        this.distance.set(distance);
    }
    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime.set(estimatedTime);
    }
}