package at.technikum.tour_planner.view;

import at.technikum.tour_planner.entity.Tour;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ToursTabView implements Initializable {
    private final ToursTabViewModel viewModel;
    private static final Logger logger = Logger.getLogger(ToursTabView.class.getName());

    @FXML private ListView<Tour> toursList;

    public ToursTabView(ToursTabViewModel tourLogOverviewViewModel) {
        this.viewModel = tourLogOverviewViewModel;
        logger.info("ToursTabView initialized.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
        setupListView();
        logger.info("ToursTabView initialized with tour list and setup.");
    }

    private void setupListView() {
        toursList.setCellFactory(lv -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(Tour tour) {
                return tour.getName();
            }

            @Override
            public Tour fromString(String string) {
                return null;
            }
        }));

        toursList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                viewModel.selectTour(newSelection);
                logger.info("Tour selected: " + newSelection.getName());
            } else {
                viewModel.clearSelectedTour();
                logger.info("Tour selection cleared.");
            }
        });

        viewModel.getTours().addListener((ListChangeListener<Tour>) change -> {
            if (change.next() && (change.wasRemoved() || change.wasUpdated())) {
                toursList.getSelectionModel().clearSelection();
                logger.info("Tours list updated or tour removed. Selection cleared.");
            }
        });
    }

}
