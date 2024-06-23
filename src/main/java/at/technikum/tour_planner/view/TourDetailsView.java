package at.technikum.tour_planner.view;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.TourDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailsView implements Initializable {

    private final TourDetailsViewModel tourDetailsViewModel;

    @FXML private TextField tourName, tourDesc, from, to, tourDistance, estimatedTime;
    @FXML private ComboBox<String> transportType;
    @FXML private Button addButton, deleteButton, editButton;
    @FXML private ImageView mapImageView;

    public TourDetailsView(Publisher publisher) {
        this.tourDetailsViewModel = new TourDetailsViewModel(publisher);
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

        tourDetailsViewModel.imageUrlProperty().addListener((obs, oldVal, newVal) -> mapImageView.setImage(newVal != null && !newVal.isEmpty() ? new Image(newVal) : new Image("src/main/resources/img.png")));
    }

    private void setupButtonBindings() {
        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.editButtonDisabledProperty());
    }

    @FXML
    protected void onAddTour() {
        tourDetailsViewModel.createAndPublishTour();
        clearFormFields();
    }

    @FXML
    protected void onEditTour() {
        tourDetailsViewModel.saveTourChanges();
        clearFormFields();
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
    }
}