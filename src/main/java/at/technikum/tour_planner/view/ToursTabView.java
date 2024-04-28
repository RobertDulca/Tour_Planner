package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ToursTabView {

    private final ToursTabViewModel viewModel;
    public ToursTabView(){
        viewModel = new ToursTabViewModel();
    }

    @FXML
    public Button addTourButton;
    @FXML
    public Button removeTourButton;
    @FXML
    public Button editTourButton;
    @FXML
    private ListView <String> toursList;

    @FXML
    public void onAddTour() {
        viewModel.addTour("New Tour");
    }

    @FXML
    public void onRemoveTour() {
        String selectedTour = toursList.getSelectionModel().getSelectedItem();
        viewModel.removeTour(selectedTour);
    }

    @FXML
    public void onEditTour() {
        //Edit Logic
    }
    @FXML
    public void initialize() {
        toursList.setItems(viewModel.getTours());
    }
}
