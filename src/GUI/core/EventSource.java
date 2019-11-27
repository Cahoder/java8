package GUI.core;

/**
 * 事件源
 */
public interface EventSource {
    /**
     * 增加监听器
     */
    void addListener(EventListener listener);

    /**
     * 通知监听器
     */
    void notifyListener();
}
