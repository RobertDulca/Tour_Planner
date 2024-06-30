package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.MenuBarViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuBarView implements Initializable {

    private final MenuBarViewModel viewModel;
    private final ToursTabViewModel toursTabViewModel;
    private Tour selectedTour;
    private static final Logger logger = Logger.getLogger(MenuBarView.class.getName());

    public MenuBarView(Publisher publisher, ToursTabViewModel toursTabViewModel) {
        this.viewModel = new MenuBarViewModel(publisher);
        this.toursTabViewModel = toursTabViewModel;
        publisher.subscribe(Event.TOUR_SELECTED, this::onTourSelected);
        publisher.subscribe(Event.TOUR_CREATED, this::onToursUpdated);
        publisher.subscribe(Event.TOUR_UPDATED, this::onToursUpdated);
        publisher.subscribe(Event.TOUR_DELETED, this::onToursUpdated);
        publisher.subscribe(Event.TOUR_IMPORTED, this::onToursUpdated);
        logger.info("MenuBarView initialized with subscriptions to events.");
    }

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private MenuItem reportMenuItem;
    @FXML
    private MenuItem summaryMenuItem;

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
                    logger.info("Tour report generated for tour: " + selectedTour.getName());
                } catch (DocumentException | IOException e) {
                    logger.log(Level.SEVERE, "Failed to generate a Report", e);
                    showAlert("Failed to generate a Report: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("No tour selected for Report");
            logger.warning("Attempted to generate report with no tour selected.");
        }
    }

    @FXML
    protected void onSummary() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Summary PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        Stage stage = (Stage) menuBar.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                viewModel.generateSummaryReport(file);
                logger.info("Summary report generated.");
            } catch (DocumentException | IOException e) {
                logger.log(Level.SEVERE, "Failed to generate Summary Report", e);
                showAlert("Failed to generate Summary Report: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
                    logger.info("Tour exported to CSV for tour: " + selectedTour.getName());
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Failed to export tour", e);
                    showAlert("Failed to export tour: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            showAlert("No tour selected for export.");
            logger.warning("Attempted to export with no tour selected.");
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
                logger.info("Tour imported from CSV file: " + file.getName());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to import tour", e);
                showAlert("Failed to import tour: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour = selectedTour;
        exportMenuItem.setDisable(selectedTour == null);
        reportMenuItem.setDisable(selectedTour == null);
        logger.info("Selected tour set to: " + (selectedTour != null ? selectedTour.getName() : "None"));
    }

    private void onToursUpdated(Object message) {
        summaryMenuItem.setDisable(toursTabViewModel.getTours().isEmpty());
        logger.info("Tours updated. Summary menu item is " + (summaryMenuItem.isDisable() ? "disabled" : "enabled"));
    }

    private void onTourSelected(Object message) {
        if (message instanceof Tour) {
            setSelectedTour((Tour) message);
            logger.info("Tour selected: " + ((Tour) message).getName());
        } else {
            setSelectedTour(null);
            logger.info("No tour selected.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exportMenuItem.setDisable(true);
        reportMenuItem.setDisable(true);
        summaryMenuItem.setDisable(toursTabViewModel.getTours().isEmpty());
        logger.info("MenuBarView initialized");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        logger.warning("Alert shown: " + message);
    }
}

