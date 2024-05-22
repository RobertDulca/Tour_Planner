package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
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

    @FXML private TextField tourName, tourDesc, from, to;
    @FXML private ComboBox<String> transportType;
    @FXML private Button addButton, deleteButton, editButton;
    @FXML private ImageView mapImageView;

    // Constructor with Publisher instance as parameter
    public TourDetailsView(Publisher publisher) {
        this.tourDetailsViewModel = new TourDetailsViewModel(publisher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTransportTypeComboBox();
        bindProperties();

        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.editButtonDisabledProperty());
    }

    private void initializeTransportTypeComboBox() {
        transportType.setPromptText("Select Transport");
        transportType.getItems().addAll("Select Transport", "Car", "Walk", "Bicycle");
        transportType.getSelectionModel().select("Select Transport");
    }

    //bind UI components to ViewModel properties
    private void bindProperties() {
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());

        transportType.valueProperty().bindBidirectional(tourDetailsViewModel.transportTypeProperty());

        tourDetailsViewModel.imageUrlProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                mapImageView.setImage(new Image(newVal));
            } else {
                mapImageView.setImage(new Image("src/main/resources/img.png"));
            }
        });
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
    }

    @FXML
    protected void onAddTour() {
        Tour newTour = tourDetailsViewModel.createTour();
        tourDetailsViewModel.getPublisher().publish(Event.TOUR_CREATED, newTour);
        clearFormFields();
    }

    private void clearFormFields() {
        tourName.clear();
        tourDesc.clear();
        from.clear();
        to.clear();
        transportType.getSelectionModel().select("Select Transport");
    }
}
