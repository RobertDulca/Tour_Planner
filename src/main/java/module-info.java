module at.technikum.tour_planner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    requires jdk.jsobject;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.swing;
    requires itextpdf;
    //requires kernel;
    //requires layout;

    opens at.technikum.tour_planner to javafx.fxml;
    opens at.technikum.tour_planner.view to javafx.fxml;
    opens at.technikum.tour_planner.entity;
    opens at.technikum.tour_planner.viewmodel to javafx.fxml;
    opens at.technikum.tour_planner.event to javafx.fxml;

    exports at.technikum.tour_planner;
    exports at.technikum.tour_planner.view;
    exports at.technikum.tour_planner.entity;
    exports at.technikum.tour_planner.event;
    exports at.technikum.tour_planner.viewmodel;
    exports at.technikum.tour_planner.service;
    exports at.technikum.tour_planner.repository;
}
