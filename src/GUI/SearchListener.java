package GUI;

import GUI.core.Event;
import GUI.core.MyBean;
import GUI.core.MyListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SearchListener extends MyListener implements ActionListener {
    private HashMap<String,String> condition = new HashMap<>();  //用于保存条件数据
    private StaffBean model = new StaffBean();  //用于保存模型数据
    private static String ErrorMsg = "";

    @Override
    public MyBean getData() {
        return model;
    }

    @Override
    public void handleEvent(Event event) {
        event.callback();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton)e.getSource();

        switch (btn.getName()){
            case "search":
                condition.clear(); //清空条件
                ErrorMsg = ""; //清空错误信息
                JPanel searchPanel = (JPanel) btn.getParent();

                JTextField jtf = (JTextField) searchPanel.getComponent(0);
                JComboBox<String> comboBox = (JComboBox<String>) searchPanel.getComponent(1);
                if (jtf.getText().trim().equals("")) {
                    //输入框没有东西
                    ErrorMsg = comboBox.getSelectedItem()+"不可为空";
                    JOptionPane.showMessageDialog(searchPanel.getParent(),ErrorMsg,"error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String text = jtf.getText().trim();
                switch (comboBox.getSelectedIndex()){
                    case 0: //选的是员工编号
                        try {
                            Integer.valueOf(text);
                            condition.put("id",text);
                        } catch (NumberFormatException ex){
                            ErrorMsg = "员工编号只能为数字";
                        }
                        break;
                    case 1: //选的是员工姓名
                        condition.put("name",text);
                        break;
                    case 2: //选的是员工性别
                        if (text.hashCode() == "男".hashCode()) {
                            condition.put("sex",0+"");
                        }
                        else if (text.hashCode() == "女".hashCode()) {
                            condition.put("sex",1+"");
                        }
                        else
                            ErrorMsg = "员工性别输入有误";
                        break;
                    case 3: //选的是员工部门
                        String[] allDepartments = model.getALLDepartments();
                        ErrorMsg = "员工部门不存在！";
                        for (int i = 0; i < allDepartments.length; i++) {
                            System.out.println();
                            if (allDepartments[i].hashCode() == text.hashCode()){
                                condition.put("department",i+"");
                                ErrorMsg = "";
                                break;
                            }
                        }
                        break;
                }

                if (!ErrorMsg.equals("")) {JOptionPane.showMessageDialog(searchPanel.getParent(),ErrorMsg,"error",JOptionPane.ERROR_MESSAGE); return;}

                //数据验证通过开始与数据库后台交互
                SQLEvent myEvent = new SQLEvent();
                myEvent.setSource(e.getSource(), condition);
                handleEvent(myEvent);
                break;
            case "searchALL":
                if (condition.size()<1) break;
                //数据验证通过开始与数据库后台交互
                SQLEvent myEvent2 = new SQLEvent();
                myEvent2.setSource(e.getSource());
                handleEvent(myEvent2);
                break;
        }
    }
}
