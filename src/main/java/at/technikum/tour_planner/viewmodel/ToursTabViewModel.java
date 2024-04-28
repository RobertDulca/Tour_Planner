package at.technikum.tour_planner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ToursTabViewModel {

    private ObservableList<String> tours = FXCollections.observableArrayList();

    public ObservableList<String> getTours() {
        return tours;
    }

    public void addTour(String tour){
        tours.add(tour);
    }
    public void removeTour(String tour){
        tours.remove(tour);
    }
    public void editTour(String tour){
        //Edit Logic
    }
}
