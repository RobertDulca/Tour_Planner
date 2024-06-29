package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.MenuBarViewModel;
import com.itextpdf.text.DocumentException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarView implements Initializable {

    private final MenuBarViewModel viewModel;
    private Tour selectedTour;

    public MenuBarView(Publisher publisher) {
        this.viewModel = new MenuBarViewModel(publisher);
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
    }

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private MenuItem reportMenuItem;

    @FXML
    protected void onReport() {
        if (selectedTour != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            Stage stage = (Stage) menuBar.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    viewModel.generateTourReport(selectedTour, file);
                } catch (DocumentException | IOException e) {
                    showAlert("Failed to generate a Report: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("No tour selected for Report");
        }
    }

    @FXML
    protected void onSummary() {
    }

    @FXML
    protected void onExport() {
        if (selectedTour != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            Stage stage = (Stage) menuBar.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    viewModel.exportTourToCsv(selectedTour, file);
                } catch (IOException e) {
                    showAlert("Failed to export tour: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("No tour selected for export.");
        }
    }

    @FXML
    protected void onImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                viewModel.importTourFromCsv(file);
            } catch (IOException e) {
                showAlert("Failed to import tour: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour = selectedTour;
        exportMenuItem.setDisable(selectedTour == null);
        reportMenuItem.setDisable(selectedTour == null);
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            setSelectedTour((Tour) message);
        } else {
            setSelectedTour(null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exportMenuItem.setDisable(true);
        reportMenuItem.setDisable(true);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
