package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import at.technikum.tour_planner.entity.Tour;

import java.net.URL;
import java.util.ResourceBundle;

public class ToursTabView implements Initializable {
    private final ToursTabViewModel viewModel;
    public ToursTabView() {
        this.viewModel = ToursTabViewModel.getInstance(); // Use Singleton instance
    }

    @FXML
    private Button addTourButton;
    @FXML
    private Button removeTourButton;
    @FXML
    private Button editTourButton;
    @FXML
    private ListView<Tour> toursList;

    @FXML
    public void onAddTour() {
        // Removed the addTour functionality for now
    }

    @FXML
    public void onRemoveTour() {
        // Removed the removeTour functionality for now
    }

    @FXML
    public void onEditTour() {
        //Edit Logic
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
        setupSelectionModel();
    }
    private void setupSelectionModel() {
        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                TourDetailsViewModel.getInstance().setTourDetails(newSelection);
            }
        });
    }

    private void setupListView() {
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<Tour>() {
            @Override
            public String toString(Tour tour) {
                return tour.getName(); // Assuming Tour class has a getName method
            }
            @Override
            public Tour fromString(String string) {
                // Implementation not needed for this example
                return null;
            }
        }));
    }
}
