package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.OpenRouteService;
import at.technikum.tour_planner.service.RouteInfo;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Alert;

public class TourDetailsViewModel {
    private final Publisher publisher;
    private Tour selectedTour;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty origin = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final BooleanProperty isAddButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isEditButtonDisabled = new SimpleBooleanProperty();
    private final BooleanProperty isTourSelected = new SimpleBooleanProperty(false);
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final DoubleProperty distance = new SimpleDoubleProperty();
    private final DoubleProperty estimatedTime = new SimpleDoubleProperty();
    private final OpenRouteService routeService = new OpenRouteService();

    public TourDetailsViewModel(Publisher publisher) {
        this.publisher = publisher;

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
            boolean originChanged = !selectedTour.getOrigin().equals(origin.get());
            boolean destinationChanged = !selectedTour.getDestination().equals(destination.get());
            boolean transportTypeChanged = !selectedTour.getTransportType().equals(transportType.get());

            selectedTour.setName(name.get());
            selectedTour.setDescription(description.get());
            selectedTour.setOrigin(origin.get());
            selectedTour.setDestination(destination.get());
            selectedTour.setTransportType(transportType.get());

            if (originChanged || destinationChanged || transportTypeChanged) {
                fetchRouteDetails(selectedTour);
            }

            selectedTour.setDistance(distance.get());
            selectedTour.setEstimatedTime(estimatedTime.get());
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
            distance.set(tour.getDistance());
            estimatedTime.set(tour.getEstimatedTime());
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
        distance.set(0);
        estimatedTime.set(0);
    }

    public void deleteSelectedTour() {
        if (selectedTour != null) {
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
        fetchRouteDetails(newTour);
        publisher.publish(Event.TOUR_CREATED, newTour);
    }

    private void fetchRouteDetails(Tour tour) {
        String from = tour.getOrigin();
        String to = tour.getDestination();
        String transportType = convertTransportType(tour.getTransportType());

        try {
            String[] fromCoords = routeService.geocodeAddress(from);
            String[] toCoords = routeService.geocodeAddress(to);

            String response = routeService.getRoute(fromCoords[0], fromCoords[1], toCoords[0], toCoords[1], transportType);
            RouteInfo routeInfo = routeService.parseRoute(response);
            if (routeInfo != null) {
                distance.set(routeInfo.getDistance());
                estimatedTime.set(routeInfo.getDuration());
                tour.setDistance(routeInfo.getDistance());
                tour.setEstimatedTime(routeInfo.getDuration());
            } else {
                showAlert("Failed to fetch route details :-(\nPlease try again!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Something went wrong :-(\nCheck input and try again!");
        }
    }

    private String convertTransportType(String transportType) {
        return switch (transportType) {
            case "Car" -> "driving-car";
            case "Bicycle" -> "cycling-regular";
            case "Walk" -> "foot-walking";
            default -> throw new IllegalArgumentException("Unknown transport type: " + transportType);
        };
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
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

    public DoubleProperty tourDistanceProperty() {
        return distance;
    }

    public DoubleProperty estimatedTimeProperty() {
        return estimatedTime;
    }
}