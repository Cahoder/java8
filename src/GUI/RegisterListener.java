package GUI;

import GUI.core.Event;
import GUI.core.MyBean;
import GUI.core.MyListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterListener extends MyListener implements ActionListener,ItemListener,FocusListener,MenuListener {
    private StaffBean model = new StaffBean();  //用于保存模型数据

    @Override
    public void handleEvent(Event event) {
        event.callback();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton)e.getSource();
        switch (jb.getName()){
            case "confirm":
                int re = JOptionPane.showConfirmDialog(jb.getParent(),"确认是否添加职员","确认对话框",JOptionPane.YES_NO_OPTION);
                if (re == JOptionPane.YES_OPTION){
                    //如果用户按了确认按钮
                    try {
                        model.checkSubmit();

                        //数据验证通过开始与数据库后台交互
                        SQLEvent myEvent = new SQLEvent();
                        myEvent.setSource(e.getSource(),getData());
                        handleEvent(myEvent);
                    } catch (IllegalArgumentException ale){
                        JOptionPane.showMessageDialog(jb.getParent(),ale.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "cancel":
                break;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            JComboBox<String> jcb = (JComboBox<String>)e.getSource();
            switch (jcb.getName()){
                case "sex":
                    model.setSex(jcb.getSelectedIndex());
                    break;
                case "department":
                    model.setDepartment(jcb.getSelectedIndex());
                    break;
                case "education":
                    model.setEducation(jcb.getSelectedIndex());
                    break;
            }
        }
    }

    @Override
    public MyBean getData() {
        return model;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        JTextField jtf = (JTextField)e.getSource();
        switch (e.getComponent().getName()){
            case "id":
                try {
                    int id = Integer.valueOf(jtf.getText());
                    model.setId(id);
                } catch (NumberFormatException ex){
                    model.setId(0);
                    jtf.setText("");
                }
                break;
            case "salary":
                try {
                    int money = Integer.valueOf(jtf.getText());
                    model.setSalary(money);
                } catch (NumberFormatException ex){
                    model.setSalary(0);
                    jtf.setText("");
                }
                break;
            case "name":
                model.setName(jtf.getText());
                break;
            case "duties":
                model.setDuties(jtf.getText());
                break;
            case "birth":
                try {
                    if (jtf.getText().equals("")) break;
                    if (model.getBirth() == null || !model.getBirth().toString().equals(jtf.getText())){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        model.setBirth(new java.sql.Date(sdf.parse(jtf.getText()).getTime()));
                        jtf.setText(model.getBirth().toString());
                    }
                }catch (ParseException exception){
                    model.setBirth(null);
                    jtf.setText("");
                }
                break;
        }
    }

    @Override
    public void menuSelected(MenuEvent e) {
        SearchFrame.getInstance().setVisible(true);
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
