package GUI;

import GUI.core.MyUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class SearchUI extends MyUI {

    public static Object[][] data = new Object[1][9];
    public final static Object[] titles = {"员工编号","员工姓名","员工性别","出生日期","所属部门","职务","工资","学历","员工状态"};
    static {
        if (SQLEvent.mysql != null){
            try {
                data = SQLEvent.search();
            } catch (SQLException e) {
                String[][] error = {{"数","据","库","连","接","失","败","～","～"}};
                data = error;
            }
        }
    }
    public final static DefaultTableModel tableModel = new DefaultTableModel(data,titles);

    

    public SearchUI(Container container) {
        super(container);
    }

    @Override
    public void Ornament(Container container) {
        container.removeAll();

        JPanel pNorth = new JPanel();
        pNorth.setPreferredSize(new Dimension(0,30));
        pNorth.setLayout(new GridLayout(1,4));
        JTextField textInput = new JTextField();
        textInput.setName("searchInput");
        JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setName("selected");
        jComboBox.addItem("员工编号");
        jComboBox.addItem("员工姓名");
        jComboBox.addItem("员工性别");
        jComboBox.addItem("员工部门");
        JButton controlBtn = new JButton("搜索");
        controlBtn.setName("search");
        JButton resetBtn = new JButton("重置");
        resetBtn.setName("searchALL");
        pNorth.add(textInput);
        pNorth.add(jComboBox);
        pNorth.add(controlBtn);
        pNorth.add(resetBtn);
        container.add(pNorth,BorderLayout.NORTH);


        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        container.add(new JScrollPane(table),BorderLayout.CENTER);


        JLabel jLabel = new JLabel("总共有"+tableModel.getRowCount()+" 条记录");
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        container.add(jLabel,BorderLayout.SOUTH);
    }
}
