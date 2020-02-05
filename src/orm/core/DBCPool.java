package orm.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据库连接池
 * This is a DataBase Connection Pool Class!
 * Use to manager database connection objects !
 */
public class DBCPool {

    /**
     * 存放着所有的连接句柄Connection对象
     */
    private List<Connection> pool;

    /**
     * 池中至少必须存在的连接句柄Connection对象
     */
    private final static int POOL_MIN_SIZE = 10;

    /**
     * 池中至多能够存在的连接句柄Connection对象
     */
    private final static int POOL_MAX_SIZE = 100;

    /**
     * 初始化连接池,使池中的连接数达到最小值
     */
    private void initPool(){
        if (pool == null)
            pool = new LinkedList<>();

        if (pool.size() < POOL_MIN_SIZE)
            pool.add(DBManager.createConnection());
    }

    /**
     * 从池中获取一个连接对象
     * @return 连接对象
     */
    public synchronized Connection getConnection(){
        Connection conn;
        //如果池中还有连接对象可用,取最后一个出来
        if (pool.size() > 0)
            conn = pool.remove(pool.size()-1);
        //如果池中的连接对象被取完了
        else
            conn = DBManager.createConnection();
        return conn;
    }

    /**
     * 连接对象使用完后需要返回池中
     * @param conn 使用完的连接对象
     */
    public synchronized void setConnection(Connection conn){
        //如果池中对象还未达到上限
        if (pool.size() < POOL_MAX_SIZE)
            pool.add(conn);
        //池中已达上限,多余的真正关闭掉
        else {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建连接池对象的时候就初始化连接池
     */
    public DBCPool() {
        initPool();
    }
}
