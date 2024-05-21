package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToursTabViewModel {

    private final Publisher publisher;
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    public ToursTabViewModel(Publisher publisher) {
        this.publisher = publisher;

        // Subscribe to the TOUR_CREATED event
        publisher.subscribe(Event.TOUR_CREATED, this::onTourCreated);
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
        publisher.publish(Event.TOUR_CREATED, tour);
    }

    public void updateTour(Tour tour) {
        int index = tours.indexOf(tour);
        if (index != -1) {
            tours.set(index, tour);
        }
    }

    public void removeTour(Tour tour) {
        tours.remove(tour);
        publisher.publish(Event.TOUR_DELETED, tour);
    }

    public void selectTour(Tour tour) {
        publisher.publish(Event.TOUR_SELECTED, tour);
    }

    private void onTourCreated(Object message) {
        if (message instanceof Tour) {
            Tour newTour = (Tour) message;
            tours.add(newTour);
        }
    }
}
