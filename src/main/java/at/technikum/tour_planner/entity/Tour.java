package at.technikum.tour_planner.entity;

import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

@Entity
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private  String name;
    @Column(name = "description")
    private  String description;
    @Column(name = "from")
    private  String from;
    @Column(name = "to")
    private  String to;
    @Column(name = "transportType")
    private  String transportType;
    @Column(name = "imageUrl")
    private  String imageUrl;

    public Tour() {}
    public Tour(String name, String description, String from, String to, String transportType, String imageUrl) {
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.imageUrl = imageUrl;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getOrigin() {
        return from;
    }
    public String getDestination() {
        return to;
    }
    public String getTransportType() {
        return transportType;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setOrigin(String origin) {
        this.from = origin;
    }
    public void setDestination(String destination) {
        this.to = destination;
    }
    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}