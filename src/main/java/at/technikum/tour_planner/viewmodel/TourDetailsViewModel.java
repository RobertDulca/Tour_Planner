package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {
    private static TourDetailsViewModel instance;
    private Tour selectedTour;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);

    // Private constructor to ensure singleton pattern
    private TourDetailsViewModel() {
        isAddButtonDisabled.bind(name.isEmpty());
    }

    // Public static method to create/get the singleton instance
    public static synchronized TourDetailsViewModel getInstance() {
        if (instance == null) {
            instance = new TourDetailsViewModel();
        }
        return instance;
    }

    // Public methods to access and modify properties
    public void setSelectedTour(Tour tour) {
        selectedTour = tour;
        isTourSelected.set(tour != null);
        if (tour != null) {
            setTourDetails(tour);
        } else {
            clearTourDetails();
        }
    }

    private void setTourDetails(Tour tour) {
        name.set(tour.getName());
        description.set(tour.getDescription());
        origin.set(tour.getOrigin());
        destination.set(tour.getDestination());
    }

    private void clearTourDetails() {
        name.set("");
        description.set("");
        origin.set("");
        destination.set("");
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            ToursTabViewModel.getInstance().removeTour(selectedTour);
            setSelectedTour(null);
        }
    }

    public Tour createTour() {
        return new Tour(name.get(), description.get(), origin.get(), destination.get());
    }

    // Accessors for JavaFX properties
    public BooleanProperty addButtonDisabledProperty() {
        return isAddButtonDisabled;
    }

    public BooleanProperty isTourSelectedProperty() {
        return isTourSelected;
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

    public Tour getSelectedTour() {
        return selectedTour;
    }
}
