package at.technikum.tour_planner.view;

import at.technikum.tour_planner.event.Publisher;
import at.technikum.tour_planner.viewmodel.MenuBarViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarView implements Initializable {
    private final MenuBarViewModel viewModel;

    public MenuBarView(Publisher publisher) {
        this.viewModel = new MenuBarViewModel(publisher);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
