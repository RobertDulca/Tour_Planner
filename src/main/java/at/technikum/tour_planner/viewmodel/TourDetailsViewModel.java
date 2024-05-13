package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {

    // Singleton instance
    private static TourDetailsViewModel instance;
    private Tour selectedTour;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final StringProperty imageUrl = new SimpleStringProperty();

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    private TourDetailsViewModel() {
        isAddButtonDisabled.bind(name.isEmpty().or(origin.isEmpty()).or(destination.isEmpty()).or(transportType.isEmpty()));  // Improved condition
    }

    // Get singleton instance of TourDetailsViewModel
    public static synchronized TourDetailsViewModel getInstance() {
        if (instance == null) {
            instance = new TourDetailsViewModel();
        }
        return instance;
    }

    public void saveTourChanges() {
        if (selectedTour != null) {
            selectedTour.setName(name.get());
            selectedTour.setDescription(description.get());
            selectedTour.setOrigin(origin.get());
            selectedTour.setDestination(destination.get());
            selectedTour.setTransportType(transportType.get());
            ToursTabViewModel.getInstance().updateTour(selectedTour);
        }
    }

    // Set the currently selected tour and update properties
    public void setSelectedTour(Tour tour) {
        selectedTour = tour;
        isTourSelected.set(tour != null);
        if (tour != null) {
            setTourDetails(tour);
        } else {
            clearTourDetails();
        }
    }

    // Update the ViewModel properties based on the selected tour
    private void setTourDetails(Tour tour) {
        name.set(tour.getName());
        description.set(tour.getDescription());
        origin.set(tour.getOrigin());
        destination.set(tour.getDestination());
        transportType.set(tour.getTransportType());
        imageUrl.set(tour.getImageUrl());
    }

    // Clear all properties when no tour is selected
    public void clearTourDetails() {
        name.set("");
        description.set("");
        origin.set("");
        destination.set("");
        transportType.set("");
        isTourSelected.set(false);  // indicate no tour selected
    }

    // Remove the selected tour from list & clear selection
    public void deleteSelectedTour() {
        if (selectedTour != null) {
            ToursTabViewModel.getInstance().removeTour(selectedTour);
            setSelectedTour(null);
        }
    }

    public Tour createTour() {
        return new Tour(name.get(), description.get(), origin.get(), destination.get(), transportType.get(), imageUrl.get());
    }

    // Accessor methods for JavaFX properties
    public StringProperty transportTypeProperty() {
        return transportType;
    }

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
}
