package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.TourLogDetailsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private Rating tourLogRating;
    @FXML
    private TextArea tourLogComment;

    public TourLogDetailsView() {
        this.tourLogDetailsViewModel = new TourLogDetailsViewModel();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindProperties();
    }

    private void bindProperties() {
        tourLogDate.valueProperty().bindBidirectional(tourLogDetailsViewModel.dateProperty());
        difficulty.valueProperty().bindBidirectional(tourLogDetailsViewModel.difficultyProperty());
        tourLogTime.textProperty().bindBidirectional(tourLogDetailsViewModel.totalTimeProperty(), new NumberStringConverter());
        tourLogRating.ratingProperty().bindBidirectional(tourLogDetailsViewModel.ratingProperty());
        tourLogComment.textProperty().bindBidirectional(tourLogDetailsViewModel.commentProperty());
    }
}
