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
    private final ToursTabViewModel viewModel = ToursTabViewModel.getInstance();

    @FXML private ListView<Tour> toursList;

    private void setupListView() {
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
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
        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                TourDetailsViewModel.getInstance().setSelectedTour(newSelection);
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
