module at.technikum.tour_planner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;


    opens at.technikum.tour_planner to javafx.fxml;
    opens at.technikum.tour_planner.view to javafx.fxml;
    exports at.technikum.tour_planner;
    exports at.technikum.tour_planner.view;
    exports at.technikum.tour_planner.viewmodel;
    exports at.technikum.tour_planner.entity;
    opens at.technikum.tour_planner.entity to javafx.fxml;
}