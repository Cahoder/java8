package designPattern.observer.listener;

import designPattern.observer.event.Event;

/**
 * 抽象事件监听者
 */
public abstract class AbstractEventListener implements EventListener {

    /**
     * 监听器执行顺序
     */
    private final int order;

    public AbstractEventListener(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public abstract void onEvent(Event event);
}
