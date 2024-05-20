package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {

    private final Publisher publisher;
    private Tour selectedTour;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final StringProperty imageUrl = new SimpleStringProperty();

    public TourDetailsViewModel(Publisher publisher) {
        this.publisher = publisher;
        isAddButtonDisabled.bind(name.isEmpty().or(origin.isEmpty()).or(destination.isEmpty()).or(transportType.isEmpty()));

        // Subscribe to events
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
    }

    private void onTourSelected(String message) {
        // Handle the tour selection
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    public void saveTourChanges() {
        if (selectedTour != null) {
            selectedTour.setName(name.get());
            selectedTour.setDescription(description.get());
            selectedTour.setOrigin(origin.get());
            selectedTour.setDestination(destination.get());
            selectedTour.setTransportType(transportType.get());
            publisher.publish(Event.TOUR_UPDATED, selectedTour.getName());
        }
    }

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
        transportType.set(tour.getTransportType());
        imageUrl.set(tour.getImageUrl());
    }

    public void clearTourDetails() {
        name.set("");
        description.set("");
        origin.set("");
        destination.set("");
        transportType.set("");
        isTourSelected.set(false);
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            publisher.publish(Event.TOUR_DELETED, selectedTour.getName());
            setSelectedTour(null);
        }
    }

    public Tour createTour() {
        return new Tour(name.get(), description.get(), origin.get(), destination.get(), transportType.get(), imageUrl.get());
    }

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
