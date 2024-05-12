package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToursTabViewModel {
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

    public void removeTour(Tour tour) {
        tours.remove(tour);
        // Adjust the selected tour based on remaining tours in the list
        if (!tours.isEmpty()) {
            TourDetailsViewModel.getInstance().setSelectedTour(tours.get(0));
        } else {
            // When no tours remain, explicitly clear tour details
            TourDetailsViewModel.getInstance().clearTourDetails();
        }
    }
}
