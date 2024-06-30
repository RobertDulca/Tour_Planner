package at.technikum.tour_planner.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tour_log")
public class TourLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "comment")
    private String comment;

    @Column(name = "difficulty")
    private int difficulty;

    @Column(name = "total_time")
    private double totalTime;

    @Column(name = "total_distance")
    private double totalDistance;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    public TourLogModel() {
    }

    public TourLogModel(LocalDate date, String comment, int difficulty, double totalTime, double totalDistance, int rating) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.rating = rating;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getTotalDistance() {return totalDistance; }

    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }
}