package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.repository.TourLogOverviewRepository;
import at.technikum.tour_planner.service.TourLogOverviewService;
import at.technikum.tour_planner.view.TourLogDetailsView;
import at.technikum.tour_planner.view.TourLogOverviewView;
import at.technikum.tour_planner.viewmodel.TourLogDetailsViewModel;
import at.technikum.tour_planner.viewmodel.TourLogOverviewViewModel;

public class ViewFactory {
    private static ViewFactory instance;

    private final Publisher publisher;

    private final TourLogOverviewRepository tourLogOverviewRepository;

    private final TourLogOverviewService tourLogOverviewService;

    private final TourLogOverviewView tourLogOverviewView;
    private final TourLogDetailsView tourLogDetailsView;
    private final TourLogOverviewViewModel tourLogOverviewViewModel;
    private final TourLogDetailsViewModel tourLogDetailsViewModel;

    private ViewFactory() {
        publisher = new Publisher();

        tourLogOverviewRepository = new TourLogOverviewRepository();
        tourLogOverviewService = new TourLogOverviewService(tourLogOverviewRepository);
        tourLogOverviewViewModel = new TourLogOverviewViewModel(publisher, tourLogOverviewService);
        tourLogDetailsViewModel = new TourLogDetailsViewModel(publisher, tourLogOverviewService);
        tourLogOverviewView = new TourLogOverviewView(tourLogOverviewViewModel);
        tourLogDetailsView = new TourLogDetailsView(tourLogDetailsViewModel);
    }

    public static ViewFactory getInstance() {
        if (null == instance) {
            instance = new ViewFactory();
        }

        return instance;
    }

    public Object create(Class<?> viewClass) {
        if(TourLogOverviewView.class == viewClass) {
            return new TourLogOverviewView(tourLogOverviewViewModel);
        }
        if(TourLogDetailsView.class == viewClass) {
            return new TourLogDetailsView(tourLogDetailsViewModel);
        }

        throw new IllegalArgumentException("Unknown view class: " + viewClass);
    }
}
