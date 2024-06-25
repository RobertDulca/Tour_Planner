package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsView implements Initializable {
    private final TourDetailsViewModel tourDetailsViewModel;

    @FXML private TextField tourName, tourDesc, from, to, tourDistance, estimatedTime;
    @FXML private ComboBox<String> transportType;
    @FXML private Button addButton, deleteButton, editButton;
    @FXML private ImageView mapImageView;

    public TourDetailsView(TourDetailsViewModel toursTabViewModel) {
        this.tourDetailsViewModel = toursTabViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTransportTypeComboBox();
        bindProperties();
        setupButtonBindings();
    }

    private void initializeTransportTypeComboBox() {
        transportType.setPromptText("Select Transport");
        transportType.getItems().addAll("Select Type", "Car", "Bicycle", "Walk");
        transportType.getSelectionModel().select("Select Type");
    }

    private void bindProperties() {
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());
        transportType.valueProperty().bindBidirectional(tourDetailsViewModel.transportTypeProperty());
        tourDistance.textProperty().bind(tourDetailsViewModel.tourDistanceProperty().asString());
        estimatedTime.textProperty().bind(tourDetailsViewModel.estimatedTimeProperty().asString());

        mapImageView.imageProperty().bind(tourDetailsViewModel.imageProperty());
    }

    private void setupButtonBindings() {
        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.editButtonDisabledProperty());
    }

    @FXML
    private void onAddTour() {
        try {
            tourDetailsViewModel.createAndPublishTour();
            clearFormFields();
        } catch (Exception e) {
            tourDetailsViewModel.showAlert("Failed to create new tour: " + e.getMessage());
        }
    }

    @FXML
    private void onEditTour() {
        try {
            tourDetailsViewModel.saveTourChanges();
            clearFormFields();
            Tour editedTour = tourDetailsViewModel.getSelectedTour();
            if (editedTour != null) {
                tourDetailsViewModel.fetchAndSetMapImage(editedTour);
            }
        } catch (Exception e) {
            tourDetailsViewModel.showAlert("Failed to update the tour: " + e.getMessage());
        }
    }

    @FXML
    protected void onDeleteTour() {
        tourDetailsViewModel.deleteSelectedTour();
        clearFormFields();
        tourDetailsViewModel.clearTourSelection();
    }

    private void clearFormFields() {
        tourName.clear();
        tourDesc.clear();
        from.clear();
        to.clear();
        transportType.getSelectionModel().select("Select Type");
        tourDetailsViewModel.tourDistanceProperty().set(0);
        tourDetailsViewModel.estimatedTimeProperty().set(0);
    }
}