package jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.lang.String;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
//        HelloJDBC();
//        BatchInsert();
//        DatabaseDate();
//        RangeSearch("2018-09-01","2019-09-01");
        //GetSqlRowKeyValue();
//        GetSqlRowsKeyValue();
//        GetSqlJavaBean();
        GetReflectionJavaBean();
    }

    //通过类反射机制获取对象对应的数据库字段信息
    private static void GetReflectionJavaBean() {
        try {
            Class mprecord = Class.forName("jdbc.MpRecord");
            MpRecordTableAnnotation mrta;
            MpRecordFieldAnnotation mrfa;

            //可以利用反射类进行拼接SQL语句
            StringBuilder tableSql = new StringBuilder("CREATE TABLE '");

            //返回直接存在于此元素上的所有注解（不包括继承的）
            Annotation[] annotationA = mprecord.getDeclaredAnnotations();
            //返回此元素上存在的所有注解（包括继承的）
            Annotation[] annotationB = mprecord.getAnnotations();

            //遍历所有的注解
            for (Annotation annotation : annotationA) {
                System.out.println(annotation);   //@jdbc.MpRecordTableAnnotation(ENGINE=InnoDB, COLLATE=utf8_unicode_ci, CHARSET=utf8, COMMENT=, TABLE=mp_record)
            }

            //获取指定的注解
            Annotation tableAnno = mprecord.getAnnotation(MpRecordTableAnnotation.class);
            if (tableAnno instanceof MpRecordTableAnnotation) {
                mrta = (MpRecordTableAnnotation) tableAnno;
                tableSql.append(mrta.TABLE()).append("' () ").append("ENGINE=")
                        .append(mrta.ENGINE()).append(" DEFAULT CHARSET=").append(mrta.CHARSET())
                        .append(" COLLATE=").append(mrta.COLLATE()).append(" COMMENT='").append(mrta.COMMENT()).append("'");
                //System.out.println("数据表 " + mrta.TABLE() + "---" + mrta.CHARSET() + "---" + mrta.COLLATE() + "---" + mrta.ENGINE());
            }


            //利用反射获取所有的成员属性
            Field[] fields = mprecord.getDeclaredFields();
            StringBuilder fieldSql = new StringBuilder();
            for (Field field : fields) {
                MpRecordFieldAnnotation fieldAnno = field.getAnnotation(MpRecordFieldAnnotation.class);
                if (fieldAnno != null){
                    fieldSql.append("'").append(fieldAnno.FIELD_NAME()).append("' ").append(fieldAnno.FIELD_TYPE()).append("(").append(fieldAnno.FIELD_LENGTH()).append(") ");
                    if (!fieldAnno.IS_NULL()) fieldSql.append("NOT NULL ");
                    if (fieldAnno.IS_DEFAULT()) fieldSql.append("DEFAULT '").append(fieldAnno.FIELD_DEFAULT()).append("' ");
                    if (fieldAnno.AUTO_INCREMENT()) fieldSql.append("AUTO_INCREMENT ");
                    fieldSql.append("COMMENT '").append(fieldAnno.FIELD_COMMENT()).append("'");
                    fieldSql.append(",\n");
                }
            }


            //最终拼接成SQL语句
            String SQL = tableSql.toString().replaceFirst("\\(\\)", "(" + fieldSql.toString() + ")");
            System.out.println(SQL);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //将数据库结构和类对应起来这种方式存储操作数据库最常用
    private static void GetSqlJavaBean(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        MpRecord mp_record = new MpRecord();
        try {
            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");
            String sql = "select * from mp_record where id > ? limit 10";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,220);
            rs = pstmt.executeQuery();

            //获取一条记录
            if (rs.first()){
                mp_record.setId(rs.getInt("id"));
                mp_record.setAdmin_user_id(rs.getInt("admin_user_id"));
                mp_record.setContent(rs.getString("content"));
                mp_record.setCreated_at(rs.getInt("created_at"));
                mp_record.setIs_download(rs.getInt("is_download"));
                mp_record.setIs_read(rs.getInt("is_read"));
                mp_record.setOpenid(rs.getString("openid"));
                mp_record.setService_id(rs.getInt("service_id"));
                mp_record.setType(rs.getInt("type"));
            }

            System.out.println(mp_record.toString());

            //获取一组记录
            LinkedList<MpRecord> list = new LinkedList<>();
            while (rs.next()){
                MpRecord one = new MpRecord();
                one.setId(rs.getInt("id"));
                one.setAdmin_user_id(rs.getInt("admin_user_id"));
                one.setContent(rs.getString("content"));
                one.setCreated_at(rs.getInt("created_at"));
                one.setIs_download(rs.getInt("is_download"));
                one.setIs_read(rs.getInt("is_read"));
                one.setOpenid(rs.getString("openid"));
                one.setService_id(rs.getInt("service_id"));
                one.setType(rs.getInt("type"));
                list.add(one);
            }
            list.forEach(System.out::println);

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            SqlConnection.mysqlClose(rs,pstmt,conn);
        }
    }

    //获取一组符合条件的记录的储存到 键值对 中
    private static void GetSqlRowsKeyValue(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");
            String sql = "select * from mp_user where id > ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,220);
            rs = pstmt.executeQuery();
            LinkedList<HashMap<String,Object>> rows = SqlConnection.getRowsFields(rs);


            rows.forEach(System.out::println);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            SqlConnection.mysqlClose(rs,pstmt,conn);
        }
    }


    //获取一条记录的储存到 键值对 中
    private static void GetSqlRowKeyValue(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");
            String sql = "select * from mp_user where id > ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,126);
            rs = pstmt.executeQuery();
            HashMap<String, Object> row = SqlConnection.getRowFields(rs, "id");

            row.entrySet().stream().forEach(System.out::println);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            SqlConnection.mysqlClose(rs,pstmt,conn);
        }
    }

    //根据范围进行查找
    private static void RangeSearch(String begin, String end) {
        String startTime;
        String endTime;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        try{
            startTime = (format.parse(begin).getTime()/1000)+"";
            endTime = (format.parse(end).getTime()/1000)+"";
        }catch (ParseException e) {
            System.out.println("输入的字符串日期有误："+ e.getMessage());
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
//            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");
            conn = SqlConnection.mysqlConnByConfigFile();

            pstmt = conn.prepareStatement("SELECT * FROM `mp_record` WHERE created_at BETWEEN ? AND ?");
            pstmt.setObject(1,startTime);
            pstmt.setObject(2,endTime);

            rs = pstmt.executeQuery();

            while (rs.next()){
                String date = format.format(rs.getLong(9));
                System.out.println( "id:" + rs.getInt(1) + " created_at:" + date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlConnection.mysqlClose(rs,pstmt,conn);
        }
    }

    //关闭自动事务提交，进行批处理插入
    private static void BatchInsert() {
        Connection conn = null;
        PreparedStatement pstmt = null;   //批处理不建议使用预编译语句，预编译需要消耗空间，插入数据过多以防超过了预编译空间会抛出异常
        Statement stmt = null;   //Statement 使用于获取执行sql语句的对象，但是非sql安全会发生sql注入
        ResultSet rs = null;

        try {
            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");
            pstmt = conn.prepareStatement("select openid from mp_user where id = ?");
            pstmt.setInt(1,50);
            rs = pstmt.executeQuery();
            if (!rs.next()) return;   //找不到指定用户ID

            conn.setAutoCommit(false);  //关闭自动提交事务

            stmt = conn.createStatement();

            try {
                for (int i = 0; i < 20000; i++) {
                    stmt.addBatch("insert into mp_record (openid,service_id,admin_user_id,content,is_read,is_download,type,created_at) values " +
                    "('"+rs.getString(1)+"',2,1000,'{msg:\"I send you a message !\",id:1000}',0,0,1,UNIX_TIMESTAMP(now())-CEIL(RAND()*100000000))");
                }
                stmt.executeBatch();
                conn.commit();
            }catch (SQLException e){
                e.printStackTrace();
                conn.rollback();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            SqlConnection.mysqlClose(rs,pstmt,stmt,conn);
        }
    }

    //jdbc连接和查询操作
    private static void HelloJDBC(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        //Statement stmt = conn.createStatement();   //Statement 使用于获取执行sql语句的对象，但是非sql安全会发生sql注入
        ResultSet rs = null;

        try {
            conn = SqlConnection.mysqlConnByParams("localhost","3306","mini_program","root","123456");

            String sql = "select * from mp_user where id > ?";  // ? 占位符
            pstmt = conn.prepareStatement(sql);       //PreparedStatement 预编译sql安全

            pstmt.setObject(1,1);   //JDBC占位符的索引从1开始

            //pstmt.execute();        //如果有结果集就会返回true，其他情况都是返回false<查询符合的count获取查询失败>
            rs = pstmt.executeQuery();//专门用于查询获取结果集
            //pstmt.executeUpdate();  //专门用于update和delete操作

            //使用迭代器遍历结果集
            while (rs.next()){
                System.out.println("id:" + rs.getInt(1) +"------"+ "openid:" + rs.getString(11));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlConnection.mysqlClose(rs,pstmt,conn);
        }
    }

    //数据库使用时间戳必须使用java.sql.*下的时间类
    private static void DatabaseDate(){
        java.sql.Date date = new Date(System.currentTimeMillis());          //返回 yyyy-MM-dd
        java.sql.Time time = new Time(System.currentTimeMillis());          //返回 HH:ii:ss
        java.sql.Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //返回 yyyy-MM-dd HH:ii:ss.mmm
        System.out.println(date);
        System.out.println(time);
        System.out.println(timestamp);
    }
}
