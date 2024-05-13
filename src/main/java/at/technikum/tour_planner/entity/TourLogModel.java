package at.technikum.tour_planner.entity;

import java.time.LocalDate;

public class TourLogModel {
    private long tourLogId;
    private LocalDate date;
    private String comment;
    private int difficulty;
    private double totalTime;

    private int rating;

    public TourLogModel(TourLogModel term) {
        this.tourLogId = term.tourLogId;
        this.date = term.date;
        this.comment = term.comment;
        this.difficulty = term.difficulty;
        this.totalTime = term.totalTime;
        this.rating = term.rating;
    }

    public long getTourLogId() {
        return tourLogId;
    }

    public void setTourLogId(long tourLogId) {
        this.tourLogId = tourLogId;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
