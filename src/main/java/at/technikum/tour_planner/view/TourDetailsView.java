package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsView implements Initializable {
    @FXML private TextField tourName, tourDesc, from, to;
    @FXML private ChoiceBox<String> transportType;
    @FXML private Button addButton, deleteButton;
    private final TourDetailsViewModel tourDetailsViewModel = TourDetailsViewModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        transportType.getItems().setAll("Car", "Walk", "Bicycle");
        transportType.getSelectionModel().selectFirst();
        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
    }

    private void bindProperties() {
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());
    }

    @FXML
    protected void onAddTour() {
        Tour newTour = tourDetailsViewModel.createTour();
        ToursTabViewModel.getInstance().addTour(newTour);
        clearFormFields();
    }

    @FXML
    protected void onDeleteTour() {
        tourDetailsViewModel.deleteSelectedTour();
        clearFormFields();
    }

    private void clearFormFields() {
        tourName.clear();
        tourDesc.clear();
        from.clear();
        to.clear();
        transportType.getSelectionModel().selectFirst();
    }
}
