package at.technikum.tour_planner.viewmodel;
import at.technikum.tour_planner.view.TourDetailsView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class TourDetailsViewTest {

    @BeforeAll
    public static void initJFX() {
        new JFXPanel(); // Initializes JavaFX environment
    }

    private void runAndWait(Runnable runnable) {
        try {
            Platform.runLater(runnable);
            Thread.sleep(100); // Wait for the JavaFX thread to process the runnable
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOnAddTour() {
        runAndWait(() -> {
            TourDetailsViewModel viewModel = mock(TourDetailsViewModel.class);
            TourDetailsView view = new TourDetailsView(viewModel);
            view.tourName = new TextField();
            view.tourDesc = new TextField();
            view.from = new TextField();
            view.to = new TextField();
            view.transportType = new ComboBox<>();
            view.transportType.getItems().addAll("Select Type", "Car", "Bicycle", "Walk");
            view.transportType.getSelectionModel().select("Car");

            view.onAddTour();

            verify(viewModel).createAndPublishTour();
        });
    }

    @Test
    public void testOnDeleteTour() {
        runAndWait(() -> {
            TourDetailsViewModel viewModel = mock(TourDetailsViewModel.class);
            TourDetailsView view = new TourDetailsView(viewModel);
            view.deleteButton = new Button();
            view.deleteButton.setDisable(false);

            view.onDeleteTour();

            verify(viewModel).deleteSelectedTour();
        });
    }

}
