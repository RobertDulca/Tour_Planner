package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.logging.Logger;

public class TourLogDetailsViewModel {

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    private final StringProperty comment = new SimpleStringProperty();

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    private final DoubleProperty totalTime = new SimpleDoubleProperty();

    private final DoubleProperty totalDistance = new SimpleDoubleProperty();

    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isEditButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final Publisher publisher;
    private final TourLogOverviewService tourLogOverviewService;
    private static final Logger logger = Logger.getLogger(TourDetailsViewModel.class.getName());
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
                        .or(totalDistance.isEqualTo(0))
                        .or(rating.isEqualTo(0))
        );

        isEditButtonDisabled.bind(
                date.isNull()
                        .or(isTourSelected.not())
                        .or(comment.isEmpty())
                        .or(difficulty.isEqualTo(0))
                        .or(totalTime.isEqualTo(0))
                        .or(totalDistance.isEqualTo(0))
                        .or(rating.isEqualTo(0))
        );

        publisher.subscribe(Event.TOUR_LOG_SELECTED, this::onTourLogSelected);
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
        logger.info("TourLogDetailsViewModel initialized and subscriptions added.");
    }

    public void onTourSelected(Object message) {
        if (message instanceof Tour) {
            selectedTour = (Tour) message;
            isTourSelected.set(true);
        } else {
            selectedTour = null;
            isTourSelected.set(false);
        }
    }

    public void onTourLogSelected(Object message) {
        if (message instanceof TourLogModel) {
            setSelectedTourLog((TourLogModel) message);
            logger.info("Tour log selected: " + ((TourLogModel) message).getDate().toString());
        } else {
            clearTourLogDetails();
            logger.info("Tour log selection cleared.");
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
            totalDistance.set(tourLog.getTotalTime());
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
        totalDistance.set(0);
        rating.set(0);
    }

    public void createTourLog() {
        if (selectedTour != null) { // Ensure a tour is selected
            TourLogModel newTourLog = new TourLogModel(date.get(), comment.get(), difficulty.get(), totalTime.get(), totalDistance.get(), rating.get());
            newTourLog.setTour(selectedTour); // Set the selected tour on the new log
            tourLogOverviewService.add(newTourLog);
            publisher.publish(Event.TOUR_LOG_CREATED, newTourLog);
            logger.info("New tour log created:" + newTourLog.getDate().toString());
        }
    }

    public void deleteSelectedTourLog() {
        if (selectedTourLog != null) {
            tourLogOverviewService.delete(selectedTourLog.getId());
            publisher.publish(Event.TOUR_LOG_DELETED, selectedTourLog);
            setSelectedTourLog(null);
            clearTourSelection();
            logger.info("Tour log deleted.");
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
            selectedTourLog.setTotalDistance(totalDistance.get());
            selectedTourLog.setRating(rating.get());
            selectedTourLog.setTour(selectedTour); // Ensure the selected tour is set
            tourLogOverviewService.update(selectedTourLog);
            publisher.publish(Event.TOUR_LOG_UPDATED, selectedTourLog);
            logger.info("Tour log updated:" + selectedTourLog.getDate().toString());
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

    public DoubleProperty totalDistanceProperty() {
        return totalDistance;
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

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            logger.warning("Alert shown: " + message);
        });
    }
}
