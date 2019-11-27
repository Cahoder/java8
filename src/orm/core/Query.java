package orm.core;


import java.util.List;

/**
 * This is a sql query interface !
 * The implements must include the CURD operation !
 * @author cahoder
 */
public interface Query {

    /**
     * Operation By DataManagerLanguage Like SQL
     * @param DML
     * @param params
     * @return
    */
    public int executeDML(String DML,Object[] params);

    /**
     * Create a new Database Record !
     * @param obj
     * @return -1 represent error insert
    */
    public int insert(Object obj);

    /**
     * Update a exist Database Record !
     * @param obj update all fields
     * @return influenced data rows
     */
    public int update(Object obj);

    /**
     * Update a part fields of a exist Database Record !
     * @param obj
     * @param fields update fields
     * @return influenced data rows
     */
    public int update(Object obj,String[] fields);

    /**
     * Delete a exist Record By Primary key id !
     * @param clazz
     * @param id
     * @return -1 represent error delete
     */
    public int delete(Class clazz,int id);

    /**
     * Delete a exist Record By Object !
     * @param obj
     * @return -1 represent error delete
     */
    public int delete(Object obj);

    /**
     * Search the recent row Record Object match condition
     * @param DML
     * @param clazz Table structure class object
     * @param params match condition
     * @return
     */
    public Object queryRow(String DML,Class clazz,Object[] params);

    /**
     * Search the all rows Record Object match condition
     * @param DML
     * @param clazz
     * @param params
     * @return
     */
    public List queryRows(String DML,Class clazz,Object[] params);

    /**
     * Search one field of one row Record Object match condition
     * @param DML
     * @param params
     * @return
     */
    public Object queryValue(String DML,Object[] params);

    /**
     * Search number of all rows Record Object match condition
     * @param DML
     * @param params
     * @return
     */
    public Number queryNumber(String DML,Object[] params);
}
