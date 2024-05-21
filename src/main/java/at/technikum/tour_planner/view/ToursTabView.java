package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Publisher;
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

    @FXML private ListView<Tour> toursList;

    // Constructor with Publisher instance as parameter
    public ToursTabView(Publisher publisher) {
        this.viewModel = new ToursTabViewModel(publisher);
    }

    //
    private void setupListView() {
        // cell factory to display the name of the tour in the list view
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
            // tour object to string
            @Override
            public String toString(Tour tour) {
                return tour.getName();
            }

            // string to tour object
            @Override
            public Tour fromString(String string) {
                return null;
            }
        }));

        // If a tour is selected, call viewModel's selectTour with Tour object.
        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewModel.selectTour(newSelection);
            }
            // else clear the selected tour
            else {
                viewModel.clearSelectedTour();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
    }
}

