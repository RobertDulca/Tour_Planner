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
        // Populate the choice box items
        transportType.getItems().addAll("Car", "Walk", "Bicycle");

        // Set initial selection to the first item to avoid null values
        transportType.getSelectionModel().selectFirst();

        // Bind properties after initializing components to avoid initial state conflicts
        bindProperties();

        // Additional UI setup not involving direct data binding
        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
    }

    private void bindProperties() {
        // Bind text properties bidirectionally
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());

        // Handle transportType with care to prevent binding issues
        transportType.valueProperty().unbind(); // Unbind any previous bindings to avoid conflicts
        transportType.valueProperty().bindBidirectional(tourDetailsViewModel.transportTypeProperty());
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
        transportType.getSelectionModel().selectFirst(); // Ensure reset to default after clearing
    }
}
