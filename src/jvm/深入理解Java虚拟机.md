##### 经典JVM垃圾收集算法描述

分代收集理论（"Partial GC"：["Minor/Young GC"、"Major/Old GC"]、"Mixed GC（G1独有）"、"Full GC"）

1. 弱分代假说：绝大多数对象都是朝生夕灭的
2. 强分代假说：熬过越多次GC的对象就越难以消亡
3. 跨代引用假说：跨代引用相对于同代引用来说仅占极少数（新引旧会导致新慢慢地熬成旧）

###### 标记-清理算法

###### 标记-复制算法

###### 标记-整理算法

##### HotSpot的算法细节实现

###### GC Roots

1. 采用OopMap数据结构优化效率

###### SafePoint和SafeRegion

1. 安全点的选定标准
2. 抢先式中断和主动式中断使线程到达安全点
3. 引入安全区域解决无法响应中断的线程

###### RememberedSet和CardTable

1. 卡表技术是记忆集理论的实现
2. 用于解决跨代引用问题

###### Write/Read Barrier

1. 类AOP机制的写屏障更新CardTable
2. 并发更新CardTable导致缓存伪共享问题

###### 并发的可达性分析判断对象是否存活

1. 增量更新（Incremental Update）
2. 原始快照（Snapshot At The Beginning，SATB）

##### 常用JVM垃圾回收器特点描述

![GC组合关系](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8p86rboyj30cv0dntaa.jpg)

###### Serial 收集器

+ 新生代收集器（复制算法）
+ 关注低延迟用户停顿
+ JDK1.3.1前唯一选择
+ "单线程"工作
+ 必须StopTheWorld直到GC结束
+ MemoryFootprint额外内存消耗最小
+ Client模式JVM默认选择

![Serial+SerialOld](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8qqpi9faj30gg04bq3w.jpg)

###### ParNew 收集器

+ 新生代收集器（复制算法）
+ 关注低延迟用户停顿
+ Serial的多线程并行版
+ GC线程并行但用户线程依旧STW
+ JDK1.7前Server模式JVM首选
+ 能够搭配CMS使用

![ParNew+SerialOld](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8qrfh9bqj30gd045gmn.jpg)

###### Parallel Scavenge 收集器

+ 新生代收集器（复制算法）
+ 关注可控的吞吐量目标
+ "多线程"并行工作
+ -XX:MaxGCPauseMillis   设置每次GC耗时阈值
+ -XX:GCTimeRatio   保证GC总耗时占比
+ +UseAdaptiveSizePolicy   启用自适应动态调节

![吞吐量计算](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8q949b5dj30fw02n74n.jpg)

###### Serial Old 收集器

+ 老年代收集器（标记整理算法）
+ Client模式JVM默认选择
+ "单线程"工作
+ JDK1.5前Server模式搭配ParallelScavenge使用
+ CMS发生ConcurrentModeFailure时的兜底

###### Parallel Old 收集器

+ 老年代收集器（标记整理算法）
+ JDK6时提供
+ "多线程"并发工作
+ 关注吞吐量配套ParallelScavenge使用

![ParallelScavenge+ParallelOld](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8qt703afj30g804fq44.jpg)

###### CMS收集器

+ 老年代收集器（标记清除算法）

+ GC实现步骤

  1. initial mark（STW）负责标记GCRoots的直接关联对象，速度很快
  2. concurrent mark通过直接关联对象遍历对象图，耗时长但与用户线程并发
  3. remark（STW）使用增量更新解决并发标记期间用户产生的改变
  4. concurrent sweep清除掉标记阶段判断已经死亡的对象，与用户线程并发

  ![CMS的GC](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8rtva5axj30gg04edh9.jpg)

+ 缺点

  1. 处理器资源敏感性，降低总吞吐量

  2. 无法处理标记结束后产生的"浮动垃圾"，可导致另一次STW的FullGC

     -XX:CMSInitiatingOccupancyFraction   决定CMS启动GC的阈值比

  3. 产生大量"空间碎片"，影响大对象分配导致另一次STW的FullGC

     +UseCMSCompactAtFullCollection    发生FullGC前进行碎片整理

     -XX:CMSFullGCsBeforeCompaction   触发若干次CMS后进行碎片整理

###### Garbage First 收集器

+ 面向Serve模式的服务（标记复制/整理算法）

+ 保证延迟可控情况下尽可能高吞吐的"全功能收集器"

+ 支持面向整堆进行GC，回收价值收益最大优先原则

+ 将堆拆分为Region，每个Region按需扮演新生代或老年代

+ 大于1/2Region的对象放入Humongous区域

+ 使用记忆集解决跨Region引用避免全堆GCRoots扫描

+ 使用SATB原始快照解决并发标记期间用户产生的改变

+ 使用TAMS（Top at Mark Start）指针隔离Region内GC区域和new Object区域

+ GC实现步骤

  1. initial mark（STW）标记GCRoots的直接关联对象并修改TAMS值，与MinorGC同步进行速度很快
  2. Concurrent Marking针对GCRoots进行可达性分析构建图，生成图后需重新处理SATB
  3. Final Marking（STW）处理并发阶段结束后仍遗留下来的最后那少量的SATB记录
  4. Live Data Counting and Evacuation（STW）对Region回收价值成本排序制定回收计划

  ![G1的GC](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8tf9kjexj30gb04ldh6.jpg)

+ 缺点

  1. Memory Footprint和Running Overload较高
  2. G1的记忆集可能会占整个堆容量≥20%
  3. 目前大内存应用上才能体现出优势

