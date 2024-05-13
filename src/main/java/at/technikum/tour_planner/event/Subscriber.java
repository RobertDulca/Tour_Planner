package at.technikum.tour_planner.event;

public interface Subscriber {

    void notify(String message);

    void notify(Object object);
}
