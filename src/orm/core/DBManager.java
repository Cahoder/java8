package orm.core;

import orm.bean.Configuration;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * 数据库连接管理
 * Manager the databases config
 * Maintain the connected object (use the connected pool)
 */
public class DBManager {

    private static Configuration configs;  //保存配置信息对象

    private static DBCPool pool;    //创建一个连接池对象

    /**
     * @return 获取数据库配置信息对象
     */
    public static Configuration getConfigs() {
        return configs;
    }

    //静态代码块,只在类加载时候执行一次
    static {
        Properties prop = new Properties();
        try {
            //加载的是编译后的绝对路径而非项目相对路径:/home/cahoder/IdeaProjects/java8/out/production/java8/orm/db.config
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("orm/db.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configs = new Configuration();
        configs.setDriver(prop.getProperty("driver"));
        configs.setDbName(prop.getProperty("dbName"));
        configs.setHost(prop.getProperty("host"));
        configs.setPassword(prop.getProperty("password"));
        configs.setPoPackage(prop.getProperty("poPackage"));
        configs.setPort(prop.getProperty("port"));
        configs.setSrcRoot(prop.getProperty("srcRoot"));
        configs.setUser(prop.getProperty("user"));
        configs.setUsingDB(prop.getProperty("usingDB"));
        configs.setProRoot(prop.getProperty("proRoot"));

        //初始化连接池对象
        pool = new DBCPool();
    }

    //获取连接
    static Connection getConnection() {
        return pool.getConnection();
    }

    //获取连接
    static Connection createConnection() {
        try {
            //使用类反射机制导入jdbc驱动到内存
            Class.forName(configs.getDriver());
            /* 使用sql包的驱动管理器建立数据库连接
             * 建立连接，连接对象Connection中包含了一个socket对象
             * 后期需要通过连接池管理连接，提高连接效率
             */
            return DriverManager.getConnection("jdbc:"
                    +configs.getUsingDB()
                    +"://"+configs.getHost()
                    +":"+configs.getPort()+"/"
                    +configs.getDbName(),configs.getUser(),configs.getPassword());
        } catch (ClassNotFoundException e) {
            System.out.println("数据库连接失败：请先导入jdbc驱动jar包！");
        } catch (SQLException e) {
            System.out.println("数据库连接失败：" + e.getMessage());
        }
        throw new IllegalAccessError("请解决问题后重试！");
    }


    //使用完要释放资源 (后开先关)
    static void closeConnection(ResultSet rs, PreparedStatement pstmt, Statement stmt, Connection conn) {
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
        /*try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        pool.setConnection(conn);  //用完连接对象要归还连接池
    }
}
