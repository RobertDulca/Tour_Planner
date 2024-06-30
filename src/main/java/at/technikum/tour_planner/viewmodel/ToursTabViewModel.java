package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.ToursTabService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ToursTabViewModel {
    private final Publisher publisher;
    private final ToursTabService tourService;
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(ToursTabViewModel.class.getName());


    public ToursTabViewModel(Publisher publisher, ToursTabService tourService) {
        this.publisher = publisher;
        this.tourService = tourService;

        // Load initial data
        List<Tour> initialTours = tourService.getAllTours();
        tours.addAll(initialTours);
        logger.info("Loaded initial tours: " + initialTours.size());

        // Subscribe to events
        publisher.subscribe(Event.TOUR_CREATED, this::onTourCreated);
        publisher.subscribe(Event.TOUR_UPDATED, this::onTourUpdated);
        publisher.subscribe(Event.TOUR_DELETED, this::onTourDeleted);
        publisher.subscribe(Event.TOUR_IMPORTED, this::onTourImported);
        publisher.subscribe(Event.TOUR_SEARCHED, this::onTourSearched);
        publisher.subscribe(Event.SEARCH_CLEARED, this::cleanSearch);
        logger.info("Subscribed to tour-related events.");
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public void selectTour(Tour tour) {
        publisher.publish(Event.TOUR_SELECTED, tour);
        logger.info("Tour selected: " + (tour != null ? tour.getName() : "None"));
    }

    public void clearSelectedTour() {
        publisher.publish(Event.TOUR_SELECTED, null);
        logger.info("Tour selection cleared.");
    }

    private void cleanSearch(Object message) {
        tours.clear();
        tours.setAll(tourService.getAllTours());
        logger.info("Search cleared, all tours reloaded.");
    }

    private void onTourCreated(Object message) {
        if (message instanceof Tour) {
            tours.setAll(tourService.getAllTours());
            logger.info("Tour created: " + ((Tour) message).getName());
        }
    }

    private void onTourUpdated(Object message) {
        if (message instanceof Tour) {
            tours.clear();
            tours.setAll(tourService.getAllTours());
            logger.info("Tour updated: " + ((Tour) message).getName());
        }
    }

    private void onTourDeleted(Object message) {
        if (message instanceof Tour) {
            tours.clear();
            tours.setAll(tourService.getAllTours());
            logger.info("Tour deleted: " + ((Tour) message).getName());
        }
    }

    private void onTourImported(Object message) {
        if (message instanceof Tour) {
            tours.add((Tour) message);
            logger.info("Tour imported: " + ((Tour) message).getName());
        }
    }

    private void onTourSearched(Object message) {
        if (message instanceof List) {
            List<UUID> searchedToursID = (List<UUID>) message;
            tours.clear();
            tours.setAll(tourService.getToursByID(searchedToursID));
            logger.info("Tours searched and loaded: " + searchedToursID.size());
        }
    }
}