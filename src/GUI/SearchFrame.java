package GUI;

import GUI.core.EventListener;
import GUI.core.MyBean;
import GUI.core.MyFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

//搜索界面
public class SearchFrame extends MyFrame {

    //设置监听对象
    public static SearchListener searchListener = null;

    private static SearchFrame instance = new SearchFrame(); //单例模式
    public static SearchFrame getInstance(){
        return instance;
    }
    private SearchFrame(){} //禁止被外部创建

    @Override
    public String Title() {
        return "员工搜索";
    }

    @Override
    public void Init() {
        setTitle(Title());
        setSize(SCREEN.width/2,SCREEN.height/2);
        setLocation(SCREEN.width/4,SCREEN.height/4);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public void LayOut() {
        new SearchUI(this.getContentPane()); //将根部容器交给UI构造
    }

    @Override
    @SuppressWarnings("unchecked")
    public void SetListeners() {
        //一定先要注册一个监听器
        searchListener = new SearchListener();
        //组件监听
        JPanel searchPanel = (JPanel) this.getContentPane().getComponent(0);
        //搜索按钮的监听
        JButton searchBtn = (JButton) searchPanel.getComponent(2);
        searchBtn.addActionListener(searchListener);
        JButton resetBtn = (JButton) searchPanel.getComponent(3);
        resetBtn.addActionListener(searchListener);
    }

    @Override
    public void Reset() {
        this.LayOut();
        this.SetListeners();
        this.revalidate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MyBean> T GetModel() {
        try {
            return (T) searchListener.getData();
        } catch (ClassCastException e){
            return null;
        }
    }

    //保存所有的事件监听对象
    private List<EventListener> LISTENERS = new ArrayList<>();
    @Override
    public void addListener(EventListener listener) {
        LISTENERS.add(listener);
    }

    @Override
    public void notifyListener() {
//        for (EventListener listener : LISTENERS) {
//            SQLEvent event = new SQLEvent();
//            event.setSource(this);
//            listener.handleEvent(event);
//        }
    }
}
