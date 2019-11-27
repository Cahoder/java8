package GUI;


import GUI.core.EventListener;
import GUI.core.MyBean;
import GUI.core.MyFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

//新增界面
public class RegisterFrame extends MyFrame {

    //设置监听对象
    public static RegisterListener registerListener = null;

    public RegisterFrame(){}

    public RegisterFrame(int x,int y,int w,int h){
        setSize(w, h);
        setLocation(x, y);
    }

    @Override
    public void Reset(){
        this.LayOut();
        this.SetListeners();
        this.revalidate();
    }

    @Override
    public String Title() {
        return "员工登记";
    }

    @Override
    public void Init() {
        setTitle(Title());
        setSize(SCREEN.width/2,SCREEN.height/2);
        setLocation(SCREEN.width/4,SCREEN.height/4);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //添加菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("查询功能");
        menu.setName("search");
        menuBar.add(menu);
        menu = new JMenu("关于");
        menu.setName("about");
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    @Override
    public void LayOut() {
        new RegisterUI(this.getContentPane()); //将根部容器交给UI构造
    }

    @Override
    @SuppressWarnings("unchecked")
    public void SetListeners() {
        //一定先要注册一个监听器
        registerListener = new RegisterListener();
        //输入框监听
        JTextField jTextField = (JTextField)this.getContentPane().getComponent(1);
        jTextField.addFocusListener(registerListener);
        jTextField = (JTextField)this.getContentPane().getComponent(3);
        jTextField.addFocusListener(registerListener);
        jTextField = (JTextField)this.getContentPane().getComponent(7);
        jTextField.addFocusListener(registerListener);
        jTextField = (JTextField)this.getContentPane().getComponent(11);
        jTextField.addFocusListener(registerListener);
        jTextField = (JTextField)this.getContentPane().getComponent(13);
        jTextField.addFocusListener(registerListener);
        //下拉选项监听
        JComboBox<String> comboBox = (JComboBox<String>)this.getContentPane().getComponent(5);
        comboBox.addItemListener(registerListener);
        comboBox = (JComboBox<String>)this.getContentPane().getComponent(9);
        comboBox.addItemListener(registerListener);
        comboBox = (JComboBox<String>)this.getContentPane().getComponent(15);
        comboBox.addItemListener(registerListener);
        //按钮监听
        JButton jButton = (JButton)this.getContentPane().getComponent(18);
        jButton.addActionListener(registerListener);
        jButton = (JButton)this.getContentPane().getComponent(19);
        jButton.addActionListener(registerListener);
        //菜单监听
        this.getJMenuBar().getMenu(0).addMenuListener(registerListener);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends MyBean> T GetModel() {
        try {
            return (T) registerListener.getData();
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
