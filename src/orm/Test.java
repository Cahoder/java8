package orm;

import orm.core.Query;
import orm.core.QueryFactory;
import orm.utils.ReflectUtils;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Query query = QueryFactory.createFactory();

        try {
            Class clazz = Class.forName("orm.po.Mp_product_clicks_log");
            Object o = clazz.newInstance();
            ReflectUtils.invokeSetter(o,"id", 62);
            ReflectUtils.invokeSetter(o,"product_id", 555L);
            ReflectUtils.invokeSetter(o,"service_id", 4L);
            ReflectUtils.invokeSetter(o,"openid","oPyYj0UWr9tj0CV4CB4-T8pFvruY");
            ReflectUtils.invokeSetter(o,"created_at", 1555676436L);

            query.insert(o);

            List list = query.queryRows("select * from mp_product_clicks_log where id >= ?", clazz, new Object[]{60});
            for (Object o1 : list) {
                Object id = ReflectUtils.invokeGetter(o1, "id");
                System.out.println(id);
            }

            //测试使用连接池之后连接速度
            long start = System.currentTimeMillis();
            for (int i = 0; i < 3000; i++) {
                Object o1 = query.queryRow("select openid from mp_product_clicks_log where id = ?",clazz, new Object[]{62});
                System.out.println(ReflectUtils.invokeGetter(o1, "openid"));
            }
            System.out.println("HAPPY ENDING TIMES: " + (System.currentTimeMillis() - start));
        } catch ( ClassNotFoundException | IllegalAccessException | InstantiationException e ) {
            e.printStackTrace();
        }


    }
}