![各款GC并发情况](https://tva1.sinaimg.cn/large/0069Mfx5gy1hc8to9e670j30m00n4dve.jpg)

###### Shenandoah 收集器

+ 低延迟垃圾集器

+ 与G1相似的堆内存布局和回收策略

+ 采用连接矩阵的全局数据结构记录跨代引用

+ GC实现步骤

  1. initial mark（STW）约等于G1

  2. Concurrent Marking 约等于G1

  3. Final Marking（STW）约等于G1

  4. Concurrent Cleanup清理毫无存活对象的Region

  5. Concurrent Evacuation复制回收集中的存活对象到新Region

     <!--采用读屏障和Brooks-Pointers转发指针实现与用户线程并发-->

  6. Initial Update Reference（STW）确保复制存活对象任务完成

  7. Concurrent Update Reference修改对象之间的旧引用

  8. Final Update Reference（STW）修改GCRoots中的旧引用

  9. Concurrent Cleanup再次调用清除Region

###### ZGC 收集器

+ 低延迟垃圾集器
+ 采用全量扫描替代G1中记忆集维护
+ 采用读屏障和染色指针实现与用户并发
+ Linux上采用Multi-Mapping实现染色指针
+ GC实现步骤
  1. Concurrent Mark遍历图并染色需回收对象指针中标志位
  2. Concurrent Prepare for Relocate条件统计需清理的Region
  3. Concurrent Relocate复制存活对象到新Region并通过染色指针+ForwardTable保证并发
  4. Concurrent Remap修正新旧引用以便能清理ForwardTable（合并到下次Concurrent Mark进行）

##### HotSpot的JVM常用配置参数

| 参数                                | 描述                                                         |
| ----------------------------------- | ------------------------------------------------------------ |
| -XX:+UseSerialGC                    | 虚拟机运行在Client模式下的默认值--使用Serial+SerialOld进行内存回收 |
| -XX:+UseParNewGC                    | 使用ParNew+SerialOld进行内存回收--JDK9已废弃                 |
| -XX:+UseConcMarkSweepGC             | 使用ParNew+CMS+SerialOld进行内存回收                         |
| -XX:+UseParallelGC                  | JDK9前在Server模式下的默认值--使用PS+SerialOld进行内存回收   |
| -XX:+UseParallelOldGC               | 使用Parallel Scavenge+Parallel Old进行内存回收               |
| -XX:SurvivorRatio=                  | 新生代Eden区与Survivor区的比，默认为8，代表Eden:Survivor=8:1 |
| -XX:PretenureSizeThreshold=         | 设置直接晋升老年代的对象大小                                 |
| -XX:MaxTenuringThreshold=           | 设置熬过Minor GC晋升老年代的对象年龄                         |
| -XX:+UseAdaptiveSizePolicy          | 动态调整Heap各区域大小以及进入老年代的年龄                   |
| -XX:HandlePromotionFailure          | 是否设置分配担保失败--JDK7后废弃                             |
| -XX:ParallelGCThreads=              | 设置并行GC时进行内存回收的线程数                             |
| -XX:GCTimeRatio=                    | Parallel Scavenge GC时间占总时间比，默认99，即允许1%GC时间   |
| -XX:MaxGCPauseMillis=               | Parallel Scavenge GC的最大停顿时间                           |
| -XX:CMSInitiatingOccupancyFraction= | 设置CMS的老年代空间使用多少后触发GC，默认68%                 |
| -XX:+UseCMSCompactAtFullCollection  | 设置CMS再触发FullGC后是否进行一次内存碎片整理--JDK9废弃      |
| -XX:CMSFullGCsBeforeCompaction=     | 设置CMS在进行若干次GC后进行一次内存碎片整理--JDK9废弃        |
| -XX:+UseG1GC                        | 使用G1收集器，JDK9后Server模式默认值                         |
| -XX:G1HeapRegionSize=               | 设置G1的Region大小，并非最终值                               |
| -XX:MaxGCPauseMillis=               | 设置G1收集过程目标时间，默认值200ms，不是硬性条件            |
| -XX:G1NewSizePercent=               | 设置G1新生代最小值，默认值5%                                 |
| -XX:G1MaxNewSizePercent=            | 设置G1新生代最大值，默认值60%                                |
| -XX:ParallelGCThreads=              | 用户线程冻结期间并行执行的收集器线程数                       |
| -XX:ConcGCThreads=                  | 并发标记、并发整理的执行线程数，不同的收集器，根据其能够并发的阶段有不同的含义 |
| -XX:InitiatingHeapOccupancyPercent= | 设置触发标记周期的Java堆占用率阈值，默认值45%，这里的Java堆值的是non_young_capacity_bytes，包括old+humongous |
| -XX:+UseShenandoahGC                | 使用Shenandoah收集器，OracleJDK不支持该收集器，需要配合参数-XX:+UnlockExperimentalVMOptions使用 |
| -XX:ShenandoahGCHeuristics          | Shenandoah何时启动一次GC过程，可选值有adaptive、static、compact、passive、aggressive |
| -XX:+UseZGC                         | 使用ZGC收集器，需要配合参数-XX:+UnlockExperimentalVMOptions使用 |
| -XX:+UseNUMA                        | 启用NUMA内存分配支持，目前只有Parallel和ZGC支持              |

##### Class文件结构

<!--一个class文件对应一个唯一类/接口，但类/接口并不一定来自class文件-->

如下是Class文件格式构成，各数据项按严格顺序排列

| 类型           | 名称                | 数量                    |
| -------------- | ------------------- | ----------------------- |
| u4             | magic               | 1                       |
| u2             | minor_version       | 1                       |
| u2             | major_version       | 1                       |
| u2             | constant_pool_count | 1                       |
| cp_info        | constant_pool       | constant_pool_count - 1 |
| u2             | access_flags        | 1                       |
| u2             | this_class          | 1                       |
| u2             | super_class         | 1                       |
| u2             | interfaces_count    | 1                       |
| u2             | interfaces          | interfaces_count        |
| u2             | fields_count        | 1                       |
| field_info     | fields              | fields_count            |
| u2             | methods_count       | 1                       |
| method_info    | methods             | methods_count           |
| u2             | attributes_count    | 1                       |
| attribute_info | attributes          | attributes_count        |

根据《Java 虚拟机规范》的规定，Class文件格式采用类C语言结构体的伪结构来存储数据

这种伪结构中只有两种数据类型：“无符号数”和“表”。

+ 无符号数：属于基本的数据类型，例如以 u1、u2、u4、u8 来分别代表 1 个字节、2 个字节、4 个字节 和 8个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者按照 UTF-8编码构成字符串值。

+ 表：由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表的命名都习惯性地以“_info”结尾。表用于描述有层次关系的复合结构的数据，整个Class文件本质上也可以视作是一张表。

  <!--无论是无符号数还是表，当需要描述同一类型但数量不定的多个数据时，经常会使用一个前置的容量计数器加若干个连续的数据项的形式，这时候称这一系列连续的某一类型的数据为某一类型的“集合”。-->

  <!--属性表作为Class文件格式中最具扩展性的一种数据项目，存放着字段表（ConstantValue-每个静态字段对应值,...）、方法表（Code-<方法内部调用每个字节码指令详细描述>,LocalVariableTable-方法内部本地变量,...）等皆作为属性关联-->

###### 方法/字段描述符规则

| 标识字符 | 含义           | 标识字符 | 含义                           |
| -------- | -------------- | -------- | ------------------------------ |
| B        | 基本类型byte   | J        | 基本类型long                   |
| C        | 基本类型char   | S        | 基本类型short                  |
| D        | 基本类型double | Z        | 基本类型boolean                |
| F        | 基本类型float  | V⊙       | 特殊类型void                   |
| I        | 基本类型int    | L        | 对象类型，如Ljava/lang/Object; |

对于数组类型，每一维度将使用一个前置的 **[** 字符来描述：例如二维字符串数组描述成"[[Ljava/lang/Object;"，一维整型数组描述成"[I"。

对于类方法描述，遵循**(先参数列表)、后返回值**的顺序描述：例如方法 int indexOf(char[]source，int sourceOffset，int sourceCount，char[]target， int targetOffset，int targetCount，int fromIndex) 的描述符为“([CII[CIII)I;”

##### 字节码指令简介

Java虚拟机采用面向操作数栈而不是面向寄存器的架构，大多数指令没有操作数只有操作码，指令参数都存放在操作数栈中（先压栈操作数再弹出给CPU计算完再压栈...）

![jvm解释器执行模型](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7abbrayej30sa03mtah.jpg)

+ 字节码与数据类型

  大部分与数据类型相关的字节码指令的操作码助记符中都有特殊的字符来表明专门为哪种数据类型服务：
  i 代表对 int 类型的数据操作，l 代表 long，s 代表 short，b 代表 byte，c 代表 char，f 代表 float，d 代表 double，a 代表 reference。

  “Not Orthogonal”：即并非每种数据类型和每一种操作都有对应的指令（指令集仅支持1字节范围内），可以通过向上转型兼容使用（iload可用于操作byte操作数）

  如下是jvm支持的数据类型相关操作指令集，**T**表示指令模板，**OP**表操作模板：

  | opcode            | byte    | short   | int           | long    | float   | double  | char    | reference     |
  | ----------------- | ------- | ------- | ------------- | ------- | ------- | ------- | ------- | ------------- |
  | **T**ipush        | bipush  | sipush  |               |         |         |         |         |               |
  | **T**const        |         |         | iconst        | lconst  | fconst  | dconst  |         | aconst        |
  | **T**load         |         |         | iload         | lload   | fload   | dload   |         | aload         |
  | **T**store        |         |         | istore        | lstore  | fstore  | dstore  |         | astore        |
  | **T**inc          |         |         | iinc          |         |         |         |         |               |
  | **T**aload        | baload  | saload  | iaload        | laload  | faload  | daload  | caload  | aaload        |
  | **T**astore       | bastore | sastore | iastore       | lastore | fastore | dastore | castore | aastore       |
  | **T**add          |         |         | iadd          | ladd    | fadd    | dadd    |         |               |
  | **T**sub          |         |         | isub          | lsub    | fsub    | dsub    |         |               |
  | **T**mul          |         |         | imul          | lmul    | fmul    | dmul    |         |               |
  | **T**div          |         |         | idiv          | ldiv    | fdiv    | ddiv    |         |               |
  | **T**rem          |         |         | irem          | lrem    | frem    | drem    |         |               |
  | **T**neg          |         |         | ineg          | lneg    | fneg    | dneg    |         |               |
  | **T**shl          |         |         | ishl          | lshl    |         |         |         |               |
  | **T**shr          |         |         | ishr          | lshr    |         |         |         |               |
  | **T**ushr         |         |         | iushr         | lushr   |         |         |         |               |
  | **T**and          |         |         | iand          | land    |         |         |         |               |
  | **T**or           |         |         | ior           | lor     |         |         |         |               |
  | **T**xor          |         |         | ixor          | lxor    |         |         |         |               |
  | i2**T**           | i2b     | i2s     |               | i2l     | i2f     | i2d     |         |               |
  | l2**T**           |         |         | l2i           |         | l2f     | l2d     |         |               |
  | f2**T**           |         |         | f2i           | f2l     |         | f2d     |         |               |
  | d2**T**           |         |         | d2i           | d2l     | d2f     |         |         |               |
  | **T**cmp          |         |         |               | lcmp    |         |         |         |               |
  | **T**cmpl         |         |         |               |         | fcmpl   | dcmpl   |         |               |
  | **T**cmpg         |         |         |               |         | fcmpg   | dcmpg   |         |               |
  | if_**T**cmp**OP** |         |         | if_icmp**OP** |         |         |         |         | if_acmp**OP** |
  | **T**return       |         |         | ireturn       | lreturn | freturn | dreturn |         | areturn       |

  大部分指令都没有支持整数类型byte、char 和 short，甚至没有任何指令支持boolean类型。编译器会在编译期或运行期将 byte 和 short 类型的 数据带符号扩展（Sign-Extend）为相应的 int 类型数据，将 boolean 和 char 类型数据零位 扩展（Zero-Extend）为相应的 int 类型数据。因此，大多数对于boolean、byte、short 和 char 类型数据的操作，实际上都是使用相应的对 int 类型作为运算类型（Computational Type）进行

+ 字节指令码归类

  1. 加载和存储指令

     ![常用加载和存储指令集](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7betof4ej30ns0hjk6z.jpg)

  2. 运算指令

  3. 类型转换指令

  4. 对象创建与访问指令

  5. 操作数栈管理指令

  6. 控制转移指令

     ![常用控制转移指令集](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7chotp0xj30mq0iewuv.jpg)

  7. 方法调用和返回指令
  
     + invokestatic：用于调用静态方法
     + invokespecial：用于调用私有实例方法、构造器，以及使用super关键字调用父类的实例方法或构造器，和所实现接口的默认方法
     + invokevirtual：用于调用非私有实例方法
     + invokeinterface：用于调用接口方法
     + invokedynamic：用于调用动态方法（唯一可由用户控制分派逻辑）
     + ireturn、lreturn、freturn、dreturn、areturn和return

  8. 异常处理指令
  
     java程序中显式抛出异常的操作（throw 语句）由athrow指令实现
  
     异常处理（catch语句）由异常表来完成
  
  9. 同步指令
  
     ![方法级和代码级同步实现](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7gm9nzjbj30n30cftn0.jpg)

##### JVM类加载机制

![类生命周期](https://tva1.sinaimg.cn/large/0069Mfx5gy1hcfqf8ydk0j30kf0fxtjc.jpg)

《Java 虚拟机规范》规定有且只有的六种**主动引用**情况必须立即对类进行“初始化”（而加载、验证、准备自然需要在此之前开始）

1. 遇到new、getstatic、putstatic、invokestatic这四条字节码指令时

   <!--被final修饰、已在编译期放入常量池的静态字段除外-->

2. 使用反射包方法对类型进行反射调用时发现该类未初始化

3. 初始化类时发现其父类还未初始化

4. 虚拟机启动时要初始化main()方法所在类

5. JDK7新加入的动态语言支持，如果一个java.lang.invoke.MethodHandle实例最后解析结果为 REF_getStatic、REF_putStatic、REF_invokeStatic、REF_newInvokeSpecial 四种类型的方法句柄，并且这个方法句柄对应的类没有进行过初始化，则需要先触发其初始化

6. 当一个接口中定义了JDK8新加入的default方法，如果有这个接口的实现类发生初始化，则要先初始化接口

+ 加载

  1. 通过全限定类名获取对应类的二进制字节流
  2. 解析字节流将静态存储结构转化为方法区运行时数据结构
  3. 堆中生成对应的Class对象，作为方法区这个类的各种数据访问入口

+ 验证

  1. 文件格式验证 --- 字节流是否符合class文件结构规范，版本是否符合当前虚拟机
  2. 元数据验证 --- 字节码描述信息进行语义分析，保证符合《Java语言规范》
  3. 字节码验证 --- 保证程序语义是合法的和符合逻辑的
  4. 符号引用验证 --- 发生于符号引用转化为直接引用，校验各种引用是否匹配有效

+ 准备

  为类中定义的static变量分配内存并设置"**零初始值**"，static final常量例外(编译期已确定可直接赋值)

+ 解析

  符号引用转化为直接引用

+ 初始化

  执行类构造器<clinit>()方法：执行static代码块和static变量真正赋值

  虚拟机会保证父类<clinit>()方法先于子类<clinit>()方法执行（接口除外）

##### 类加载器

由不同的类加载器加载相同class文件，instanceof结果为false

+ 双亲委派模型

  ![双亲委派模型](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7gw69pruj30kt0nznd8.jpg)

  启动类加载器（Bootstrap Class Loader）负责加载<JAVA_HOME>\lib和-Xbootclasspath参数下类包

  <!--用户自定义加载器如需直接委派给启动类加载器,约定可在代码中直接返回null-->

  扩展类加载器（Extension Class Loader）负责加载<JAVA_HOME>\lib\ext和java.ext.dirs系统变量下类包

  <!--JDK9后被模块化的平台类加载器（Platform Class Loader）替代-->

  应用程序类加载器（Application Class Loader）负责加载用户路径下类包的默认加载器

+ 破坏双亲委派模型

  1. JNDI 服务（JDK1.3时加入rt.jar由启动类加载器管理），但它又要调用用户路径下的JNDI服务提供者接口（Service Provider Interface，SPI）的代码，但启动类加载器不会识别加载这些代码，如何解决？

     ![线程上下文类加载器](https://tva1.sinaimg.cn/large/0069Mfx5gy1hd7h0737rgj30ja0blqef.jpg)

##### JVM字节码执行

VM-Stack属于线程私有，每个方法从调用开始至执行结束过程，都对应着一个栈帧从入栈到出栈的过程

###### 栈帧数据结构

![栈帧结构](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdbrfii7j1j30dr0fhmyh.jpg)

每个Stack Frame都包括：局部变量表、操作数栈、动态连接、方法返回地址和一些额外的附加信息

+ 局部变量表的大小、操作数栈的深度在编译期就已确定，写入method_info的code属性中

+ **局部变量表**：以变量槽（Variable Slot）为最小单位，实例方法的Slots[0]一般为this指针

  32位数据占1个slot，64位数（long/double）据占2个slot，由于线程私有因此保证原子

+ **操作数栈**：属于后入先出栈（LIFO），32位数据占1个栈容量，64位数（long/double）据占2个栈容量

+ **动态连接**：每个栈帧都包含一个指向运行时常量池中该栈帧所属方法的引用，为支持方法调用中动态链接

+ **方法返回地址**：无论方法是否正常返回，都必须返回到最初方法被调用时的位置

###### 方法调用

方法调用是指确定被调用方法版本，暂未涉及方法内部具体运行过程

由于class文件编译不涉及传统编译中连接步骤（仅仅符号引用，不转换成直接引用），而是放在类加载甚至运行期进行

+ 类加载期能解析的调用

  主要有**静态方法**和**私有方法**两大类

  ![Non/Virtual-Method](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdbx956n9gj30k8054aey.jpg)

+ 主要运行期的调用-分派（静态单分派、静态多分派、动态单分派、动态多分派）

  ```java
  Human man = new Man();
  //Human称为变量的"静态类型"（Static-Type）或"外观类型"（Apparent-Type）
  //Man称为变量的"实际类型"（Actual-Type）或"运行时类型"（Runtime-Type）
  ```

  ![Java中的分派](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdbyskoucwj30h60afwhu.jpg)

  + 静态分派

    依赖静态类型来决定方法执行版本的分派动作，最典型应用表现就是方法重载（编译期间根据Static-Type决定使用"适合"重载版本）

  + 动态分派

    主指invokevirtual指令运行时解析过程，最典型应用表现就是方法重写（对类字段属性无效）

###### 动态类型语言支持

invokedynamic指令允许类型和方法检查转移到运行期（JVM层面模拟字节码实现）

java.lang.invoke包是JDK7加入的（用户api层面模拟字节码实现）

###### 基于栈的字节码指令解释执行引擎

javac编译器负责输入指令流，jvm执行引擎负责执行

字节码指令流中的指令大部分都是零地址指令，它们依赖操作数栈进行工作。

![两种指令集架构示例](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdbzojiqibj30kc0gxao3.jpg)

##### 编译器的前(阶)端优化

###### Javac编译过程

例：javac把*.java 文件转变成 *.class 文件的过程

![javac编译顺序过程](https://tva1.sinaimg.cn/large/0069Mfx5gy1hds8ufv1agj30kg0lctjg.jpg)

+ 常量折叠（Constant Folding）代码优化，`int a = 1 + 2; -> int a = 3;`

+ 局部变量声明为final，对运行期是完全没有影响

+ 字节码生成阶段的编译优化，少量的代码添加和转换工作

  ![字节码生成阶段的编译优化](https://tva1.sinaimg.cn/large/0069Mfx5gy1hds95glp6ej30k00csk5n.jpg)

+ 主要校验源码写的"对不对"，基本不考虑写的"好不好"

+ 提供语法糖提升编码效率，并负责解开语法糖

###### Java语法糖的味道

+ 泛型的优劣

  C#选择的泛型实现方式是**“具现化式泛型”（Reified Generics）**，具体类型真实存在于源码、编译后的中间语言、抑或是运行期中，性能优于Java泛型实现。`List<int>与List<string>就是两个不同的类型`

  Java选择的泛型实现方式是**“类型擦除式泛型”（Type Erasure Generics）**，具体类型仅存在于源码，编译后替换为裸类型（RawType），元素访问修改时自动插入一些强制类型转换和检查指令，主要为兼容旧版本JDK。`List<int>与List<String>属于同一个类型`

  ```java
  public class TypeErasureGenerics<E> {
  	//Java中不支持的泛型用法
      public void doSomething(Object item) {
          //类型擦除运行期无法取得具体类型，导致下列用法不支持
          if (item instanceof E) {} // 不合法，无法对泛型进行实例判断
          E newItem = new E(); // 不合法，无法使用泛型创建对象
          E[] itemArray = new E[10]; // 不合法，无法使用泛型创建数组
          
          //不支持原始类型的泛型，遇到原生类型自动装箱拆箱（影响性能）
          ArrayList<int> ilist = new ArrayList<int>();
  		ArrayList<long> llist = new ArrayList<long>();
          
          
      }
      
      //List转数组必须传入componentType，由于无法知道T的具体类型
      public static <T> T[] convert(List<T> list, Class<T> componentType) {
      	T[] array = (T[]) Array.newInstance(componentType, list.size());
       	//...
      }
      
      //下面两个方法无法重载，编译失败
      public static void method(List<String> list) {}
      public static void method(List<Integer> list) {}
      //下面两个方法大部分编译器会拒绝编译（返回值不参与重载选择）
      public static String method(List<String> list) { return ""; }
   	public static int method(List<Integer> list) { return 1; }
      
  }
  ```

  所谓的擦除仅仅是对方法的Code属性中的字节码进行擦除，实际上元数据中还是保留了泛型信息，这也是我们在编码时能通过反射手段取得参数化类型的根本依据。

+ 自动装箱拆箱与foreach循环与变长参数

  ```java
  //自动装箱的陷阱
  public static void main(String[] args) {
      Integer a = 1;			//编译解糖成 Integer a = Integer.valueOf(1);
      Integer b = 2;
      Integer c = 3;
      Integer d = 3;
      Integer e = 321;
      Integer f = 321;
      Long g = 3L;
      System.out.println(c == d);		//-128~127存在缓存
      System.out.println(e == f);
      //包装类的“==”运算在不遇到算术运算的情况下不会自动拆箱
      System.out.println(c == (a + b));	//Integer.intValue(3) == Integer.intValue(1+2)
      System.out.println(g == (a + b));	//Long.longValue(3) == Integer.intValue(1+2)
      //包装类的equals()方法不处理数据转型的关系
      System.out.println(c.equals(a + b));  //Integer.equals(Integer)
      System.out.println(g.equals(a + b));  //Long.equals(Integer)
  }
  ```

+ 条件编译

  ```java
  //Java的条件编译优化只支持方法体内部
  
  public static void main1(String[] args) {
      //已知可预测条件
  	if (true) {
          System.out.println("block 1");
      } else {
          System.out.println("block 2");
      }
      //会被编译器简化成
      System.out.println("block 1");
  }
  public static void main2(String[] args) {
      //拒绝编译且编译器将会提示“Unreachable code”
      while (false) {
      	System.out.println("");
  	}
  }
  ```

+ 其余语法糖可通过 `*.java --编译成-- *.class --反编译回-- *.java` 方式查看

##### 编译器的后(阶)端优化

例：JIT即时编译器，运行期把字节码转变成本地机器码的过程

例：AOT提前编译器，提前把程序编译成与目标机器指令集相关的二进制代码的过程

+ 内置多少上述后端编译器与具体JVM实现有关，规范未强制要求

+ 输出本地机器码的“质量”高低才是编译器关注的重点

+ class字节码最初都是通过解释器（Interpreter）进行解释执行的

+ 主流JVM采用解释器和编译器并存的架构执行（分层编译&Mixed-Mode）

  ![解释器和编译器交互](https://tva1.sinaimg.cn/large/0069Mfx5gy1hduek7nscxj30jr09wdkh.jpg)

  | jvm参数 | 参数描述                                    |
  | ------- | ------------------------------------------- |
  | -client | 虚拟机运行在客户端模式（Interpreter+C1JIT） |
  | -server | 虚拟机运行在服务端模式（Interpreter+C2JIT） |
  | -Xint   | 强制虚拟机仅使用Interpreter                 |
  | -Xcomp  | 让虚拟机优先使用JIT，Interpreter兜底        |

###### JIT即时编译器

将判定为“热点代码”（Hot Spot Code）优化编译成本地机器码

+ 何为热点代码？1.被多次调用的方法  2. 方法体中被多次执行的代码块

  <!--但优化热点代码都以整个方法作为编译对象-->

+ 如何判定热点代码（触发JIT）？

  1. 基于采样的热点探测（周期检查各线程栈顶方法）
  2. 基于计数器的热点探测（计数器统计"方法"或"回边"调用次数）

+ `-XX:-BackgroundCompilation`提交JIT后阻塞直至本地代码生成并调用

+ 编译优化过程

  ![客户端模式JIT局部优化](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdugh11wgtj30f40980ue.jpg)

  ![服务端模式JIT全局优化](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdugisgauwj30k80fmtpk.jpg)

###### AOT提前编译器

两条实现思路：1、运行前先静态翻译字节码（就能激进优化）  2、提前执行JIT并缓存下来（不能激进优化）

优点是能针对具体机器做优化提高性能

缺点是不能像JIT做：性能分析制导优化（Profile-Guided Optimization，PGO）、激进预测性优化（Aggressive Speculative Optimization）、链接时优化（Link-Time Optimization，LTO）

###### 常见编译器优化技术

+ **方法内联**（最重要的优化技术之一）将被调用方法体内嵌至发起调用的方法体中

  作用：能消除方法调用成本，为其他优化手段建立基础

  支持内联的非虚方法：invokespecial和invokestatic和final修饰的方法

  优化虚方法不支持内联：

  1. 类型继承关系分析(Class Hierarchy Analysis，CHA)技术，检测已加载类/接口是否存在多于1个的子类/实现/Override

     ```java
     如果类/接口目前仅查询到单个版本，则看作是非虚方法进行守护内联（Guarded-Inlining）
     ```

  2. 内联缓存（Inline Cache）技术，缓存调用的具体虚方法

+ **逃逸分析**（最前沿的优化技术之一）

  作用：并非是直接优化代码的手段，而是为其他优化措施提供依据的分析技术

  缺点：目前这项技术尚未成熟（判断符合逃逸程度需要很高计算成本，甚至产生负收益）

  原理：分析对象动态作用域，当一个对象在方法里面被定义后，它可能被外部方法所引用，例如作为调用参数传递到其他方法中，这种称为方法逃逸；甚至还有可能被外部线程访问到，譬如赋值给可以在其他线程中访问的实例变量，这种称为线程逃逸；从不逃逸、方法逃逸到线程逃逸，称为对象由低到高的不同逃逸程度。

  <!--只要能证明一个对象符合某逃逸程度，就能针对不同的逃逸程度采取不同的优化手段-->

  ![栈上分配](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdvp5zvn6oj30k508ln6a.jpg)

  ![标量替换](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdvp7hy6x5j30k30ex14s.jpg)

  ![同步消除](https://tva1.sinaimg.cn/large/0069Mfx5gy1hdvp8f6fayj30k202mq5k.jpg)

  | jvm参数                        | 参数描述                       |
  | ------------------------------ | ------------------------------ |
  | -XX:+DoEscapeAnalysis          | 手动开启逃逸分析               |
  | -XX:+PrintEscapeAnalysis       | 查看逃逸分析情况               |
  | -XX:+EliminateAllocations      | 开启对无法方法逃逸的标量替换   |
  | -XX:+PrintEliminateAllocations | 查看标量替换情况               |
  | -XX:+EliminateLocks            | 开启对无法线程逃逸的同步锁消除 |

+ **公共子表达式消除**（语言无关的经典优化技术之一）

  原理：如果一个表达式E之前已经被计算过了，并且从先前的计算到现在E中所有变量的值都没有发生变化，那么E的这次出现就称为公共子表达式。对于这种表达式，没有必要花时间再对它重新进行计算，只需要直接用前面计算过的表达式结果代替 E。如果这种优化仅限于程序基本块内，便可称为局部公共子表达式消除（Local Common Subexpression Elimination），如果这种优化的范围涵盖了多个基本块，那就称为全局公共子表达式消除（Global Common Subexpression Elimination）。

+ **数组边界检查消除**（语言相关的经典优化技术之一）

  原理：Java属于动态安全语言，例如对数组foo[i]读写访问会自动进行`"i>=0 && i<foo.length"`检查，但这个隐式检查每次都执行会造成性能损耗，因此需特定条件下考虑消除检查

  ```java
  //例子1: 编译期就确定绝不会数组越界，则可消除检查
  int[] foo = new int[10];
  int i = foo[3];
  //例子2: for循环中可判定i的取值范围永远在区间[0，foo2.length)
  int[] foo1 = new int[5];
  int[] foo2 = new int[10];
  for (int i = 0; i < foo1.length; i++) {
      int j = foo2[i];
  }
  //例子3: 隐式异常处理，空指针检查和算术运算中除数为零检查均采用此方案
  ```

  语言相关的其它消除：如自动装箱消除（Autobox Elimination）、安全点消除（Safepoint Elimination）、消除反射（Dereflection）等

##### Java内存模型与线程

+ 计算机概述 -- 高效并发的副作用

  缓存一致性协议和指令重排

  ![硬件内存交互](https://tva1.sinaimg.cn/large/0069Mfx5gy1he16l6c325j30jy0cxn4h.jpg)

+ JMM -- 硬件无关性解决并发与其副作用

  <!--借鉴"Shared Memory Multiprocessors System"实现思路：缓存一致性协议+指令屏障-->

  ![JMM内存交互](https://tva1.sinaimg.cn/large/0069Mfx5gy1he16mej296j30k10cqtgw.jpg)

  ```java
  JMM中定义了8个原子操作用于 主内存<->工作内存 之间交互操作
  1.lock（锁定）：作用于主内存的变量，它把一个变量标识为一条线程独占的状态。
  2.unlock（解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定。
  3.read（读取）：作用于主内存的变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的 load 动作使用。
  4.load（载入）：作用于工作内存的变量，它把 read 操作从主内存中得到的变量值放入工作内存的变量副本中。
  5.use（使用）：作用于工作内存的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用变量的值的字节码指令时将会执行这个操作。
  6.assign（赋值）：作用于工作内存的变量，它把一个从执行引擎接收的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时执行这个操作。
  7.store（存储）：作用于工作内存的变量，它把工作内存中一个变量的值传送到主内存中，以便随后的 write 操作使用。
  8.write（写入）：作用于主内存的变量，它把 store 操作从工作内存中得到的变量的值放入主内存的变量中。
  
  此处变量指：实例字段、静态字段和构成数组对象的元素，不包括局部变量和方法参数
  read 与 load 操作 & store 与 write 操作 & lock 与 unlock 操作必须配对顺序执行（不要求连续执行）
  发生 assign 操作后必须把数据从工作内存同步回主内存，新的变量只能在主内存中诞生然后拷贝到工作内存中使用
  lock 操作需先清空工作内存副本，unlock操作需先同步工作内存副本回主内存
  
  volatile变量的特殊规则
  volatile的可见性是要求各线程工作内存使用该变量前要先同步主存，规避不一致出现的条件（普通变量不保证）
  volatile变量的运算在并发下一样是不安全的（java运算符并非原子操作）
  volatile的禁止指令重排序优化是通过插入内存屏障指令实现
  
  long或double类型变量的宽松规定：允许将64位数据的读写划分为两次32位操作进行（兼容32位JVM）
  实际开发中，除非该数据有明确可知的线程竞争，否则不需要刻意把用到的long和double变量专门声明为volatile
  ```

  + 原子性

    基本数据类型的访问、读写都具备原子性（上述的read\\load\\...等）

    synchronized使用monitorenter和monitorexit指令隐式使用lock和unlock满足原子操作

  + 可见性

    volatile、synchronized和final关键字均能保证可见性

  + 有序性

    Java程序中天然的有序性可以总结为一句话：如果在本线程内观察，所有的操作都是有序的；如果在一个线程中观察另一个线程，所有的操作都是无序的。前半句是指“线程内似表现为串行的语义”（Within-Thread As-If-Serial Semantics），后半句是指“指令重排序”现象和 “工作内存与主内存同步延迟”现象。
    ![java保证有序性](https://tva1.sinaimg.cn/large/0069Mfx5gy1he1flkbb4hj30k303igou.jpg)

  **Happens-Before原则：针对8个原子操作规则并发更通俗易懂的解答**

  例：如果A先行于B，则说明A"操作"早于B且A产生的"影响"能被B观察到

  以下是JMM保证的"天然的"Happens-Before关系，如果两个操作之间的关系不在此列，并无法从下列规则推导出来，则它们就没有顺序性保障，虚拟机可以对它们随意地进行重排序

  ![8个Happens-Before原则](https://tva1.sinaimg.cn/large/0069Mfx5gy1he1g7bxps4j30k60lznbr.jpg)

  ```java
  private int value = 0;
  public void setValue(int value) {
   this.value = value;
  }
  public int getValue() {
   return value;
  }
  
  问：A线程调用setter,B线程调用getter,怎么改能保证A先行于B?
  答：1.使用synchronized修饰getter/setter方法
     2.使用volatile修饰value变量
  ```

  推导结论：时间先后顺序与先行发生原则之间基本没有因果关系，衡量并发安全问题的时候不要受时间顺序的干扰，一切必须以先行发生原则为准。

+ 计算机多线程模型

  1. 使用内核线程（Kernel-Level Thread， KLT）实现（1:1）

     程序一般不会直接使用KLT，而是使用其提供的高级接口---轻量级进程（Light Weight Process，LWP）

     ![LWP与KLT-1:1](https://tva1.sinaimg.cn/large/0069Mfx5gy1he2b2hzvmej30ka0gjth7.jpg)

  2. 用户线程（User Thread，UT）实现（1:N）

     线程的创建、销毁、切换和调度都由用户自行控制，缺点是处理进程阻塞和多处理器映射

     ![UT-1:N](https://tva1.sinaimg.cn/large/0069Mfx5gy1he2b6zrgx4j30k70e6q9i.jpg)

  3. 用户和内核线程混合实现（M:N）

     用户线程的系统调用要通过轻量级进程来完成，降低整个进程被完全阻塞风险

     ![UT与LWP-M:N](https://tva1.sinaimg.cn/large/0069Mfx5gy1he2ba7bksnj30jx0e37a4.jpg)

+ 计算机线程调度策略

  1. 抢占式（Preemptive ThreadsScheduling）线程调度

     每个线程将由系统来分配执行时间，线程的切换不由线程本身来决定，也不会有一个线程导致整个进程甚至整个系统阻塞的问题

  2. 协同式（Cooperative Threads-Scheduling）线程调度

     线程的执行时间由线程本身来控制，线程把自己的工作执行完了之后，要主动通知系统切换到另外一个线程上去。缺点是不稳定，只要有 一个进程坚持不让出处理器执行时间，就可能会导致整个系统崩溃。

+ Java与线程

  多线程模型：主流商用JVM均采用（1:1）内核线程模型实现

  线程调度：均采用抢占式线程调度

  线程状态转换：New、Running、Waiting、Timed Waiting、Blocked、Terminated

  ![java线程状态转换](https://tva1.sinaimg.cn/large/0069Mfx5gy1he2bip45ffj30g20aaq3w.jpg)

+ Java与协程

  大量用户线程切换（用户态<-->内核态）的开销会接近计算本身的开销

  有栈协程：用户自己模拟多线程调用，并进行调用栈的保护、恢复工作

  无栈协程：典型应用await/async/yield关键字，本质上是一种有限状态机，状态保存在闭包里

  java的协程方案：纤程（Fiber）、Loom 项目、Quasar 协程库

##### 线程安全与锁优化

先要并发安全再谈高效并发

###### Java中的线程安全

+ 线程安全的定义：”当多个线程同时访问一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行额外的同步，或者在调用方进行任何其他的协调操作，调用这个对象的行为都可以获得正确的结果，那就称这个对象是线程安全的。”

+ 线程安全的强度

  1. 不可变：只要一个不可变的对象被正确地构建出来，那其外部的可见状态永远都不会改变

     例：final修饰的基本数据类型、String类的方法调用只返回新构造字符串对象、Number类下部分子类

  2. 绝对线程安全：虽然提供者内部保证线程安全，但要求调用者正确同步调用

     ```java
     public static void main(String[] args) {
         Vector<Integer> vector = new Vector<>();
         while (true) {
             for (int i = 0; i < 10; i++) { vector.add(i); }
             Thread removeThread = new Thread(() -> {
                 //未正确同步调用可能抛出ArrayIndexOutOfBoundsException
                 for (int i = vector.size() - 1; i >= 0; i++) { vector.remove(i); }
             });
             Thread printThread = new Thread(() -> {
                 //未正确同步调用可能抛出ArrayIndexOutOfBoundsException
                 for (int i = 0; i < vector.size(); i++) { 
                     System.out.println((vector.get(i))); 
                 }
             });
             removeThread.start();
             printThread.start();
             //不要同时产生过多的线程，否则会导致操作系统假死
             while (Thread.activeCount() > 50);
         }
     }
     ```

  3. 相对线程安全：针对对象单次调用是线程安全的，但对于一些特定顺序的连续调用，就可能需要在调用端使用额外的同步手段来保证调用的正确性

  4. 线程兼容：指对象本身并不是线程安全的，但是可以通过在调用端正确地使用同步手段来保证对象在并发环境中可以安全地使用

  5. 线程对立：不管调用端是否采取了同步措施，都无法在多线程环境中并发使用代码

     例：Thread类的suspend()&resume()、System.setIn()&Sytem.setOut()等

+ 线程安全的手段

  1. 互斥同步（Mutual Exclusion & Blocking Synchronization）
  
     ![互斥与同步](https://tva1.sinaimg.cn/large/0069Mfx5gy1he3qbu3j5wj30k4053n1u.jpg)
  
     悲观的并发策略
  
     Synchronized关键字：可重入、等待不可中断、支持wait() & notify()/notifyAll()实现一个隐含条件
  
     JUC包的Lock接口：可重入、等待可中断、支持是否公平锁、支持绑定多个Condition条件
  
  2. 非阻塞同步（Non-Blocking Synchronization）
  
     乐观的并发策略（需要”硬件指令集”原子性操作支持，例CAS原子指令）
  
     `sun.misc.Unsafe`类接口
  
     “ABA 问题”解决：带版本号的AtomicStampedReference，但实际性能不如互斥同步
  
  3. 无需同步方案（Un-Need Locking）
  
     ![同步与线程安全](https://tva1.sinaimg.cn/large/0069Mfx5gy1he76me9vl8j30k0039n0n.jpg)
  
     可重入代码（Reentrant Code）/纯代码（Pure Code）：不依赖全局变量、存储在堆上的数据和公用的系统资源，用到的状态量都由参数中传入，不会调用非可重入的方法等
  
     线程本地存储（Thread Local Storage）：MQ架构、Web-Thread-per-Request、ThreadLocal类

###### JVM中的锁优化

![32位JVM对象头MarkWord内存布局](https://tva1.sinaimg.cn/large/0069Mfx5gy1he77jfpqh9j30gb06gwfh.jpg)

1. 自旋锁与自适应自旋：主要依赖JVM的性能监控信息
2. 锁消除：主要依赖逃逸分析的数据支持
3. 锁粗化
4. 轻量级锁
5. 偏向锁
