package at.technikum.tour_planner;

import at.technikum.tour_planner.event.Publisher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
    private Publisher publisher;
    private static final String DARK_THEME = "/dark-theme.css";
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        publisher = new Publisher();
        FXMLLoader loader = new FXMLLoader(TourPlannerApplication.class.getResource("main-view.fxml"));
        loader.setControllerFactory(new TourPlannerControllerFactory(publisher));
        scene = new Scene(loader.load(), 900, 550);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();

        stage.setUserData(this);
    }

    public void switchToDarkMode() {
        scene.getStylesheets().add(getClass().getResource(DARK_THEME).toExternalForm());
    }

    public void switchToLightMode() {
        scene.getStylesheets().remove(getClass().getResource(DARK_THEME).toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }
}
