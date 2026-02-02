package raindrop.game;

import javafx.event.Event;
import javafx.event.EventType;

public class DropMissedEvent extends Event {
    public static final EventType<DropMissedEvent> DROP_MISSED = new EventType<>(Event.ANY, "DROP_MISSED");
    public DropMissedEvent() {
        super(DROP_MISSED);
    }
}
