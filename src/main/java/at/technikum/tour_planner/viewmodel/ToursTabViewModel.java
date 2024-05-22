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
            tours.add((Tour) message);
        }
    }

    private void onTourUpdated(Object message) {
        if (message instanceof Tour) {
            int index = tours.indexOf((Tour) message);
            if (index != -1) {
                tours.set(index, (Tour) message);
            }
        }
    }

    private void onTourDeleted(Object message) {
        if (message instanceof Tour) {
            tours.remove((Tour) message);
            clearSelectedTour();
        }
    }
}