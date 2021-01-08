package designPattern.observer.broadcast;

import designPattern.observer.event.Event;
import designPattern.observer.listener.AbstractEventListener;

import java.util.List;

/**
 * 事件广播器
 */
public class AbstractEventBroadcaster {

    /**
     * 所有的监听事件
     */
    private final List<AbstractEventListener> listeners;

    public AbstractEventBroadcaster(List<AbstractEventListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * 注册事件监听器
     * @param listener 事件监听器
     */
    public void registerListener(AbstractEventListener listener) {
        this.listeners.add(listener);
    }

    /**
     * 移除事件监听器
     * @param listener 事件监听器
     * @return 如果该监听器不存在返回false
     */
    public boolean removeListener(AbstractEventListener listener) {
        return this.listeners.remove(listener);
    }

    /**
     * 广播事件
     * @param event 事件
     */
    public void broadCastEvent(Event event){
        for (AbstractEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
