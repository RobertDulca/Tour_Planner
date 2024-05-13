package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsView implements Initializable {
    @FXML private TextField tourName, tourDesc, from, to;
    @FXML private ChoiceBox<String> transportType;
    @FXML private Button addButton, deleteButton, editButton;
    @FXML private ImageView mapImageView;
    @FXML
    protected void onEditTour() {
        tourDetailsViewModel.saveTourChanges();
        clearFormFields();
    }
    private final TourDetailsViewModel tourDetailsViewModel = TourDetailsViewModel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transportType.getItems().addAll("Select Transport", "Car", "Walk", "Bicycle");
        transportType.getSelectionModel().select("Select Transport");

        bindProperties();

        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());  // Disable edit button when no tour is selected

    }
    private void bindProperties() {
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());

        transportType.valueProperty().bindBidirectional(tourDetailsViewModel.transportTypeProperty());
        // Reset transport type to null if placeholder is selected
        tourDetailsViewModel.transportTypeProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select Transport".equals(newVal)) {
                tourDetailsViewModel.transportTypeProperty().set(null);
            }
        });

        // Bind image property to update the ImageView
        tourDetailsViewModel.imageUrlProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                // Update image with new URL
                mapImageView.setImage(new Image(newVal));
            } else {
                // Set to placeholder image if the URL is empty or null
                mapImageView.setImage(new Image("src/main/resources/img.png"));
            }
        });
    }

    @FXML
    protected void onAddTour() {
        if (!"Select Transport".equals(transportType.getValue())) {
            Tour newTour = tourDetailsViewModel.createTour();
            ToursTabViewModel.getInstance().addTour(newTour);
            clearFormFields();
        }
    }

    @FXML
    protected void onDeleteTour() {
        tourDetailsViewModel.deleteSelectedTour();
        clearFormFields();
    }

    // Clear form fields after operations
    private void clearFormFields() {
        tourName.clear();
        tourDesc.clear();
        from.clear();
        to.clear();
        transportType.getSelectionModel().select("Select Transport");
    }
}
