package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.TourLogOverviewRepository;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class TourLogOverviewViewModel {
    private final Publisher publisher;

    private final TourLogOverviewService tourLogOverviewService;

    public static TourLogOverviewViewModel getInstance() {
        return instance;
    }

    private static final TourLogOverviewViewModel instance = new TourLogOverviewViewModel(new Publisher(), new TourLogOverviewService(new TourLogOverviewRepository()));

    private final ObservableList<TourLogModel> tourLogs = FXCollections.observableArrayList();

    private final IntegerProperty selectedTourLogIndex = new SimpleIntegerProperty();

    public TourLogOverviewViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        this.selectedTourLogIndex.addListener(observable -> selectTourLog());

        publisher.subscribe(Event.TOUR_LOG_CREATED, this::updateTourLogs);
    }

    public void selectTourLog() {
        if (selectedTourLogIndex.get() == -1) {
            return;
        }

        publisher.publish(Event.TOUR_LOG_SELECTED, tourLogs.get(selectedTourLogIndex.get()));
    }

    private void updateTourLogs(Object message) {
        System.out.println("Updating tour logs...");
        List<TourLogModel> allTourLogs = tourLogOverviewService.findAll();
        if(allTourLogs.isEmpty()) {
            System.out.println("No tour logs found.");
        } else {
            System.out.println("Received tour logs: " + allTourLogs.size());
            allTourLogs.forEach(log -> System.out.println("Log Date: " + log.getDate() + ", Details: " + log.getComment())); // Assuming getDetails() is a method in TourLogModel
        }
        tourLogs.setAll(allTourLogs);
    }

    public ObservableList<TourLogModel> getTourLogs() {
        return tourLogs;
    }

    public IntegerProperty selectedTourLogIndexProperty() {
        return selectedTourLogIndex;
    }
}
