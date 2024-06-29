package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.TourLogDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogDetailsView implements Initializable {
    private final TourLogDetailsViewModel tourLogDetailsViewModel;

    @FXML
    private DatePicker tourLogDate;
    @FXML
    private Slider difficulty;
    @FXML
    private TextField tourLogTime;
    @FXML
    private TextField tourLogDistance;
    @FXML
    private Rating tourLogRating;
    @FXML
    private TextArea tourLogComment;
    @FXML
    private Button addButton, deleteButton, editButton;

    public TourLogDetailsView(TourLogDetailsViewModel tourLogViewModel) {
        this.tourLogDetailsViewModel = tourLogViewModel;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        setupButtonBindings();
    }

    private void bindProperties() {
        this.tourLogDate.valueProperty().bindBidirectional(tourLogDetailsViewModel.dateProperty());
        this.difficulty.valueProperty().bindBidirectional(tourLogDetailsViewModel.difficultyProperty());
        this.tourLogTime.textProperty().bindBidirectional(tourLogDetailsViewModel.totalTimeProperty(), new NumberStringConverter());
        this.tourLogDistance.textProperty().bindBidirectional(tourLogDetailsViewModel.totalDistanceProperty(), new NumberStringConverter());
        this.tourLogRating.ratingProperty().bindBidirectional(tourLogDetailsViewModel.ratingProperty());
        this.tourLogComment.textProperty().bindBidirectional(tourLogDetailsViewModel.commentProperty());
    }

    private void setupButtonBindings() {
        addButton.disableProperty().bind(tourLogDetailsViewModel.addButtonDisabledProperty());
        deleteButton.disableProperty().bind(tourLogDetailsViewModel.isTourSelectedProperty().not());
        editButton.disableProperty().bind(tourLogDetailsViewModel.editButtonDisabledProperty());
    }

    @FXML
    public void createTourLog() {
        this.tourLogDetailsViewModel.createTourLog();
        clearFormFields();
    }

    @FXML
    public void updateTourLog() {
        this.tourLogDetailsViewModel.updateTourLog();
        clearFormFields();
    }

    @FXML
    public void deleteTourLog() {
        this.tourLogDetailsViewModel.deleteSelectedTourLog();
        clearFormFields();
    }

    private void clearFormFields() {
        tourLogDate.setValue(null);
        difficulty.setValue(0);
        tourLogTime.clear();
        tourLogDistance.clear();
        tourLogRating.setRating(0);
        tourLogComment.clear();
    }
}
