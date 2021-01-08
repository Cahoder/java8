package designPattern.observer;

import designPattern.observer.event.AbstractEvent;

import java.util.EventObject;

/**
 * 测试事件
 */
public class TestEvent extends AbstractEvent {

    private static final long serialVersionUID = -8883128552095889609L;

    public TestEvent(EventObject eventObject) {
        super(eventObject);
    }

}
