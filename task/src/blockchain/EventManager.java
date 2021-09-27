package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<Event, List<EventListener>> listeners;

    public EventManager(Event... events) {
        this.listeners = new HashMap<>();
        for (var event : events) {
            this.listeners.put(event, new ArrayList<>());
        }
    }

    public void subscribe(EventListener listener, Event... events) {
        for (var event : events) {
            var eventListeners = this.listeners.get(event);
            if (eventListeners != null) {
                eventListeners.add(listener);
            } else {
                eventListeners = new ArrayList<>();
                eventListeners.add(listener);
                this.listeners.put(event, eventListeners);
            }
        }
    }

    public void unsubscribe(EventListener listener, Event event) {
        var eventListeners = this.listeners.get(event);
        if (eventListeners == null) {
            return;
        }
        eventListeners.remove(listener);
    }

    public void notify(Event event) {
        var eventListeners = this.listeners.get(event);
        if (eventListeners == null) {
            return;
        }

        for (var listener : eventListeners) {
            listener.update(event);
        }
    }
}