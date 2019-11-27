package GUI;

import GUI.core.MyUI;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends MyUI {

    public RegisterUI(Container container) {
        super(container);
    }

    @Override
    public void Ornament(Container container) {
        container.removeAll();
        container.setLayout(new GridLayout(10,2));   //设置成为网格布局
        String[] titles = {"员工编号","员工姓名","员工性别","出生日期(YYYY-MM-DD)","所属部门","职务","工资","学历","员工状态"};
        //对应数据库的JavaBean对象的属性,后续应该使用反射获取
        String[] tag = {"id","name","sex","birth","department","duties","salary","education","workState"};

        JComponent[][] jComponents = new JComponent[9][2];
        for (int i = 0; i < jComponents.length; i++) {
            for (int j = 0; j < jComponents[i].length; j++) {
                if (j == 0){
                    //设置标签组件和文字位置和背景颜色
                    JLabel jLabel = new JLabel(titles[i] + ":");
                    jLabel.setHorizontalAlignment(JLabel.CENTER);
                    jLabel.setVerticalAlignment(JLabel.CENTER);
                    jLabel.setHorizontalTextPosition(JLabel.CENTER);
                    jLabel.setVerticalTextPosition(JLabel.CENTER);
                    jLabel.setOpaque(true); // If true the component paints every pixel within its bounds.
                    jLabel.setBackground(Color.gray);
                    jComponents[i][j] = jLabel;
                }else {
                    JComponent jcom = null;

                    switch(i){
                        case 0 :
                        case 1 :
                        case 3 :
                        case 5 :
                        case 6 :
                            jcom = new JTextField();
                            break;
                        case 2 :
                            JComboBox<String> comboBox = new JComboBox<>();
                            comboBox.addItem("男");
                            comboBox.addItem("女");
                            jcom = comboBox;
                            break;
                        case 4 :
                            JComboBox<String> comboBox2 = new JComboBox<>();
                            comboBox2.addItem("财务部");
                            comboBox2.addItem("开发部");
                            comboBox2.addItem("运营部");
                            comboBox2.addItem("人事部");
                            comboBox2.addItem("管理部");
                            jcom = comboBox2;
                            break;
                        case 7 :
                            JComboBox<String> comboBox3 = new JComboBox<>();
                            comboBox3.addItem("小学");
                            comboBox3.addItem("初中");
                            comboBox3.addItem("高中");
                            comboBox3.addItem("大专");
                            comboBox3.addItem("本科");
                            comboBox3.addItem("硕士");
                            comboBox3.addItem("博士");
                            jcom = comboBox3;
                            break;
                        case 8 :
                            jcom = new JTextField("新入职");
                            jcom.setEnabled(false);
                            break;
                    }
                    jcom.setName(tag[i]);
                    jComponents[i][j] = jcom;
                }
                container.add(jComponents[i][j]);
            }
        }

        //添加确认取消按钮
        JButton confirm = new JButton("确认");
        confirm.setName("confirm");
        container.add(confirm);
        JButton cancel = new JButton("取消");
        cancel.setName("cancel");
        container.add(cancel);
    }

}
