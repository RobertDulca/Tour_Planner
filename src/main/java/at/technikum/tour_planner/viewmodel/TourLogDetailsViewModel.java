package at.technikum.tour_planner.viewmodel;

import javafx.beans.property.*;

import java.time.LocalDate;

public class TourLogDetailsViewModel {

    private final LongProperty tourLogID = new SimpleLongProperty();

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private final StringProperty comment = new SimpleStringProperty();

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    private final DoubleProperty totalTime = new SimpleDoubleProperty();

    private final IntegerProperty rating = new SimpleIntegerProperty();

    public LongProperty tourLogIDProperty() {
        return tourLogID;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public IntegerProperty difficultyProperty() {
        return difficulty;
    }

    public DoubleProperty totalTimeProperty() {
        return totalTime;
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }
}
