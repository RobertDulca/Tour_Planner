package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.ToursTabService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {
    private final Publisher publisher;
    private Tour selectedTour;
    private final ToursTabService tourService;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isEditButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final StringProperty imageUrl = new SimpleStringProperty();

    public TourDetailsViewModel(Publisher publisher, ToursTabService tourService) {
        this.publisher = publisher;
        this.tourService = tourService;

        isAddButtonDisabled.bind(
                name.isEmpty()
                        .or(origin.isEmpty())
                        .or(destination.isEmpty())
                        .or(transportType.isEmpty())
                        .or(transportType.isEqualTo("Select Type"))
        );

        isEditButtonDisabled.bind(
                name.isEmpty()
                        .or(isTourSelected.not())
                        .or(origin.isEmpty())
                        .or(destination.isEmpty())
                        .or(transportType.isEmpty())
                        .or(transportType.isEqualTo("Select Type"))
        );

        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            setSelectedTour((Tour) message);
        } else {
            clearTourDetails();
        }
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
            tourService.updateTour(selectedTour);
            publisher.publish(Event.TOUR_UPDATED, selectedTour);
        }
    }

    public void setSelectedTour(Tour tour) {
        selectedTour = tour;
        isTourSelected.set(tour != null);
        if (tour != null) {
            name.set(tour.getName());
            description.set(tour.getDescription());
            origin.set(tour.getOrigin());
            destination.set(tour.getDestination());
            transportType.set(tour.getTransportType());
            imageUrl.set(tour.getImageUrl());
        } else {
            clearTourDetails();
        }
    }

    private void clearTourDetails() {
        name.set("");
        description.set("");
        origin.set("");
        destination.set("");
        transportType.set("Select Type");
        isTourSelected.set(false);
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
            tourService.deleteTour(selectedTour.getId());
            publisher.publish(Event.TOUR_DELETED, selectedTour);
            setSelectedTour(null);
            clearTourSelection();
        }
    }

    public void clearTourSelection() {
        publisher.publish(Event.TOUR_SELECTED, null);
    }

    public void createAndPublishTour() {
        Tour newTour = new Tour(name.get(), description.get(), origin.get(), destination.get(), transportType.get(), imageUrl.get());
        tourService.saveTour(newTour);
        publisher.publish(Event.TOUR_CREATED, newTour);
    }

    public StringProperty transportTypeProperty() {
        return transportType;
    }

    public BooleanProperty addButtonDisabledProperty() {
        return isAddButtonDisabled;
    }

    public BooleanProperty editButtonDisabledProperty() {
        return isEditButtonDisabled;
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