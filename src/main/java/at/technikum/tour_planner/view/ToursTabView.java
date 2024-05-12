package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;
public class ToursTabView implements Initializable {
    private final ToursTabViewModel viewModel;

    public ToursTabView() {
        this.viewModel = ToursTabViewModel.getInstance(); // Use Singleton instance
    }

    @FXML
    private ListView<Tour> toursList;

    private void setupListView() {
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<Tour>() {
            @Override
            public String toString(Tour tour) {
                return tour.getName(); // Display the name of the tour
            }

            @Override
            public Tour fromString(String string) {
                return null; // Conversion back not required for display purposes
            }
        }));
    }

    private void setupSelectionModel() {
        // Updates the selected tour in the TourDetailsViewModel
        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            TourDetailsViewModel.getInstance().setSelectedTour(newSelection);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
        setupSelectionModel();

        // Initially select the first item if available
        if (!viewModel.getTours().isEmpty()) {
            toursList.getSelectionModel().selectFirst();
        }
    }
}
