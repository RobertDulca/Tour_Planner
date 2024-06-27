package at.technikum.tour_planner.view;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.MenuBarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarView implements Initializable {
    private final MenuBarViewModel viewModel;

    @FXML
    private MenuBar menuBar;

    public MenuBarView(Publisher publisher) {
        this.viewModel = new MenuBarViewModel(publisher);
    }

    @FXML
    protected void onSummary() {
    }

    @FXML
    protected void onExport() {
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
