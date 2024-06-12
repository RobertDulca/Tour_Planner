package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.viewmodel.TourLogOverviewViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TourLogOverviewView implements Initializable {
    private final TourLogOverviewViewModel tourLogOverviewViewModel;
    @FXML
    public ListView<LocalDate> tourLogList;

    public TourLogOverviewView(TourLogOverviewViewModel tourLogOverviewViewModel) {
        this.tourLogOverviewViewModel = tourLogOverviewViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tourLogList.setItems(tourLogOverviewViewModel.getTourLogs());
        this.tourLogOverviewViewModel.selectedTourLogIndexProperty().bind(tourLogList.getSelectionModel().selectedIndexProperty());
    }
}
