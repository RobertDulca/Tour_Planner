package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TourDetailsView implements Initializable {
    private final TourDetailsViewModel tourDetailsViewModel;
    private static final Logger logger = Logger.getLogger(TourDetailsView.class.getName());

    @FXML
    public TextField tourName;
    @FXML
    public TextField tourDesc;
    @FXML
    public TextField from;
    @FXML
    public TextField to;
    @FXML
    private TextField tourDistance;
    @FXML
    private TextField estimatedTime;
    @FXML
    public ComboBox<String> transportType;
    @FXML
    private Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private ImageView mapImageView;

    public TourDetailsView(TourDetailsViewModel toursTabViewModel) {
        this.tourDetailsViewModel = toursTabViewModel;
        logger.info("TourDetailsView initialized.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTransportTypeComboBox();
        bindProperties();
        setupButtonBindings();
        logger.info("TourDetailsView initialized with transport types and property bindings.");
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

        // Convert the image path to an Image object and bind it to the ImageView
        ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> {
            String imagePath = tourDetailsViewModel.imageProperty().get();
            if (imagePath == null || imagePath.isEmpty()) {
                return null;
            }
            return new Image(new File(imagePath).toURI().toString());
        }, tourDetailsViewModel.imageProperty());

        mapImageView.imageProperty().bind(imageBinding);
    }

    private void setupButtonBindings() {
        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.editButtonDisabledProperty());
    }

    @FXML
    public void onAddTour() {
        try {
            tourDetailsViewModel.createAndPublishTour();
            clearFormFields();
            logger.info("New tour added successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create new tour", e);
            tourDetailsViewModel.showAlert("Failed to create new tour: " + e.getMessage());
        }
    }

    @FXML
    private void onEditTour() {
        try {
            tourDetailsViewModel.saveTourChanges();
            Tour editedTour = tourDetailsViewModel.getSelectedTour();
            if (editedTour != null) {
                tourDetailsViewModel.fetchAndSetMapImage(editedTour);
            }
            clearFormFields();
            logger.info("Tour edited successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update the tour", e);
            tourDetailsViewModel.showAlert("Failed to update the tour: " + e.getMessage());
        }
    }

    @FXML
    public void onDeleteTour() {
        tourDetailsViewModel.deleteSelectedTour();
        clearFormFields();
        tourDetailsViewModel.clearTourSelection();
        logger.info("Tour deleted successfully.");
    }

    private void clearFormFields() {
        tourName.clear();
        tourDesc.clear();
        from.clear();
        to.clear();
        transportType.getSelectionModel().select("Select Type");
        tourDetailsViewModel.tourDistanceProperty().set(0);
        tourDetailsViewModel.estimatedTimeProperty().set(0);
        tourDetailsViewModel.imageProperty().set(null);
        logger.info("Form fields cleared.");
    }
}