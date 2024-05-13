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

    // Singleton instance
    private final ToursTabViewModel viewModel = ToursTabViewModel.getInstance();

    @FXML private ListView<Tour> toursList;

    private void setupListView() {
        // Set cell factory that uses TextFieldListCell to display the tour names
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(Tour tour) {
                return tour.getName(); // Display tour name
            }

            // convert back to Tour object
            @Override
            public Tour fromString(String string) {
                return null;
            }
        }));
    }

    // behavior when a tour is selected/deselected
    private void setupSelectionModel() {
        // listener to selectedItemProperty
        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // when new tour is selected:
            if (newSelection != null) {
                // Tell TourDetailsViewModel which tour is selected
                TourDetailsViewModel.getInstance().setSelectedTour(newSelection);
            } else {
                // Clear view if no tour selected
                TourDetailsViewModel.getInstance().clearTourDetails();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
        setupSelectionModel();
    }
}

