package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.TourLogDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TourLogDetailsView implements Initializable {
    private final TourLogDetailsViewModel tourLogDetailsViewModel;
    private static final Logger logger = Logger.getLogger(TourLogDetailsView.class.getName());

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
        logger.info("TourLogDetailsView initialized.");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
        setupButtonBindings();
        logger.info("TourLogDetailsView initialized with property bindings and button bindings.");
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
        try {
            this.tourLogDetailsViewModel.createTourLog();
            clearFormFields();
            logger.info("Tour log created.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating tour log.", e);
            tourLogDetailsViewModel.showAlert("Error creating tour log" + e.getMessage());
        }
    }

    @FXML
    public void updateTourLog() {
        this.tourLogDetailsViewModel.updateTourLog();
        clearFormFields();
        logger.info("Tour log updated.");
    }

    @FXML
    public void deleteTourLog() {
        this.tourLogDetailsViewModel.deleteSelectedTourLog();
        clearFormFields();
        logger.info("Tour log deleted.");
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
