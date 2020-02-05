package orm.bean;

/**
 * 管理配置信息
 */
public class Configuration {
    private String driver; //数据库驱动类
    private String usingDB; //数据库类型（mysql-oracle-access）
    private String host; //连接地址
    private String port; //连接端口
    private String dbName; //数据库名
    private String user; //用户名
    private String password; //密码
    private String srcRoot; //项目根目录路径
    private String proRoot; //项目编译后class字节码根目录路径
    private String poPackage; //persistence object package path (用于持久化保存数据库数据表信息映射到本地javaBean类中) 存放的包名字

    /**
     * 不带参数构造器
     */
    public Configuration() {}

    /**
     * 带参数构造器
     */
    public Configuration(
            String driver,
            String usingDB,
            String host,
            String port,
            String dbName,
            String user,
            String password,
            String srcRoot,
            String poPackage
    ) {
        this.driver = driver;
        this.usingDB = usingDB;
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.srcRoot = srcRoot;
        this.poPackage = poPackage;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSrcRoot() {
        return srcRoot;
    }

    public void setSrcRoot(String srcRoot) {
        this.srcRoot = srcRoot;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }

    public String getProRoot() {
        return proRoot;
    }

    public void setProRoot(String proRoot) {
        this.proRoot = proRoot;
    }
}
