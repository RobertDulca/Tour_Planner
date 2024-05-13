package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToursTabViewModel {

    // Singleton instance
    private static final ToursTabViewModel instance = new ToursTabViewModel();
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();

    private ToursTabViewModel() {}

    public static ToursTabViewModel getInstance() {
        return instance;
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    // replacing old tour with new
    public void updateTour(Tour tour) {
        int index = tours.indexOf(tour);
        if (index != -1) {
            tours.set(index, tour);
        }
    }

    public void removeTour(Tour tour) {
        tours.remove(tour);
        // Adjust selected tour based on remaining tours
        if (!tours.isEmpty()) {
            // Select the first tour in the list
            TourDetailsViewModel.getInstance().setSelectedTour(tours.get(0));
        } else {
            // When no tours remain, clear tour details
            TourDetailsViewModel.getInstance().clearTourDetails();
        }
    }
}
