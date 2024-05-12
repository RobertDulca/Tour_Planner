package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {
    private Tour selectedTour;
    private static TourDetailsViewModel instance; // Singleton instance
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);

    public Tour getSelectedTour() {
        return this.selectedTour;
    }
    public void setSelectedTour(Tour tour) {
        selectedTour = tour;
        isTourSelected.set(tour != null);  // This should correctly reflect the state
        if (tour != null) {
            setTourDetails(tour);  // Update the tour details
        }
    }
    public void deleteSelectedTour() {
        if (selectedTour != null) {
            ToursTabViewModel.getInstance().removeTour(selectedTour);
            setSelectedTour(null);  // Clear the selected tour
        }
    }

    public void setTourDetails(Tour tour) {
        name.set(tour.getName());
        description.set(tour.getDescription());
        origin.set(tour.getOrigin());
        destination.set(tour.getDestination());
    }

    public BooleanProperty isTourSelectedProperty() {
        return isTourSelected;
    }

    public TourDetailsViewModel() {
        // Bind the BooleanProperty to whether the name property is empty
        isAddButtonDisabled.bind(name.isEmpty());
    }
    public static TourDetailsViewModel getInstance() {
        if (instance == null) {
            instance = new TourDetailsViewModel();
        }
        return instance;
    }
    public BooleanProperty addButtonDisabledProperty() {
        return isAddButtonDisabled;
    }
    public StringProperty tourNameProperty() {
        return name;
    }

    public StringProperty tourDescriptionProperty() {
        return description;
    }

    public StringProperty fromProperty() {
        return origin;
    }

    public StringProperty toProperty() {
        return destination;
    }

    public Tour createTour() {
        return new Tour(name.get(), description.get(), origin.get(), destination.get());
    }
}