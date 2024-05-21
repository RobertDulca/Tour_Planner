package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Event;
import at.technikum.tour_planner.event.Publisher;
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
    private final TourDetailsViewModel tourDetailsViewModel;

    @FXML private TextField tourName, tourDesc, from, to;
    @FXML private ChoiceBox<String> transportType;
    @FXML private Button addButton, deleteButton, editButton;
    @FXML private ImageView mapImageView;

    public TourDetailsView(Publisher publisher) {
        this.tourDetailsViewModel = new TourDetailsViewModel(publisher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transportType.getItems().addAll("Select Transport", "Car", "Walk", "Bicycle");
        transportType.getSelectionModel().select("Select Transport");

        bindProperties();

        addButton.disableProperty().bind(tourDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourDetailsViewModel.isTourSelectedProperty().not());
    }

    private void bindProperties() {
        tourName.textProperty().bindBidirectional(tourDetailsViewModel.tourNameProperty());
        tourDesc.textProperty().bindBidirectional(tourDetailsViewModel.tourDescriptionProperty());
        from.textProperty().bindBidirectional(tourDetailsViewModel.fromProperty());
        to.textProperty().bindBidirectional(tourDetailsViewModel.toProperty());

        transportType.valueProperty().bindBidirectional(tourDetailsViewModel.transportTypeProperty());
        tourDetailsViewModel.transportTypeProperty().addListener((obs, oldVal, newVal) -> {
            if ("Select Transport".equals(newVal)) {
                tourDetailsViewModel.transportTypeProperty().set(null);
            }
        });

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
