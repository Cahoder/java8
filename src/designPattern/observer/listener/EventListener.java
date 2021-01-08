package designPattern.observer.listener;

import designPattern.observer.event.Event;

/**
 * 事件监听接口类
 */
public interface EventListener {

    /**
     * 触发事件方法
     * @param event 事件
     */
    void onEvent(Event event);
}
