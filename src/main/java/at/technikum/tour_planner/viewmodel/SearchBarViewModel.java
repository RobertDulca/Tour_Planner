package at.technikum.tour_planner.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchBarViewModel {
    private StringProperty searchText = new SimpleStringProperty("");
    private BooleanProperty disabledSearchButton = new SimpleBooleanProperty(true);

    public SearchBarViewModel() {
        searchText.addListener((obs, oldVal, newVal) -> {
            boolean isDisabled = newVal == null || newVal.trim().isEmpty();
            disabledSearchButton.set(isDisabled);
        });
    }
    public StringProperty searchTextProperty() {
        return searchText;
    }

    public BooleanProperty searchButtonDisabledProperty() {
        return disabledSearchButton;
    }
}
