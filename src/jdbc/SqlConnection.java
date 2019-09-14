package jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.lang.String;
import java.util.*;
import java.util.stream.Stream;

/**
 * 获取一个mysql的连接句柄Connection
 * 前提必须导入jdbc.jar（java database connected）
 *
 */
class SqlConnection {

    private final static String configFile = "db.config";
    private static Properties configs;
    
    static {
        configs = new Properties();

        try {
            configs.load(new InputStreamReader(new FileInputStream(configFile)));
        } catch (IOException e) {
            configs = null;
//            e.printStackTrace();
        }
    }

    //配置文件连接
    static Connection mysqlConnByConfigFile(){
        if (configs == null){
            throw new IllegalAccessError("数据库配置文件不存在：（请创建一个"+configFile+"文件在对应的路径下）");
        }
        String error;
        try {
            //使用类反射机制导入jdbc驱动到内存
            Class.forName("com.mysql.cj.jdbc.Driver");
            /* 使用sql包的驱动管理器建立数据库连接
             * 建立连接，连接对象Connection中包含了一个socket对象
             * 后期需要通过连接池管理连接，提高连接效率
             */
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://"+configs.getProperty("host")+":" +configs.getProperty("port")
                            +"/"+configs.getProperty("database"),configs.getProperty("user"),configs.getProperty("password"));
            return conn;
        } catch (ClassNotFoundException e) {
            error = "数据库连接失败：请先导入jdbc驱动jar包！";
        } catch (SQLException e) {
            error = "数据库连接失败：" + e.getMessage();
        }
        throw new IllegalAccessError(error);
    }

    //参数连接
    static Connection mysqlConnByParams(String host, String port, String database, String user, String password)
    {
        try {
            //使用类反射机制导入jdbc驱动到内存
            Class.forName("com.mysql.cj.jdbc.Driver");
            /* 使用sql包的驱动管理器建立数据库连接
             * 建立连接，连接对象Connection中包含了一个socket对象
             * 后期需要通过连接池管理连接，提高连接效率
             */
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database,user,password);

            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("数据库连接失败：请先导入jdbc驱动jar包！");
        } catch (SQLException e) {
            System.out.println("数据库连接失败：" + e.getMessage());
        }
        throw new IllegalAccessError("请解决问题后重试！");
    }

    static List getSchemaTables(Connection conn,String dbName) throws SQLException{
        if (conn == null || dbName == null) throw new SQLException("请确认参数类型!");

        String sql = "select * from information_schema.tables where table_schema='"+dbName.trim()+"'";

        Statement stmt;
        ResultSet rs;
        List<String> tables = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                tables.add(rs.getString("table_name"));
            }

        }catch (SQLException e){
            tables.clear();
            throw e;
        }
        return tables;
    }

    static Map<String,Object> getSchemaTableFields(Connection conn,String dbName,String tableName) throws SQLException {
        if (conn == null || tableName == null) throw new SQLException("请确认参数类型!");
        String sql = "select * from Information_schema.columns  where TABLE_NAME='"+ tableName +"'and TABLE_SCHEMA='"+ dbName +"'";

        Statement stmt;
        ResultSet rs;
        Map<String,Object> fields = new HashMap<>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                fields.put(rs.getString("column_name"),rs.getString("data_type"));
            }

        }catch (SQLException e){
            fields.clear();
            throw e;
        }
        return fields;
    }


    //获取一条记录的键值数组
    static HashMap<String,Object> getRowFields(ResultSet rs,String...fields) throws SQLException {
        //参数校验是否有指定字段（否则默认查找全部的字段）
        Stream<String> stream = Arrays.stream(fields);
        if (stream.anyMatch(s -> s.trim().matches("\\*"))){
            return getRowFields(rs);
        }

        HashMap<String,Object> Fields = new HashMap<>();
        try {
            if (rs.first()){
                for (String field : fields) {
                    Fields.put(field, rs.getObject(field));
                }
            }
        }catch (SQLException e){
            Fields.clear();
            throw e;
        }
        return Fields;
    }

    //获取一条记录的键值数组
    static HashMap<String,Object> getRowFields(ResultSet rs) throws SQLException {
        HashMap<String,Object> Fields = new HashMap<>();
        Statement stmt = rs.getStatement();
        Connection conn = stmt.getConnection();
        String dbName = conn.getCatalog();
        List dbTables = getSchemaTables(conn,dbName); //获取当前数据库的所有数据表
        String table = "";
        String[] str = stmt.toString().split(" ");
        for (String s : str) {
            if (dbTables.contains(s)){
                table = s;
                break;
            }
        }
        if (table.equals("")){
            throw new SQLException("参数错误！");
        }

        //获取当前数据表的所有字段
        Map<String, Object> tableFields = getSchemaTableFields(conn, dbName, table);
        try {
            if (rs.first()){
                for (String field : tableFields.keySet()) {
                    Fields.put(field, rs.getObject(field));
                }
            }
        }catch (SQLException e){
            Fields.clear();
            throw e;
        }

        return Fields;
    }

    //获取一组记录的键值数组
    static LinkedList<HashMap<String,Object>> getRowsFields(ResultSet rs,String...fields) throws SQLException {
        //参数校验是否有指定字段（否则默认查找全部的字段）
        Stream<String> stream = Arrays.stream(fields);
        if (stream.anyMatch(s -> s.trim().matches("\\*"))){
            return getRowsFields(rs);
        }

        LinkedList<HashMap<String,Object>> lists = new LinkedList<>();
        try {
            while (rs.next()){
                HashMap<String, Object> one = new HashMap<>();
                for (String field : fields) {
                    one.put(field, rs.getObject(field));
                }
                lists.add(one);
            }
        }catch (SQLException e){
            lists.clear();
            throw e;
        }
        return lists;
    }

    //获取一组记录的键值数组
    static LinkedList<HashMap<String,Object>> getRowsFields(ResultSet rs) throws SQLException {
        LinkedList<HashMap<String,Object>> lists = new LinkedList<>();
        Statement stmt = rs.getStatement();
        Connection conn = stmt.getConnection();
        String dbName = conn.getCatalog();
        List dbTables = getSchemaTables(conn,dbName); //获取当前数据库的所有数据表
        String table = "";
        String[] str = stmt.toString().split(" ");
        for (String s : str) {
            if (dbTables.contains(s)){
                table = s;
                break;
            }
        }
        if (table.equals("")){
            throw new SQLException("参数错误！");
        }

        //获取当前数据表的所有字段
        Map<String, Object> tableFields = getSchemaTableFields(conn, dbName, table);
        try {
            while (rs.next()){
                HashMap<String, Object> one = new HashMap<>();
                for (String field : tableFields.keySet()) {
                    one.put(field, rs.getObject(field));
                }
                lists.add(one);
            }
        }catch (SQLException e){
            lists.clear();
            throw e;
        }
        return lists;
    }


    //使用完要释放资源 (后开先关)
    static void mysqlClose(ResultSet rs, PreparedStatement pstmt, Connection conn){
        try{
            if (rs != null) {
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void mysqlClose(ResultSet rs, Statement stmt, Connection conn){
        try{
            if (rs != null) {
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void mysqlClose(ResultSet rs, PreparedStatement pstmt, Statement stmt, Connection conn){
        try{
            if (rs != null) {
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void mysqlClose(Statement stmt, Connection conn){
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void mysqlClose(PreparedStatement pstmt, Connection conn){
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
