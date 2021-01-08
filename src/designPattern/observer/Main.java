package designPattern.observer;

import designPattern.observer.listener.AbstractEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    //测试用例
    public static void main(String[] args) {
        //定义三个事件监听器
        List<AbstractEventListener> listeners = new ArrayList<>();
        listeners.add(new Test1EventListener(2));
        listeners.add(new Test3EventListener(1));
        listeners.add(new Test2EventListener(3));

        //可以根据listener里面的order做排序
        listeners = listeners.stream()
                .sorted(Comparator.comparing(AbstractEventListener::getOrder))
                .collect(Collectors.toList());

        //在广播器中注册监听器
        TestEventBroadcaster broadcaster = new TestEventBroadcaster(listeners);
        EventObject eventObject = new EventObject("Time:"+System.currentTimeMillis());

        //广播事件
        broadcaster.broadCastEvent(new TestEvent(eventObject));
    }
}
