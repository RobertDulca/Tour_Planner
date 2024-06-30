package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.entity.TourLogModel;
import at.technikum.tour_planner.viewmodel.TourLogOverviewViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TourLogOverviewView implements Initializable {
    private final TourLogOverviewViewModel tourLogOverviewViewModel;
    @FXML
    public ListView<TourLogModel> tourLogList;

    public TourLogOverviewView(TourLogOverviewViewModel tourLogOverviewViewModel) {
        this.tourLogOverviewViewModel = tourLogOverviewViewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tourLogList.setItems(tourLogOverviewViewModel.getTourLogs());
        setupListView();
    }

    private void setupListView() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Set a custom cell factory to display the tour logs
        tourLogList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(TourLogModel tourLog) {
                // Customize this to display the relevant information from TourLogModel
                return String.format("Date: %s, Difficulty: %d, Rating: %d",
                        tourLog.getDate() != null ? tourLog.getDate().format(dateFormatter) : "No Date",
                        tourLog.getDifficulty(),
                        tourLog.getRating());
            }

            @Override
            public TourLogModel fromString(String string) {
                return null; // No need to convert from String to TourLogModel
            }
        }));

        tourLogList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tourLogOverviewViewModel.selectTourLog(newSelection);
            } else {
                tourLogOverviewViewModel.clearSelectedTourLog();
            }
        });

        // Clear selection when a tour is removed or updated
        tourLogOverviewViewModel.getTourLogs().addListener((ListChangeListener<TourLogModel>) change -> {
            if (change.next() && (change.wasRemoved() || change.wasUpdated())) {
                tourLogList.getSelectionModel().clearSelection();
            }
        });
    }
}
