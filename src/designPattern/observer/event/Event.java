package designPattern.observer.event;

import java.io.Serializable;
import java.util.EventObject;

/**
 * 事件接口
 */
public interface Event extends Serializable {

    /**
     * 获取事件持有的数据对象
     * @return EventObject
     */
    EventObject getEventObject();

    /**
     * 设置事件持有的数据对象
     * @param eventObject 数据对象
     */
    void setEventObject(EventObject eventObject);
}
