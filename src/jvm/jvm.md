#### JVM知识体系图

![](https://s3.bmp.ovh/imgs/2022/03/29218d6b84c03b79.png)

#### JVM学习路线

![](https://s3.bmp.ovh/imgs/2022/03/79a2e9101a5cd1fa.png)

##### 类字节码编译

- jvm只认字节码指令集，需要有相应的编译器（内置在jdk中）将源代码翻译成字节码（.class）文件

- jvm不仅仅只支持java，还衍生出了许多基于jvm的语言

  ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/jvm/java-jvm-class-1.png)

- class文件的结构属性

  ![](https://pdai-1257820000.cos.ap-beijing.myqcloud.com/pdai.tech/public/_images/jvm/java-jvm-class-2.png)

- javac编译 和 javap反编译

##### Java类加载机制

###### 类加载生命周期

![](https://s3.bmp.ovh/imgs/2022/02/5af325b5b627f462.png)

类的加载主要包括`加载` `验证` `准备` `解析` `初始化`五个阶段，其中`加载` `验证` `准备` `初始化`这四个阶段发生的顺序是确定的，而`解析`某些情况下允许晚于`初始化`，这是为了支持Java语言的运行时绑定(动态/晚期绑定)。另外这几个阶段是按顺序开始的，却不是按顺序进行或完成，因为这些阶段通常都是互相交叉地混合进行的，通常在一个阶段执行的过程中调用或激活另一个阶段。

- 加载: 查找并加载类的二进制数据

  ![](https://s3.bmp.ovh/imgs/2022/02/1cf99253a7467597.png)

  **该阶段虚拟机需要完成以下三件事情：**

  1. 通过一个类的全限定名来获取其定义的二进制字节流。
  2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构。
  3. 在堆中生成一个代表这个类的java.lang.Class对象，作为对方法区中这些数据的访问入口。

  ```java
  //用户可自行决定使用系统提供的类加载器，还是自定义的类加载器。
  //JVM规范允许加载器预加载类，但预加载过程中出现的意外（.class文件缺失或存在错误），类加载器必须在程序首次主动使用该类时才报告错误
  ```

- 连接

  - 验证: 确保被加载的类的正确性

    **验证阶段大致会完成4个阶段的检验动作：**

    1. `文件格式验证`: 验证字节流是否符合Class文件格式的规范；例如: 是否以`0xCAFEBABE`开头、主次版本号是否在当前虚拟机的处理范围之内、常量池中的常量是否有不被支持的类型。
    2. `元数据验证`: 对字节码描述的信息进行语义分析(注意: 对比`javac`编译阶段的语义分析)，以保证其描述的信息符合Java语言规范的要求；例如: 这个类是否有父类，除了`java.lang.Object`之外。
    3. `字节码验证`: 通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。
    4. `符号引用验证`: 确保解析动作能正确执行。

    > 验证阶段是非常重要的，但不是必须的，它对程序运行期没有影响，如果所引用的类经过反复验证，那么可以考虑采用`-Xverifynone`参数来关闭大部分的类验证措施，以缩短虚拟机类加载的时间。

  - 准备: 为类的静态变量分配内存，并将其初始化为默认值

    > 准备阶段分配的内存都在方法区中

    1. 这里进行内存分配的仅包括类变量（`static`修饰的变量），而不包括实例变量（实例变量随对象实例化时在堆中分配）
    2. 这里初始化的是根据类变量数据类型而定的默认值（如`0` `0L` `null` `false`等），而非源码中显示定义的初始值

    ```java
    //同时被static和final修饰的常量，必须在声明的时候就为其显式地赋值，否则编译时不通过
    //因为常量在编译期就会确定相应的值，并将其放入了调用它的类的常量池中
    ```

  - 解析: 把类中的符号引用转换为直接引用

    符号引用就是一组符号来描述目标，可以是任何字面量；
    
    解析动作主要针对`类`或`接口` `字段` `类方法` `接口方法` `方法类型` `方法句柄`和`调用点`限定符7类符号引用；
    
    `直接引用`就是直接指向目标的指针、相对偏移量或一个间接定位到目标的句柄；

- 初始化：为类的静态变量赋予正确的初始值，JVM负责对类进行初始化，主要对类变量进行初始化

  **类变量初始化方式**

  1. 声明类变量时显式指定初始值
  2. 通过静态代码块为类变量指定初始值

  **类初始化流程**

  - 假如这个类还没有被加载和连接，则程序先加载并连接该类
  - 假如该类的直接父类还没有被初始化，则先初始化其直接父类
  - 假如类中有初始化语句（静态代码块），则系统依次执行这些初始化语句
  
  > 类使用分为直接引用和间接引用，判别条件是看是否会引起类的初始化
  
  **直接引用会触发类的初始化**
  
  - 创建类的实例，也就是new的方式
  - 访问某个类或接口的静态变量，或者对该静态变量赋值
  - 调用类的静态方法
  - 反射(如Class.forName("com.jvm.Test"))
  - 初始化某个类的子类，则其父类也会被初始化
  - Java虚拟机启动时被标明为启动类的类，例如直接使用java.exe命令来运行某个主类
  
  **间接引用不会触发类的初始化**
  
  - 引用子类的静态变量，但该静态变量继承自父类，不会引发子类初始化，只会初始化父类
  - 定义一个类数组变量（public AnClass[] field;），不会引发该类的初始化
  - 引用类的常量，不会引起该类的初始化

- 使用： 类访问方法区内的数据结构的接口， 对象是Heap区的数据
- 卸载： 结束生命周期
  - 执行了System.exit()方法
  - 程序正常执行结束
  - 程序在执行过程中遇到了异常或错误而异常终止
  - 由于操作系统出现错误而导致Java虚拟机进程终止

###### 类加载器

- 类加载器的层次

![](https://s3.bmp.ovh/imgs/2022/02/4570569b5f17a965.png)

```java
//站在jvm的角度，只存在两类加载器：
//C++编写的启动类加载器（BootStrapClassLoader）和Java编写的其它类加载器（extend java.lang.ClassLoader）
//其他类加载器需要由启动类加载器加载到内存中之后才能去加载其他的类

//站在java开发人员的角度，存在四类加载器：
//Bootstrap ClassLoader，负责加载存放在JDK\jre\lib(JDK代表JDK的安装目录，下同)下，或被-Xbootclasspath参数指定的路径中的，并且能被虚拟机识别的类库(如rt.jar，所有的java.*开头的类均被Bootstrap ClassLoader加载)。启动类加载器是无法被Java程序直接引用的。 
//Extension ClassLoader，该加载器由sun.misc.Launcher$ExtClassLoader实现，它负责加载JDK\jre\lib\ext目录中，或者由java.ext.dirs系统变量指定的路径中的所有类库(如javax.*开头的类)，开发者可以直接使用扩展类加载器。
//Application ClassLoader，该类加载器由sun.misc.Launcher$AppClassLoader来实现，它负责加载用户类路径(ClassPath)所指定的类，开发者可以直接使用该类加载器，如果应用程序中没有自定义过自己的类加载器，一般情况下这个就是程序中默认的类加载器。
//自定义类加载器，
```

- 类加载方式

  1、命令行启动应用时候由JVM初始化加载

  2、通过Class.forName()方法动态加载

  3、通过ClassLoader.loadClass()方法动态加载

  ```java
  //Class.forName()和ClassLoader.loadClass()区别?
  //1. Class.forName()将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块
  //   Class.forName(name, initialize, loader)带参函数也可控制是否加载static块
  //2. ClassLoader.loadClass()只将.class文件加载到jvm中，不会执行static块,只有当newInstance才会去执行static块
  ```

- 类加载机制

  `全盘负责`，当一个类加载器负责加载某个Class时，该Class所依赖的和引用的其他Class也将由该类加载器负责载入，除非显式使用另外一个类加载器来载入

  `父类委托`，先让父类加载器试图加载该类，只有在父类加载器无法加载该类时才尝试从自己的类路径中加载该类

  `缓存机制`，缓存机制将会保证所有加载过的Class都会被缓存，当程序中需要使用某个Class时，类加载器先从缓存区寻找该Class，只有缓存区不存在，系统才会读取该类对应的二进制数据，并将其转换成Class对象，存入缓存区。这就是为什么修改了Class后，必须重启JVM，程序的修改才会生效

  `双亲委派机制`，如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把请求委托给父加载器去完成，依次向上，因此，所有的类加载请求最终都应该被传递到顶层的启动类加载器中，只有当父加载器在它的搜索范围中没有找到所需的类时，即无法完成该加载，子加载器才会尝试自己去加载该类。

  > 双亲委派优势：
  > 1.防止内存中出现多份同样的系统类的字节码
  > 2.保证Java程序安全稳定运行

##### JVM内存结构



##### GC回收机制



##### 排错调优
