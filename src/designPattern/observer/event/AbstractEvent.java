package designPattern.observer.event;

import java.util.EventObject;

/**
 * 抽象事件
 */
public class AbstractEvent implements Event {

    private static final long serialVersionUID = -6751338731586198853L;

    private long timestamp;

    private EventObject eventObject;

    public AbstractEvent(EventObject eventObject) {
        setEventObject(eventObject);
    }

    @Override
    public EventObject getEventObject() {
        return this.eventObject;
    }

    @Override
    public void setEventObject(EventObject eventObject) {
        this.eventObject = eventObject;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp(){
        return this.timestamp;
    }
}
