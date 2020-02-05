package orm.core;

import orm.bean.TableInfo;
import orm.utils.StringUtils;

import java.util.Map;

/**
 * Obtain a query Object by QueryFactory use DBManager's config
 * This is an singleton !
 */
public class QueryFactory {

    private static Query prototype;

    //根据当前使用的数据库类型创建
    static {
        String SQLCLIENT = "Query" + StringUtils.firstCharToUpperCase(DBManager.getConfigs().getUsingDB());
        try {
            Class queryClient = Class.forName("orm.core." + SQLCLIENT);
            prototype = (Query) queryClient.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private QueryFactory(){}  //单例

    /**
     * 通过克隆模式创建
     * @return 相应的Query对象
     */
    public static Query createFactory() {
        try {
            Map<String, TableInfo> tables = TableContext.tables;  //使之触发加载相应静态资源

            return (Query) prototype.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalAccessError("db.config 文件配置信息中数据库类型填写有误!");
        }
    }

}
