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
    private TourLogModel tourLogModel;
    private final Publisher publisher;
    private final TourLogOverviewService tourLogOverviewService;

    private void updateTourLogDetails(Object tourLogDetails) {
        if (tourLogDetails instanceof TourLogModel) {
            TourLogModel log = (TourLogModel) tourLogDetails;
            tourLogModel = log;
            tourLogID.set(log.getTourLogId());
            date.set(log.getDate());
            comment.set(log.getComment());
            difficulty.set(log.getDifficulty());
            totalTime.set(log.getTotalTime());
            rating.set(log.getRating());
        }
    }

    public TourLogDetailsViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        publisher.subscribe(Event.TOUR_LOG_SELECTED, this::updateTourLogDetails);
    }

    public void saveTourLog(){
        tourLogOverviewService.add(tourLogModel);
        publisher.publish(Event.TOUR_LOG_SAVED, tourLogModel);
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
