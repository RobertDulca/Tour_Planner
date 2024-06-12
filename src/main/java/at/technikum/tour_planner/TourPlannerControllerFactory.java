package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.TourLogOverviewRepository;
import at.technikum.tour_planner.service.TourLogOverviewService;
import at.technikum.tour_planner.view.*;
import at.technikum.tour_planner.viewmodel.TourLogDetailsViewModel;
import at.technikum.tour_planner.viewmodel.TourLogOverviewViewModel;

public class TourPlannerControllerFactory implements javafx.util.Callback<Class<?>, Object> {
    private final Publisher publisher;

    private final TourLogOverviewRepository tourLogOverviewRepository;
    private final TourLogOverviewService tourLogOverviewService;
    private final TourLogOverviewView tourLogOverviewView;
    private final TourLogDetailsView tourLogDetailsView;
    private final TourLogOverviewViewModel tourLogOverviewViewModel;
    private final TourLogDetailsViewModel tourLogDetailsViewModel;

    public TourPlannerControllerFactory(Publisher publisher) {
        this.publisher = publisher;

        tourLogOverviewRepository = new TourLogOverviewRepository();
        tourLogOverviewService = new TourLogOverviewService(tourLogOverviewRepository);
        tourLogOverviewViewModel = new TourLogOverviewViewModel(publisher, tourLogOverviewService);
        tourLogDetailsViewModel = new TourLogDetailsViewModel(publisher, tourLogOverviewService);
        tourLogOverviewView = new TourLogOverviewView(tourLogOverviewViewModel);
        tourLogDetailsView = new TourLogDetailsView(tourLogDetailsViewModel);
    }

    @Override
    public Object call(Class<?> param) {
        try {
            if (param == MenuBarView.class) {
                return new MenuBarView(publisher);
            } else if (param == SearchBarView.class) {
                return new SearchBarView(publisher);
            } else if (param == TourDetailsView.class) {
                return new TourDetailsView(publisher);
            } else if (param == ToursTabView.class) {
                return new ToursTabView(publisher);
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
