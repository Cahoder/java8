package orm.core;


import orm.bean.ColumnInfo;
import orm.bean.TableInfo;
import orm.utils.JDBCUtils;
import orm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a sql query interface !
 * The implements must include the CURD operation !
 * 对外提供服务的核心类
 * @author cahoder
 */
public abstract class Query implements Cloneable {

    /**
     * 定义一个模板方法,对所有的连接数据库操作进行封装，通过回调进行具体操作
     * @param DML SQL 语句
     * @param params 参数
     * @param callback 回调具体操作
     * @return 返回执行完结果
     */
    private Object generalTemplate(String DML, Object[] params, QueryCallback callback){
        Connection conn = DBManager.getConnection(); //获取数据库连接
        PreparedStatement pstm = null;
        Statement stmt = null; //ps:目前还没支持普通SQL语句使用
        ResultSet rs = null;

        try {
            pstm = conn.prepareStatement(DML);
            JDBCUtils.installParams(pstm,params); //为预处理语句填充参数

            return callback.doExecute(conn,pstm,stmt,rs);  //让回调继续做具体任务

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBManager.closeConnection(rs,pstm,stmt,conn); //关闭连接
        }
    }

    /**
     * Operation By DataManagerLanguage Like SQL
     * @param DML SQL 增/删/改 都是使用这个方法的语句
     * @param params 参数
     * @return Reflection row lines after execute the DML
    */
    public int executeDML(String DML,Object[] params){
        return (int) generalTemplate(DML, params, new QueryCallback() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement pstm, Statement stmt, ResultSet rs) {
                try {
                    return pstm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /**
     * Create a new Database Record !
     * The object's attribute which is not null will be insert !
     * @param obj 待插入的对象
     * @return -1 represent error insert
    */
    public int insert(Object obj){
        Class clazz = obj.getClass();
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);

        StringBuilder sql = new StringBuilder("INSERT INTO "+table.getTbName()+" (");  //拼接预处理SQL语句
        List<Object> params = new ArrayList<>(); //用来保存待填补的预处理语句的?符号相应的值
        int countNotNullField = 0; //用来保存非空字段的个数

        //获取clazz对象所有成员属性并与数据表字段对应
        Field[] fields = clazz.getDeclaredFields(); //注:getFields()方法只能获取public属性
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = ReflectUtils.invokeGetter(obj,fieldName);
            if (fieldValue != null){
                countNotNullField++;
                sql.append(fieldName).append(",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" VALUES (");
        //填补相应的预处理语句?符号
        for (int i = 0; i < countNotNullField; i++) sql.append("?,");
        sql.setCharAt(sql.length()-1,')');

        return executeDML(sql.toString(),params.toArray());
    }

    /**
     * Update all fields of a exist Database Record !
     * @param obj 待更新的对象
     * @return influenced data rows
     */
    public int update(Object obj){
        Class clazz = obj.getClass();
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);

        return update(obj,table.getColumns().keySet().toArray(new String[]{}));
    }

    /**
     * Update a part fields of a exist Database Record !
     * @param obj 待更新的对象
     * @param fields 需要更新的字段名称
     * @return influenced data rows
     */
    public int update(Object obj,String[] fields){
        Class clazz = obj.getClass();
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);

        StringBuilder sql = new StringBuilder("UPDATE "+table.getTbName()+" SET ");  //拼接预处理SQL语句
        List<Object> params = new ArrayList<>(); //用来保存待填补的预处理语句的?符号相应的值

        //循环需要修改的字段
        for (String fieldName : fields) {
            Object fieldValue = ReflectUtils.invokeGetter(obj,fieldName);
            sql.append(fieldName).append(" = ?,");
            params.add(fieldValue);
        }

        sql.deleteCharAt(sql.length()-1); //删除最后多余的 , 符号
        sql.append(" WHERE ");
        sql.append(table.getPrimaryKey().getFieldName()).append(" = ?");

        Object primaryKeyValue = ReflectUtils.invokeGetter(obj,table.getPrimaryKey().getFieldName());  //获取主键的值
        params.add(primaryKeyValue);

        return executeDML(sql.toString(),params.toArray());
    }

    /**
     * Delete a exist Record By Primary key id !
     * @param clazz Table structure class object
     * @param id 待删除主键
     * @return -1 represent error delete
     */
    public int delete(Class clazz,Object id){
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);
        //目前仅仅支持唯一主键删除
        ColumnInfo primaryKey = table.getPrimaryKey();
        String sql = "DELETE FROM "+table.getTbName()+" WHERE "+primaryKey.getFieldName()+" = ?";

        return executeDML(sql,new Object[]{id});
    }

    /**
     * Delete a exist Record By Object !
     * @param obj 待删除的数据表信息的JavaBean对象
     * @return -1 represent error delete
     */
    public int delete(Object obj){
        Class clazz = obj.getClass();
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);

        return delete(clazz,ReflectUtils.invokeGetter(obj,table.getPrimaryKey().getFieldName()));  //再通过反射获取主键属性的值
    }

    /**
     * 一行多列
     * Search the recent row Record Object match condition
     * @param DML SQL 语句
     * @param clazz Table structure class object
     * @param params match condition
     * @return Query result
     */
    public Object queryRow(String DML,Class clazz,Object[] params){
        List rows = queryRows(DML, clazz, params);
        return (rows==null||rows.size()==0) ? null:rows.get(0);
    }

    /**
     * 多行多列
     * Search the all rows Record Object match condition
     * @param DML SQL 语句
     * @param clazz Table structure class object
     * @param params match condition
     * @return Query result
     */
    public List queryRows(String DML,Class clazz,Object[] params){
        return (List) generalTemplate(DML, params, new QueryCallback() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement pstm, Statement stmt, ResultSet rs) {
                List<Object> rows = null;
                try {
                    rs = pstm.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData(); //获取查询结果的数据集元信息

                    //处理多行数据
                    while (rs.next()){
                        if (rows == null) rows = new ArrayList<>(); //创建容器储存查询结果集对象

                        Object obj = clazz.newInstance(); //创建实例对象用来存储数据库数据

                        //处理多列数据
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            String columnName = rsmd.getColumnLabel(i+1);   //一旦SQL语句使用了别名,返回的名字也是别名
                            Object columnValue = rs.getObject(i+1);

                            if (columnValue == null) continue;  //该字段的值为空则不调用设置相应成员属性的getter方法

                            //调用每个字段的setter方法
                            ReflectUtils.invokeSetter(obj,columnName,columnValue);
                        }

                        rows.add(obj);
                    }
                } catch (SQLException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
                return rows;
            }
        });
    }

    /**
     * 一行一列 -> 对象
     * Search one field of one row Record Object match condition
     * @param DML SQL 语句
     * @param params match condition
     * @return a object
     */
    public Object queryValue(String DML,Object[] params){
        return  generalTemplate(DML, params, new QueryCallback() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement pstm, Statement stmt, ResultSet rs) {
                Object value = null; //存储查询结果对象
                try {
                    rs = pstm.executeQuery();

                    //处理一行一列数据
                    while (rs.next()){
                        value = rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
    }

    /**
     * 一行一列 -> 数字
     * Search number of all rows Record Object match condition
     * @param DML SQL 语句
     * @param params match condition
     * @return a number
     */
    public Number queryNumber(String DML,Object[] params){
        return (Number)queryValue(DML,params);
    }

    /**
     * 通过主键查找 -> 对象
     * Search one field of one row Record Object match by primary key condition
     * @param clazz Table structure class object
     * @param id primary key value
     * @return a object
     */
    public Object queryById(Class clazz,Object id){
        //根据clazz对象获取对应的表结构信息
        TableInfo table = TableContext.poClassTableMap.get(clazz);
        //目前仅仅支持唯一主键删除
        ColumnInfo primaryKey = table.getPrimaryKey();
        String sql = "SELECT * FROM "+table.getTbName()+" WHERE "+primaryKey.getFieldName()+" = ?";

        return queryRow(sql,clazz,new Object[]{id});
    }

    /**
     * 查询数据并分页显示
     * 不同的SQL类型查询方式不同,因此定义成抽象方法
     * @param pageIndex 第几页
     * @param pageSize 每一页数据量
     * @return 分页后的数据集对象
     */
    public abstract List queryByPaginated(int pageIndex,int pageSize);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
