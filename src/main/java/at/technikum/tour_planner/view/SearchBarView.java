package at.technikum.tour_planner.view;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.SearchBarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchBarView implements Initializable {
    private final SearchBarViewModel viewModel;

    @FXML
    public TextField searchBar;
    @FXML
    private Button searchButton;

    public SearchBarView(Publisher publisher) {
        this.viewModel = new SearchBarViewModel(publisher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBar.textProperty().bindBidirectional(viewModel.searchTextProperty());
        searchButton.disableProperty().bind(viewModel.searchButtonDisabledProperty());
    }
}
