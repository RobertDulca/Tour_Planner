package at.technikum.tour_planner.view;

import at.technikum.tour_planner.viewmodel.MenuBarViewModel;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarView {

    private final MenuBarViewModel viewModel;
    public MenuBarView() {
        this.viewModel = new MenuBarViewModel();
    }

    @FXML
    protected void onNewTour() {
        viewModel.createNewTour();
    }
    @FXML
    protected void onExit() {
        viewModel.exitApplication();
    }
    @FXML
    protected void onCopy() {
        viewModel.copyData();
    }
    @FXML
    protected void onPaste() {
        viewModel.pasteData();
    }
    @FXML
    protected void onDelete() {
        viewModel.deleteData();
    }


    public void initialize(URL location, ResourceBundle resources) {}

}
