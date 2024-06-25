package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.ToursTabService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ToursTabViewModel {
    private final Publisher publisher;
    private final ToursTabService tourService;
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public ToursTabViewModel(Publisher publisher, ToursTabService tourService) {
        this.publisher = publisher;
        this.tourService = tourService;

        // Load initial data
        List<Tour> initialTours = tourService.getAllTours();
        tours.addAll(initialTours);

        // Subscribe to events
        publisher.subscribe(Event.TOUR_CREATED, this::onTourCreated);
        publisher.subscribe(Event.TOUR_UPDATED, this::onTourUpdated);
        publisher.subscribe(Event.TOUR_DELETED, this::onTourDeleted);
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public void selectTour(Tour tour) {
        publisher.publish(Event.TOUR_SELECTED, tour);
    }

    public void clearSelectedTour() {
        publisher.publish(Event.TOUR_SELECTED, null);
    }

    private void onTourCreated(Object message) {
        if (message instanceof Tour) {
            tours.setAll(tourService.getAllTours());
        }
    }

    private void onTourUpdated(Object message) {
        if (message instanceof Tour) {
            tours.clear();
            tours.setAll(tourService.getAllTours());
        }
    }

    private void onTourDeleted(Object message) {
        if (message instanceof Tour) {
            tours.clear();
            tours.setAll(tourService.getAllTours());
        }
    }
}