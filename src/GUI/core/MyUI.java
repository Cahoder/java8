package GUI.core;

import java.awt.*;

/**
 * 所有静态页面的样式界面基类
 */
public abstract class MyUI {
    public MyUI(Container container){
        Ornament(container);
    }
    public abstract void Ornament(Container container);
}
