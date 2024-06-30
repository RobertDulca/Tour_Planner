package at.technikum.tour_planner.view;

import at.technikum.tour_planner.TourPlannerApplication;
import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.SearchBarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;


import java.net.URL;
import java.util.ResourceBundle;

public class SearchBarView implements Initializable {
    private final SearchBarViewModel viewModel;

    @FXML
    public TextField searchBar;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private ToggleButton toggleDarkMode;

    public SearchBarView(SearchBarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSearchType();
        searchBar.textProperty().bindBidirectional(viewModel.searchTextProperty());
        searchType.valueProperty().bindBidirectional(viewModel.selectedCategoryProperty());

        // Listen for selection changes in the ComboBox to trigger the search
        searchType.setOnAction(event -> viewModel.performSearch());
        searchBar.setOnKeyPressed(event -> handleKeyPress(event));
        toggleDarkMode.setOnAction(event -> handleToggleDarkMode());
    }

    private void handleToggleDarkMode() {
        TourPlannerApplication app = (TourPlannerApplication) searchBar.getScene().getWindow().getUserData();
        if (toggleDarkMode.isSelected()) {
            app.switchToDarkMode();
            toggleDarkMode.setText("Light Mode");
        } else {
            app.switchToLightMode();
            toggleDarkMode.setText("Dark Mode");
        }
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                viewModel.performSearch();
                break;
            default:
                break;
        }
    }

    private void initializeSearchType() {
        searchType.setPromptText("Select Search");
        searchType.getItems().addAll("", "Tour", "Tour Log", "Special Attribute");
        searchType.getSelectionModel().select("Tour Name");
    }
}
