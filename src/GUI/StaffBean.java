package GUI;

import GUI.core.MyBean;

/**
 * 用于储存每个员工的个人信息
 */
public class StaffBean extends MyBean {
    private final static String[] SEX = {"男","女"};
    private final static String[] DEP = {"财务部","开发部","运营部","人事部","管理部"};
    private final static String[] EDU= {"小学","初中","高中","大专","本科","硕士","博士"};
    @TableFieldAnnotation(FIELD_NAME = "id",FIELD_TYPE = "int",FIELD_LENGTH = 11,FIELD_COMMENT = "职员编号",IS_NULL = false)
    private int id = 0;
    @TableFieldAnnotation(FIELD_NAME = "name",FIELD_TYPE = "varchar",FIELD_LENGTH = 50,IS_NULL = false,FIELD_COMMENT = "职员姓名")
    private String name = "";
    @TableFieldAnnotation(FIELD_NAME = "sex",FIELD_TYPE = "tinyint",FIELD_LENGTH = 1,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "性别(0/男,1/女)")
    private int sex = 0;
    @TableFieldAnnotation(FIELD_NAME = "birth",FIELD_TYPE = "Date",FIELD_LENGTH = 3,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "1970-1-1",FIELD_COMMENT = "生日")
    private java.sql.Date birth = null;
    @TableFieldAnnotation(FIELD_NAME = "department",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "部门ID")
    private int department = 0;
    @TableFieldAnnotation(FIELD_NAME = "education",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "学历ID")
    private int education = 0;
    @TableFieldAnnotation(FIELD_NAME = "duties",FIELD_TYPE = "varchar",FIELD_LENGTH = 50,IS_NULL = false,FIELD_COMMENT = "职员职责")
    private String duties = "";
    @TableFieldAnnotation(FIELD_NAME = "salary",FIELD_TYPE = "int",FIELD_LENGTH = 11,IS_NULL = false,IS_DEFAULT = true,FIELD_DEFAULT = "0",FIELD_COMMENT = "工资")
    private int salary = 0;
    @TableFieldAnnotation(FIELD_NAME = "workState",FIELD_TYPE = "varchar",FIELD_LENGTH = 50,IS_NULL = false,FIELD_COMMENT = "新入职")
    private String workState = "新入职";

    //空参构造器必须要有
    public StaffBean(){}

    //表单检测
    public boolean checkSubmit() throws IllegalArgumentException {
        if (id == 0) throw new IllegalArgumentException("员工编号不可以为空");
        if (name.equals("")) throw new IllegalArgumentException("员工姓名不可以为空");
        if (birth == null) throw new IllegalArgumentException("员工生日不可以为空");
        if (duties.equals("")) throw new IllegalArgumentException("员工职务不可以为空");
        if (salary < 0) throw new IllegalArgumentException("员工工资有误");
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.sql.Date getBirth() {
        return birth;
    }

    public void setBirth(java.sql.Date birth) {
        this.birth = birth;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getworkState() {
        return workState;
    }

    public void setworkState(String workstate) {
        this.workState = workstate;
    }

    public String getSex() {
        return SEX[sex];
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDepartment() {
        return DEP[department];
    }

    public int getDepartmentIndex() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getEducation() {
        return EDU[education];
    }

    public int getEducationIndex(){
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public String[] getALLDepartments(){
        return DEP;
    }
}
