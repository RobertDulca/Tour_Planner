package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.OpenRouteService;
import at.technikum.tour_planner.service.RouteInfo;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.technikum.tour_planner.service.ToursTabService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourDetailsViewModel {
    private final Publisher publisher;
    private Tour selectedTour;
    private final ToursTabService tourService;
    private static final Logger logger = Logger.getLogger(TourDetailsViewModel.class.getName());


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
        logger.info("TourDetailsViewModel initialized and subscriptions set up.");
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            setSelectedTour((Tour) message);
            logger.info("Tour selected: " + ((Tour) message).getName());
        } else {
            clearTourDetails();
            logger.info("Tour selection cleared.");
        }
    }


    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            logger.warning("Alert shown: " + message);
        });
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
                try {
                    fetchAndSetMapImage(selectedTour);
                } catch (IOException e) {
                    showAlert("Failed to update map image: " + e.getMessage());
                    logger.log(Level.SEVERE, "Failed to update map image", e);
                    e.printStackTrace();
                }
            }

            selectedTour.setDistance(distance.get());
            selectedTour.setEstimatedTime(estimatedTime.get());
            selectedTour.setImageUrl(imageUrl.get());
            tourService.updateTour(selectedTour);
            publisher.publish(Event.TOUR_UPDATED, selectedTour);
            logger.info("Tour changes saved and tour updated: " + selectedTour.getName());
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
            if (tour.getImageUrl() != null && !tour.getImageUrl().isEmpty()) {
                imageUrl.set(tour.getImageUrl());
            } else {
                imageUrl.set(null);
            }
            logger.info("Tour details set for: " + tour.getName());
        } else {
            clearTourDetails();
        }
    }


    public Tour getSelectedTour() {
        return selectedTour;
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
        imageUrl.set("");
        logger.info("Tour details cleared.");
    }


    public void deleteSelectedTour() {
        if (selectedTour != null) {
            tourService.deleteTour(selectedTour.getId());
            publisher.publish(Event.TOUR_DELETED, selectedTour);
            logger.info("Tour deleted: " + selectedTour.getName());
            setSelectedTour(null);
            clearTourSelection();
        }
    }


    public void clearTourSelection() {
        publisher.publish(Event.TOUR_SELECTED, null);
    }

    public void createAndPublishTour() {
        String imageUrlValue = imageUrl.get() != null ? imageUrl.get() : "";
        Tour newTour = new Tour(name.get(), description.get(), origin.get(), destination.get(), transportType.get(), imageUrlValue);
        try {
            fetchRouteDetails(newTour);
            fetchAndSetMapImage(newTour);
            tourService.saveTour(newTour);
            publisher.publish(Event.TOUR_CREATED, newTour);
            logger.info("New tour created and published: " + newTour.getName());
        } catch (Exception e) {
            showAlert("Failed to create new tour: " + e.getMessage());
            logger.log(Level.SEVERE, "Failed to create new tour", e);
            e.printStackTrace();
        }
    }


    private String convertTransportType(String transportType) {     //make it API friendly
        return switch (transportType) {
            case "Car" -> "driving-car";
            case "Bicycle" -> "cycling-regular";
            case "Walk" -> "foot-walking";
            default -> throw new IllegalArgumentException("Unknown transport type: " + transportType);
        };
    }

    private void fetchRouteDetails(Tour tour) {
        String from = tour.getOrigin();
        String to = tour.getDestination();
        String transportType = convertTransportType(tour.getTransportType());

        try {
            double[] fromCoords = routeService.geocodeAddress(from);
            double[] toCoords = routeService.geocodeAddress(to);

            String response = routeService.getRoute(Double.toString(fromCoords[0]), Double.toString(fromCoords[1]), Double.toString(toCoords[0]), Double.toString(toCoords[1]), transportType);
            RouteInfo routeInfo = routeService.parseRoute(response);
            if (routeInfo != null) {
                distance.set(routeInfo.getDistance());
                estimatedTime.set(routeInfo.getDuration());
                tour.setDistance(routeInfo.getDistance());
                tour.setEstimatedTime(routeInfo.getDuration());
                logger.info("Route details fetched for tour: " + tour.getName());
            } else {
                showAlert("Failed to fetch route details :-(\nPlease try again!");
                logger.warning("Failed to fetch route details for tour: " + tour.getName());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching route details for tour: " + tour.getName(), e);
            e.printStackTrace();
            showAlert("Something went wrong :-(\nCheck input and try again!");
        }
    }


    public void fetchAndSetMapImage(Tour tour) throws IOException {
        try {
            BufferedImage mapImage = routeService.fetchMapForTour(tour, 16, 3); // Adjusted zoom level
            String imagePath = routeService.saveImage(mapImage);
            imageUrl.set(imagePath);
            tour.setImageUrl(imagePath);
            logger.info("Map image fetched and set for tour: " + tour.getName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to fetch map image for tour: " + tour.getName(), e);
            e.printStackTrace();
            showAlert("Failed to fetch map image: " + e.getMessage());
            throw e;
        }
    }


    public StringProperty imageProperty() {
        return imageUrl;
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