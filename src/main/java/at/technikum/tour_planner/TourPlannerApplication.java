package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
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
        loader.setControllerFactory(new TourPlannerControllerFactory(publisher));
        Scene scene = new Scene(loader.load(), 900, 550);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
