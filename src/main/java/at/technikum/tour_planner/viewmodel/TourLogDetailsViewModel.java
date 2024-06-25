package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.beans.property.*;

import java.time.LocalDate;

public class TourLogDetailsViewModel {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private final StringProperty comment = new SimpleStringProperty();

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    private final DoubleProperty totalTime = new SimpleDoubleProperty();

    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isEditButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final Publisher publisher;
    private final TourLogOverviewService tourLogOverviewService;
    private TourLogModel selectedTourLog;
    private Tour selectedTour;

    public TourLogDetailsViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        isAddButtonDisabled.bind(
                date.isNull()
                        .or(comment.isEmpty())
                        .or(difficulty.isEqualTo(0))
                        .or(totalTime.isEqualTo(0))
                        .or(rating.isEqualTo(0))
        );

        isEditButtonDisabled.bind(
                date.isNull()
                        .or(isTourSelected.not())
                        .or(comment.isEmpty())
                        .or(difficulty.isEqualTo(0))
                        .or(totalTime.isEqualTo(0))
                        .or(rating.isEqualTo(0))
        );

        publisher.subscribe(Event.TOUR_LOG_SELECTED, this::onTourLogSelected);
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            selectedTour = (Tour) message;
            isTourSelected.set(true);
        } else {
            selectedTour = null;
            isTourSelected.set(false);
        }
    }

    private void onTourLogSelected(Object message) {
        if (message instanceof TourLogModel) {
            setSelectedTourLog((TourLogModel) message);
        } else {
            clearTourLogDetails();
        }
    }

    private void setSelectedTourLog(TourLogModel tourLog) {
        selectedTourLog = tourLog;
        isTourSelected.set(true);
        if(tourLog != null) {
            date.set(tourLog.getDate());
            comment.set(tourLog.getComment());
            difficulty.set(tourLog.getDifficulty());
            totalTime.set(tourLog.getTotalTime());
            rating.set(tourLog.getRating());
        } else {
            clearTourLogDetails();
        }
    }

    private void clearTourLogDetails() {
        selectedTourLog = null;
        isTourSelected.set(false);
        date.set(null);
        comment.set("");
        difficulty.set(0);
        totalTime.set(0);
        rating.set(0);
    }

    public void createTourLog() {
        if (selectedTour != null) { // Ensure a tour is selected
            TourLogModel newTourLog = new TourLogModel(date.get(), comment.get(), difficulty.get(), totalTime.get(), rating.get());
            newTourLog.setTour(selectedTour); // Set the selected tour on the new log
            tourLogOverviewService.add(newTourLog);
            publisher.publish(Event.TOUR_LOG_CREATED, newTourLog);
        }
    }

    public void deleteSelectedTourLog() {
        if (selectedTourLog != null) {
            tourLogOverviewService.delete(selectedTourLog.getId());
            publisher.publish(Event.TOUR_LOG_DELETED, selectedTourLog);
            setSelectedTourLog(null);
            clearTourSelection();
        }
    }

    public void clearTourSelection() {
        publisher.publish(Event.TOUR_LOG_SELECTED, null);
    }

    public void updateTourLog() {
        if (selectedTourLog != null) {
            selectedTourLog.setDate(date.get());
            selectedTourLog.setComment(comment.get());
            selectedTourLog.setDifficulty(difficulty.get());
            selectedTourLog.setTotalTime(totalTime.get());
            selectedTourLog.setRating(rating.get());
            selectedTourLog.setTour(selectedTour); // Ensure the selected tour is set
            tourLogOverviewService.update(selectedTourLog);
            publisher.publish(Event.TOUR_LOG_UPDATED, selectedTourLog);
        }
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

    public BooleanProperty addButtonDisabledProperty() {
        return isAddButtonDisabled;
    }

    public BooleanProperty editButtonDisabledProperty() {
        return isEditButtonDisabled;
    }

    public BooleanProperty isTourSelectedProperty() {
        return isTourSelected;
    }
}
