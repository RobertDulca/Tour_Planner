package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.UUID;

public class TourLogOverviewViewModel {
    private final Publisher publisher;
    private final TourLogOverviewService tourLogOverviewService;
    private final ObservableList<TourLogModel> tourLogs = FXCollections.observableArrayList();
    private UUID selectedTourId;

    public TourLogOverviewViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        // Subscribe to tour selection changes
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);

        // Initial loading of logs (could be empty if no tour is selected initially)
        updateTourLogs(null);

        // Subscribe to log changes
        publisher.subscribe(Event.TOUR_LOG_CREATED, this::updateTourLogs);
        publisher.subscribe(Event.TOUR_LOG_UPDATED, this::updateTourLogs);
        publisher.subscribe(Event.TOUR_LOG_DELETED, this::updateTourLogs);
        publisher.subscribe(Event.TOUR_LOG_SEARCHED, this::searchTourLogs);
        publisher.subscribe(Event.SEARCH_CLEARED, this::cleanSearch);
    }

    private void cleanSearch(Object message) {
        List<TourLogModel> allTourLogs = tourLogOverviewService.findByTourId(selectedTourId);
        tourLogs.clear();
        tourLogs.setAll(allTourLogs);
    }

    private void searchTourLogs(Object message) {
        if (message instanceof List) {
            List<UUID> logIds = (List<UUID>) message;
            tourLogs.clear();
            tourLogs.setAll(tourLogOverviewService.getTourLogsByIds(logIds));
        }
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            Tour selectedTour = (Tour) message;
            selectedTourId = selectedTour.getId();
        } else {
            selectedTourId = null;
        }
        updateTourLogs(null);
    }

    private void updateTourLogs(Object message) {
        List<TourLogModel> allTourLogs;
        if (selectedTourId != null) {
            allTourLogs = tourLogOverviewService.findByTourId(selectedTourId);
        } else {
            allTourLogs = FXCollections.emptyObservableList(); // Clear logs if no tour is selected
        }
        tourLogs.clear();
        tourLogs.setAll(allTourLogs);
    }

    public ObservableList<TourLogModel> getTourLogs() {
        return tourLogs;
    }

    public void selectTourLog(TourLogModel tourLog) {
        publisher.publish(Event.TOUR_LOG_SELECTED, tourLog);
    }

    public void clearSelectedTourLog() {
        publisher.publish(Event.TOUR_LOG_SELECTED, null);
    }
}
