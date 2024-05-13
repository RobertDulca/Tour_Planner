package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.TourLogOverviewService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TourLogOverviewViewModel {
    private final Publisher publisher;

    private final TourLogOverviewService tourLogOverviewService;

    private final ObservableList<TourLogModel> tourLogs = FXCollections.observableArrayList();

    private final IntegerProperty selectedTourLogIndex = new SimpleIntegerProperty();

    public TourLogOverviewViewModel(Publisher publisher, TourLogOverviewService tourLogOverviewService) {
        this.publisher = publisher;
        this.tourLogOverviewService = tourLogOverviewService;

        this.selectedTourLogIndex.addListener(observable -> selectTourLog());

        this.publisher.subscribe(Event.TOUR_LOG_SELECTED, this::updateTourLogs);
    }

    public void selectTourLog() {
        if (selectedTourLogIndex.get() == -1) {
            return;
        }

        publisher.publish(Event.TOUR_LOG_SELECTED, tourLogs.get(selectedTourLogIndex.get()));
    }

    private void updateTourLogs(TourLogModel tourLog) {
        tourLogs.setAll((TourLogModel) tourLogOverviewService.findAll());
    }

    public ObservableList<TourLogModel> getTourLogs() {
        return tourLogs;
    }

    public IntegerProperty selectedTourLogIndexProperty() {
        return selectedTourLogIndex;
    }
}
