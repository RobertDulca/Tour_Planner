package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.SearchRepository;
import at.technikum.tour_planner.repository.TourLogOverviewDatabaseRepository;
import at.technikum.tour_planner.repository.ToursTabDatabaseRepository;
import at.technikum.tour_planner.repository.ToursTabRepository;
import at.technikum.tour_planner.service.SearchService;
import at.technikum.tour_planner.service.TourLogOverviewService;
import at.technikum.tour_planner.service.ToursTabService;
import at.technikum.tour_planner.view.*;
import at.technikum.tour_planner.viewmodel.*;

public class TourPlannerControllerFactory implements javafx.util.Callback<Class<?>, Object> {
    private final Publisher publisher;

    private final TourLogOverviewDatabaseRepository tourLogOverviewRepository;
    private final ToursTabDatabaseRepository toursTabRepository;
    private final SearchRepository searchRepository;
    private final ToursTabService toursTabService;
    private final TourLogOverviewService tourLogOverviewService;
    private final SearchService searchService;
    private final TourLogOverviewView tourLogOverviewView;
    private final TourLogDetailsView tourLogDetailsView;
    private final SearchBarView searchBarView;
    private final TourLogOverviewViewModel tourLogOverviewViewModel;
    private final ToursTabViewModel toursTabViewModel;
    private final TourLogDetailsViewModel tourLogDetailsViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    private final SearchBarViewModel searchBarViewModel;

    public TourPlannerControllerFactory(Publisher publisher) {
        this.publisher = publisher;

        tourLogOverviewRepository = new TourLogOverviewDatabaseRepository();
        toursTabRepository = new ToursTabDatabaseRepository();
        tourLogOverviewService = new TourLogOverviewService(tourLogOverviewRepository);
        toursTabService = new ToursTabService(toursTabRepository);
        tourLogOverviewViewModel = new TourLogOverviewViewModel(publisher, tourLogOverviewService);
        toursTabViewModel = new ToursTabViewModel(publisher, toursTabService);
        tourLogDetailsViewModel = new TourLogDetailsViewModel(publisher, tourLogOverviewService);
        tourDetailsViewModel = new TourDetailsViewModel(publisher, toursTabService);
        tourLogOverviewView = new TourLogOverviewView(tourLogOverviewViewModel);
        tourLogDetailsView = new TourLogDetailsView(tourLogDetailsViewModel);

        searchRepository = new SearchRepository();
        searchService = new SearchService(searchRepository);
        searchBarViewModel = new SearchBarViewModel(publisher, searchService);
        searchBarView = new SearchBarView(searchBarViewModel);
    }

    @Override
    public Object call(Class<?> param) {
        try {
            if (param == MenuBarView.class) {
                return new MenuBarView(publisher, toursTabViewModel);
            } else if (param == SearchBarView.class) {
                return new SearchBarView(searchBarViewModel);
            } else if (param == TourDetailsView.class) {
                return new TourDetailsView(tourDetailsViewModel);
            } else if (param == ToursTabView.class) {
                return new ToursTabView(toursTabViewModel);
            } else if (param == TourLogDetailsView.class) {
                return new TourLogDetailsView(tourLogDetailsViewModel);
            } else if (param == TourLogOverviewView.class) {
                return new TourLogOverviewView(tourLogOverviewViewModel);
            } else if (param == MainController.class) {
                return new MainController();
            } else {
                throw new IllegalArgumentException("Unknown controller: " + param);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
