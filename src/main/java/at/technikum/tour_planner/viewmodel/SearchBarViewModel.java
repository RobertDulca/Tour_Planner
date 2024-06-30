package at.technikum.tour_planner.viewmodel;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.service.SearchService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SearchBarViewModel {
    private final Publisher publisher;
    private final SearchService searchService;
    private Tour selectedTour;
    private final StringProperty searchText = new SimpleStringProperty("");
    private final StringProperty searchType = new SimpleStringProperty("");
    private final BooleanProperty comboBoxDisabled = new SimpleBooleanProperty(true);
    private final Logger logger = Logger.getLogger(SearchBarViewModel.class.getName());

    public SearchBarViewModel(Publisher publisher, SearchService searchService) {
        this.publisher = publisher;
        this.searchService = searchService;

        comboBoxDisabled.bind(searchText.isEmpty());
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
        logger.info("SearchBarViewModel initialized.");
    }

    public void onTourSelected(Object message) {
        if (message instanceof Tour) {
            selectedTour = (Tour) message;
            if(!searchText.get().isEmpty() && !searchType.get().isEmpty()) {
                performSearch();
            }
        } else {
            selectedTour = null;
        }
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }
    public StringProperty selectedCategoryProperty() {
        return searchType;
    }
    public BooleanProperty comboBoxDisabledProperty() {
        return comboBoxDisabled;
    }

    public void performSearch() {
        if (searchText.get().isEmpty() || searchType.get().isEmpty()){
            publisher.publish(Event.SEARCH_CLEARED, null);
            logger.info("Search category or text empty.");
            return; // No action if search text or category is not selected
        }

        switch (searchType.get()) {
            case "Tour" -> searchTours(searchText.get());
            case "Tour Log" -> searchTourLog(searchText.get());
            case "Special Attribute" -> searchSpecialAttribute(searchText.get());
            default -> {
            }
        }
    }
    private void searchTours(String searchText) {
        List<UUID> overviewIDs = searchService.searchTours(searchText);
        publisher.publish(Event.TOUR_SEARCHED, overviewIDs);
        logger.info("Searching Tours for: " + searchText);
    }

    private void searchTourLog(String searchText) {
        List<UUID> searchId = searchService.searchTourLog(searchText, selectedTour.getId());
        publisher.publish(Event.TOUR_LOG_SEARCHED, searchId);
        logger.info("Searching Tour Log for: " + searchText);
    }

    private void searchSpecialAttribute(String searchText) {
        searchService.searchAttribute(searchText);
        System.out.println("Searching Special Attribute for: " + searchText);
    }
}
