package at.technikum.tour_planner.event;

public interface Subscriber {
    void notify(Object message);
}
