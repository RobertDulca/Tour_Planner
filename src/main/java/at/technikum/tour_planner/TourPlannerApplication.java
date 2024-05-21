package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.view.MenuBarView;
import at.technikum.tour_planner.view.SearchBarView;
import at.technikum.tour_planner.view.TourDetailsView;
import at.technikum.tour_planner.view.ToursTabView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
    private Publisher publisher;
    @Override
    public void start(Stage stage) throws IOException {
        publisher = new Publisher();
        FXMLLoader loader = new FXMLLoader(TourPlannerApplication.class.getResource("main-view.fxml"));
        // controller factory responsible for creating controllers for the views
        loader.setControllerFactory(param -> {
            try {
                if (param == MenuBarView.class) {
                    return new MenuBarView(publisher);
                }
                if (param == SearchBarView.class) {
                    return new SearchBarView(publisher);
                }
                if (param == TourDetailsView.class) {
                    return new TourDetailsView(publisher);
                }
                if (param == ToursTabView.class) {
                    return new ToursTabView(publisher);
                }
                if (param == MainController.class) {
                    return new MainController();
                }
                throw new IllegalArgumentException("Unknown controller: " + param);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        Scene scene = new Scene(loader.load(), 900, 500);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
