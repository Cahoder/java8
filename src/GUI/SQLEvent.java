package GUI;

import GUI.core.Event;
import GUI.core.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

/**
 * 与后端的数据库交互事件都在这里触发
 */
public class SQLEvent implements Event {

    public final static Connection mysql =
            SQLConnection.mysqlConnByParams("121.36.14.14","3306","gpnu_java_test","gpnu_user","123456");
    public final static String TABLESQL = "CREATE TABLE chdTable ("+
            "id int(11) NOT NULL COMMENT 职员编号,"+
            "name varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 职员姓名,"+
            "sex tinyint(1) NOT NULL DEFAULT 0 COMMENT 性别(0/男,1/女),"+
            "birth date NOT NULL COMMENT 生日,"+
            "department int(11) NOT NULL DEFAULT 0 COMMENT 部门,"+
            "education int(11) NOT NULL DEFAULT 0 COMMENT 学历,"+
            "duties varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 职员职责,"+
            "salary int(11) NOT NULL DEFAULT 0 COMMENT 工资,"+
            "workState varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 新入职 COMMENT 员工状态,"+
            "PRIMARY KEY (id)"+
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT 职员信息记录表";

    private static void CreateTable(){
        try {
            mysql.prepareStatement(TABLESQL).executeUpdate();
        } catch (SQLException e) {
            System.out.println("创建数据表失败！");
        }
    }

