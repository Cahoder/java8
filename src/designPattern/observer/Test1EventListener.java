package designPattern.observer;

import designPattern.observer.event.Event;
import designPattern.observer.listener.AbstractEventListener;

import java.util.EventObject;

/**
 * 测试监听器1
 */
public class Test1EventListener extends AbstractEventListener {
    public Test1EventListener(int order) {
        super(order);
    }

    @Override
    public void onEvent(Event event) {

        //TODO 执行事件1的业务逻辑
        System.out.println("事件监听器1执行");

        //可以获取事件持有的数据对象
        EventObject eventObject = event.getEventObject();
        System.out.println(eventObject);
    }
}
