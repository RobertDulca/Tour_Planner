package at.technikum.tour_planner.event;

import java.util.*;

public class Publisher {
    private final Map<Event, List<Subscriber>> subscriberMap = new HashMap<>();

    public void subscribe(Event event, Subscriber subscriber) {
        subscriberMap.computeIfAbsent(event, k -> new ArrayList<>()).add(subscriber);
    }

    public void publish(Event event, Object message) {
        List<Subscriber> subscribers = subscriberMap.get(event);
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.notify(message);
            }
        }
    }
}