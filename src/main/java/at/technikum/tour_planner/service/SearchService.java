package at.technikum.tour_planner.service;

import at.technikum.tour_planner.repository.SearchRepository;

import java.util.List;
import java.util.UUID;

public class SearchService {

    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<UUID> searchTours(String searchText) {
        return searchRepository.searchTours(searchText);
    }

    public List<UUID> searchTourLog(String searchText, UUID tourId) {
        return searchRepository.searchTourLogs(searchText, tourId);
    }

    public void searchAttribute(String searchText) {
    }
}
