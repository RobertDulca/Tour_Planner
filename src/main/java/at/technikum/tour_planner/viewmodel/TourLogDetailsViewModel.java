package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.beans.property.*;

import java.time.LocalDate;

public class TourLogDetailsViewModel {

    private final LongProperty tourLogID = new SimpleLongProperty();

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private final StringProperty comment = new SimpleStringProperty();

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    private final DoubleProperty totalTime = new SimpleDoubleProperty();

    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final Publisher publisher;
    private final TourLogOverviewService tourLogOverviewService;

    private void updateTourLogDetails(Object tourLogDetails) {

    }

    public TourLogDetailsViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        publisher.subscribe(Event.TOUR_LOG_SELECTED, this::updateTourLogDetails);
    }

    private TourLogModel newTourLogModel(){
        TourLogModel tourLogModel = new TourLogModel();
        tourLogModel.setDate(date.get());
        tourLogModel.setComment(comment.get());
        tourLogModel.setDifficulty(difficulty.get());
        tourLogModel.setTotalTime(totalTime.get());
        tourLogModel.setRating(rating.get());

        return tourLogModel;
    }

    public void createTourLog(){
        TourLogModel newTourLog = newTourLogModel();
        tourLogOverviewService.add(newTourLog);
        publisher.publish(Event.TOUR_LOG_CREATED, newTourLog);
    }

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
