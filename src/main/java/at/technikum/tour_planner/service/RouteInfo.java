package at.technikum.tour_planner.service;

public class RouteInfo {
    private final double distance;
    private final double duration;

    public RouteInfo(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }
}
