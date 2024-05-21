package at.technikum.tour_planner.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publisher {

    private final Map<Event, List<Subscriber>> subscriberMap;

    public Publisher() {
        subscriberMap = new HashMap<>();
    }

    public void subscribe(Event event, Subscriber subscriber) {
        List<Subscriber> subscribers = subscriberMap.get(event);

        if (subscribers == null) {
            subscribers = new ArrayList<>();
        }

        subscribers.add(subscriber);
        subscriberMap.put(event, subscribers);
    }

    public void publish(Event event, Object message) {
        List<Subscriber> subscribers = subscriberMap.get(event);

        if (subscribers == null) {
            // Log this event
            return;
        }

        for (Subscriber subscriber : subscribers) {
            subscriber.notify(message);
        }
    }
}
