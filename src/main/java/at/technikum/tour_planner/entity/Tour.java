package at.technikum.tour_planner.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty from;
    private final StringProperty to;
    private final StringProperty transportType;
    private final StringProperty imageUrl;
    private final DoubleProperty distance;
    private final DoubleProperty estimatedTime;

    @Column(name = "name")
    private  String name;
    @Column(name = "description")
    private  String description;
    @Column(name = "start")
    private  String start;
    @Column(name = "destination")
    private  String destination;
    @Column(name = "transportType")
    private  String transportType;
    @Column(name = "imageUrl")
    private  String imageUrl;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLogModel> tourLogs;

    public Tour() {}
    public Tour(String name, String description, String from, String to, String transportType, String imageUrl) {
        this.name = name;
        this.description = description;
        this.start = from;
        this.destination = to;
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
        return start;
    }

    public String getDestination() {
        return destination;
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
    public double getDistance() {
        return distance.get();
    }

    public double getEstimatedTime() {
        return estimatedTime.get();
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrigin(String origin) {
        this.start = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime.set(estimatedTime);
    }
}