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
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
        publisher.publish(Event.TOUR_CREATED, tour.getName());
    }

    public void updateTour(Tour tour) {
        int index = tours.indexOf(tour);
        if (index != -1) {
            tours.set(index, tour);
        }
    }

    public void removeTour(Tour tour) {
        tours.remove(tour);
        publisher.publish(Event.TOUR_DELETED, tour.getName());
    }
}
