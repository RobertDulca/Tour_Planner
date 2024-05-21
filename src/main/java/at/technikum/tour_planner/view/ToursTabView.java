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

    public ToursTabView(Publisher publisher) {
        this.viewModel = new ToursTabViewModel(publisher);
    }

    private void setupListView() {
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(Tour tour) {
                return tour.getName();
            }

            @Override
            public Tour fromString(String string) {
                return null;
            }
        }));

        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewModel.selectTour(newSelection);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
    }
}