    static {
        try {
            List gpnu_java_test = SQLConnection.getSchemaTables(mysql, "gpnu_java_test");
            if(!gpnu_java_test.contains("chdTable")) CreateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MyFrame CLIENT = null;   //保存GUI客户端
    private Object source = null; //保存前端触发对象
    private Object data = null; //保存需要储存到数据库的数据对象

    public static void SetClient(MyFrame client){
        CLIENT = client;
    }

    @Override
    public Object getSource() {
        return source;
    }
    public Object getData() {
        return data;
    }
    public void setSource(Object source){
        this.source = source;
    }
    public void setSource(Object source, Object data){
        this.source = source;
        this.data = data;
    }

    @Override
    public Date getWhen() {
        return new Date();
    }

    @Override
    public String getMessage() {
        return "前端操作触发回调数据库交互";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void callback() {
        if (CLIENT == null) throw new IllegalStateException("请设定GUI客户端！");
        JButton jButton = (JButton)getSource(); //获取触发事件的按钮

        switch (jButton.getName()){
            case "confirm":
                try {
                    insert();
                    JOptionPane.showMessageDialog(jButton.getParent(),"插入成功","success",JOptionPane.INFORMATION_MESSAGE);
                    CLIENT.Reset();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(jButton.getParent(),e.getMessage(),"error",JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "search":
                HashMap<String,String> condition = (HashMap<String,String>) data;
                try {
                    //刷新页面数据
                    SearchUI.tableModel.setRowCount(0);
                    SearchUI.data = search(condition);
                    for (Object[] search : SearchUI.data) {
                        SearchUI.tableModel.addRow(search);
                    }
                    SearchUI.tableModel.fireTableDataChanged();

                    JPanel searchPanel = (JPanel) jButton.getParent();
                    Container parent = searchPanel.getParent();
                    JLabel label = (JLabel)parent.getComponent(2);
                    label.setText("总共有"+ SearchUI.data.length+" 条记录");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(jButton.getParent(),"查询失败！","error",JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "searchALL":
                try {
                    //重置页面数据
                    SearchUI.data = search();
                    SearchUI.tableModel.setRowCount(0);
                    SearchUI.data = search();
                    for (Object[] search : SearchUI.data) {
                        SearchUI.tableModel.addRow(search);
                    }
                    SearchUI.tableModel.fireTableDataChanged();
                    JPanel searchPanel = (JPanel) jButton.getParent();
                    Container parent = searchPanel.getParent();
                    JLabel label = (JLabel)parent.getComponent(2);
                    label.setText("总共有"+ SearchUI.data.length+" 条记录");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(jButton.getParent(),"重置失败！","error",JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    public Boolean insert() throws SQLException{
        String preSQL = "INSERT INTO gpnu_java_test.chdTable(id, name, sex, birth, department, education, duties, salary, workState) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstm = mysql.prepareStatement(preSQL);
        StaffBean staff = (StaffBean) data;
        pstm.setInt(1,staff.getId());
        pstm.setString(2,staff.getName());
        pstm.setInt(3,staff.getId());
        pstm.setDate(4,staff.getBirth());
        pstm.setInt(5,staff.getDepartmentIndex());
        pstm.setInt(6,staff.getEducationIndex());
        pstm.setString(7,staff.getDuties());
        pstm.setInt(8,staff.getSalary());
        pstm.setString(9,staff.getworkState());
        pstm.executeUpdate();
        return true;
    }

    public static Object[][] search() throws SQLException {
        String preSQL= "SELECT * FROM gpnu_java_test.chdTable";

        ResultSet resultSet = mysql.prepareStatement(preSQL).executeQuery();
        LinkedList<StaffBean> staffBeans = new LinkedList<>();
        while (resultSet.next()){
            StaffBean staff = new StaffBean();
            staff.setId(resultSet.getInt(1));
            staff.setName(resultSet.getString(2));
            staff.setSex(resultSet.getInt(3));
            staff.setBirth(resultSet.getDate(4));
            staff.setDepartment(resultSet.getInt(5));
            staff.setEducation(resultSet.getInt(6));
            staff.setDuties(resultSet.getString(7));
            staff.setSalary(resultSet.getInt(8));
            staff.setworkState(resultSet.getString(9));
            staffBeans.add(staff);
        }
        Object[][] data = new Object[staffBeans.size()][9];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = staffBeans.get(i).getId();
            data[i][1] = staffBeans.get(i).getName();
            data[i][2] = staffBeans.get(i).getSex();
            data[i][3] = staffBeans.get(i).getBirth();
            data[i][4] = staffBeans.get(i).getDepartment();
            data[i][5] = staffBeans.get(i).getDuties();
            data[i][6] = staffBeans.get(i).getSalary();
            data[i][7] = staffBeans.get(i).getEducation();
            data[i][8] = staffBeans.get(i).getworkState();
        }
        return data;
    }

    public static Object[][] search(HashMap<String,String>condition) throws SQLException {
        String preSQL;
        if (condition.size() == 0) preSQL = "SELECT * FROM gpnu_java_test.chdTable";
        else {
            preSQL = "SELECT * FROM gpnu_java_test.chdTable where ";
            Set<String> fields = condition.keySet();
            for (String field : fields) {
                preSQL += field + " = '" + condition.get(field) + "'";
            }
        }
        ResultSet resultSet = mysql.prepareStatement(preSQL).executeQuery();
        LinkedList<StaffBean> staffBeans = new LinkedList<>();
        while (resultSet.next()){
            StaffBean staff = new StaffBean();
            staff.setId(resultSet.getInt(1));
            staff.setName(resultSet.getString(2));
            staff.setSex(resultSet.getInt(3));
            staff.setBirth(resultSet.getDate(4));
            staff.setDepartment(resultSet.getInt(5));
            staff.setEducation(resultSet.getInt(6));
            staff.setDuties(resultSet.getString(7));
            staff.setSalary(resultSet.getInt(8));
            staff.setworkState(resultSet.getString(9));
            staffBeans.add(staff);
        }
        Object[][] data = new Object[staffBeans.size()][9];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = staffBeans.get(i).getId();
            data[i][1] = staffBeans.get(i).getName();
            data[i][2] = staffBeans.get(i).getSex();
            data[i][3] = staffBeans.get(i).getBirth();
            data[i][4] = staffBeans.get(i).getDepartment();
            data[i][5] = staffBeans.get(i).getDuties();
            data[i][6] = staffBeans.get(i).getSalary();
            data[i][7] = staffBeans.get(i).getEducation();
            data[i][8] = staffBeans.get(i).getworkState();
        }
        return data;
    }
}
