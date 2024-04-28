package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.SearchBarViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchBarView {
    private SearchBarViewModel viewModel;
    public SearchBarView(){
        viewModel = new SearchBarViewModel();
    }
    @FXML
    public TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("TESTPRINT");
        searchBar.textProperty().bindBidirectional(viewModel.searchTextProperty());
        searchButton.disableProperty().bind(viewModel.searchButtonDisabledProperty());
    }
}
