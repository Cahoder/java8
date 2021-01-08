package designPattern.observer;

import designPattern.observer.event.Event;
import designPattern.observer.listener.AbstractEventListener;

import java.util.EventObject;

/**
 * 测试监听器2
 */
public class Test2EventListener extends AbstractEventListener {
    public Test2EventListener(int order) {
        super(order);
    }

    @Override
    public void onEvent(Event event) {
        //TODO 执行事件2的业务逻辑
        System.out.println("事件监听器2执行");

        //可以获取事件持有的数据对象
        EventObject eventObject = event.getEventObject();
        System.out.println(eventObject);
    }
}
