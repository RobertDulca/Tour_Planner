module at.technikum.tour_planner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires jdk.jsobject;
    requires com.google.gson;

    opens at.technikum.tour_planner to javafx.fxml;
    opens at.technikum.tour_planner.view to javafx.fxml;
    opens at.technikum.tour_planner.entity to javafx.fxml;
    opens at.technikum.tour_planner.viewmodel to javafx.fxml;
    opens at.technikum.tour_planner.event to javafx.fxml;

    exports at.technikum.tour_planner;
    exports at.technikum.tour_planner.view;
    exports at.technikum.tour_planner.entity;
    exports at.technikum.tour_planner.event;
    exports at.technikum.tour_planner.viewmodel;
}
