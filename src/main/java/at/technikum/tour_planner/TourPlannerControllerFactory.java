package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.view.*;

public class TourPlannerControllerFactory implements javafx.util.Callback<Class<?>, Object> {
    private final Publisher publisher;

    public TourPlannerControllerFactory(Publisher publisher) {
        this.publisher = publisher;
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
