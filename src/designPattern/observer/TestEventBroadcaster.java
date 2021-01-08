package designPattern.observer;

import designPattern.observer.broadcast.AbstractEventBroadcaster;
import designPattern.observer.listener.AbstractEventListener;

import java.util.List;

/**
 * 测试事件广播器
 */
public class TestEventBroadcaster extends AbstractEventBroadcaster {
    public TestEventBroadcaster(List<AbstractEventListener> listeners) {
        super(listeners);
    }
}
