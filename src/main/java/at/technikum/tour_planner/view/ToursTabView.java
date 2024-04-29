package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.ToursTabViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ToursTabView implements Initializable {

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
    public void initialize(URL location, ResourceBundle resources) {
        toursList.setItems(viewModel.getTours());
    }
}
