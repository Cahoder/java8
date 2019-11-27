package GUI.core;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件
 */
public interface Event  extends Serializable {
    /**
     * 何时 何地 何人 发生此事件
     */
    Object getSource();
    Date getWhen();
    String getMessage();
    /**
     * 事件回调方法
     */
    void callback();
}
