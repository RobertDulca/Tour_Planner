package at.technikum.tour_planner.service;

import at.technikum.tour_planner.repository.SearchRepository;

import java.util.List;
import java.util.UUID;

public class SearchService {

    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<UUID> searchtours(String searchText) {
        return searchRepository.searchTours(searchText);
    }

    public void searchtourLog(String searchText) {
    }

    public void searchAttribute(String searchText) {
    }
}
