package orm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public interface QueryCallback {

    /**
     * 提供一个执行具体Query操作的接口,通过回调完成具体操作
     * @param conn 数据库连接句柄
     * @param pstm 预处理SQL语句
     * @param stmt 非预处理SQL语句
     * @param rs 结果集
     * @return 返回进行具体操作后的结果
     */
    Object doExecute(Connection conn, PreparedStatement pstm, Statement stmt, ResultSet rs);
}
