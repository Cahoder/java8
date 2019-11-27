package GUI.core;

import javax.swing.*;
import java.awt.*;

/**
 * 一切用户界面的基类
 */
public abstract class MyFrame extends JFrame implements EventSource {

    protected final static Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize(); //保存当前屏幕
    public MyFrame(){
        Init();
        LayOut();
        SetListeners();
        setVisible(true);
    }
    public abstract String Title();
    public abstract void Init();
    public abstract void LayOut();
    public abstract void SetListeners();
    public abstract void Reset(); //客户端重置
    public abstract <T extends MyBean> T GetModel();
}
