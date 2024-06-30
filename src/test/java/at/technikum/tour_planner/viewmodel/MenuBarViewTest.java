package at.technikum.tour_planner.viewmodel;
import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.view.MenuBarView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MenuBarViewTest {

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
    public void testSetSelectedTour() {
        runAndWait(() -> {
            ToursTabViewModel toursTabViewModel = mock(ToursTabViewModel.class);
            MenuBarView menuBarView = new MenuBarView(mock(Publisher.class), toursTabViewModel);

            menuBarView.menuBar = new MenuBar(); // Initialize MenuBar
            menuBarView.exportMenuItem = new MenuItem();
            menuBarView.reportMenuItem = new MenuItem();

            Tour tour = new Tour("Test Tour", "Description", "Wien", "Graz", "Car", "");
            menuBarView.setSelectedTour(tour);
            assertEquals(tour, menuBarView.selectedTour);
        });
    }

    @Test
    public void testOnImport() throws Exception {
        runAndWait(() -> {
            MenuBarViewModel viewModel = mock(MenuBarViewModel.class);
            FileChooser fileChooser = mock(FileChooser.class);
            File file = mock(File.class);
            when(fileChooser.showOpenDialog(any(Stage.class))).thenReturn(file);
            try {
                doNothing().when(viewModel).importTourFromCsv(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            MenuBarView menuBarView = new MenuBarView(mock(Publisher.class), mock(ToursTabViewModel.class));
            menuBarView.menuBar = new MenuBar(); // Initialize MenuBar

            Stage stage = new Stage();
            Scene scene = new Scene(menuBarView.menuBar);
            stage.setScene(scene);

            menuBarView.onImport();

            try {
                verify(viewModel).importTourFromCsv(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
