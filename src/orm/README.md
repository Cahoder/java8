
-------------------[[ Object Relationship Mapping Frame ]]-------------------

1.首先在src/orm下新建db.config文件，如果已经存在请重新创建并编译
db.config文件中需要填写的内容如下:
    host=连接地址
    port=连接端口
    user=连接用户
    password=连接密码
    dbName=连接的数据库名称
    driver=连接的jdbc驱动类
    usingDB=使用的数据库类型
    srcRoot=项目的根目录
    poPackage=用于保存持久化数据库信息的包目录

2.目前仅支持唯一主键,尚不支持外键和联合主键使用

3.PO包中类的成员属性尽量使用包装类,避免使用基本数据类型---方便检测成员属性是否为空

4.查询功能还不支持联表查询和起别名,需要配合建立一个VO包(variable object package),用于存放需要联表查询的结果对象;
  A.根据连表查询字段结果,配合JavaFileUtils工具类生成.java文件放到vo包中去
  B.再使用JavaFileUtils.compilerJavaFile方法动态编译源码
  C.最后使用反射创建对象,根据联表结果获得的字段相应在对象中setter方法设置属性值
  D.返回 结果/结果集 即可
  ps:简单测试可以查看链接https://www.bilibili.com/video/av29579728?p=13

5.目前仅支持预处理SQL语句