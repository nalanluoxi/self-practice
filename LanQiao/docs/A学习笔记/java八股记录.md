# java八股


# JavaSE

## 为什么金融类型的数据要使用BigDecimal？
在金融、电商、会计等场景中，绝对不能用 float / double 存储、计算金额，必须用 Java 的 BigDecimal，核心原因只有一个：
float/double 会产生精度丢失，导致钱算错；而 BigDecimal 可以精确表示小数，保证金融计算绝对准确。
1. 先看一个致命问题：float/double 精度丢失
   直接运行这段代码，你就能直观感受到问题：
   java
   运行
   public class Test {
   public static void main(String[] args) {
   double a = 0.1;
   double b = 0.2;
   System.out.println(a + b);
   // 输出结果：0.30000000000000004（不是 0.3！）
   }
   }
   0.1 + 0.2 不等于 0.3，这在金融里是灾难：
   算账少一分、多一分，对账永远对不上
   交易、计费、利息计算出现错误，直接造成资金损失
2. 根本原因：二进制无法精确表示十进制小数
   float 和 double 是二进制浮点数，设计初衷是科学计算，不是精确小数计算。
   就像 十进制无法精确表示 1/3 = 0.3333... 一样，
   二进制无法精确表示 0.1、0.2 这类十进制小数，只能存一个无限接近的近似值。
   一旦累加、相乘，误差会越来越大，完全不能用于钱。
3. BigDecimal 的核心优势（金融场景刚需）
   ① 绝对精确的十进制表示
   BigDecimal 用十进制字符串 / 整数存储数字，没有任何精度丢失，0.1 就是 0.1，不会变成近似值。
   java
   运行
   BigDecimal a = new BigDecimal("0.1");
   BigDecimal b = new BigDecimal("0.2");
   System.out.println(a.add(b));
   // 输出：0.3 （完全正确）
   ② 支持自定义舍入规则
   金融计算必须严格控制四舍五入（如银行家舍入、四舍五入、向上取整）：
   利息计算
   汇率换算
   税费计算
   金额分位
   BigDecimal 提供 RoundingMode 枚举，满足所有金融规范。
   ③ 支持超大数值范围
   金融数据可能出现超大金额（如万亿、亿亿级），float/double 会溢出，BigDecimal 理论上无大小限制。
   ④ 提供精准的加减乘除、比较运算
   所有计算都是精确十进制运算，完全符合会计准则。
4. 对比总结（一看就懂）
   表格
   类型	精度	适用场景	金融场景是否可用
   float/double	不精确，有精度丢失	科学计算、图形、游戏	❌ 绝对禁止
   BigDecimal	完全精确，无丢失	金融、电商、会计、计费	✅ 强制使用
   int/long	精确（分 / 厘存储）	简易金额存储	✅ 可选（麻烦）
   补充：也有人用 long 存分（比如 1 元存 100），也能避免精度问题，但计算、格式化、展示都很麻烦，BigDecimal 是金融开发的标准最优解。
## bigDecimal的常见坑点有哪些
坑 1：用 new BigDecimal(double) 构造，精度直接丢失
错误写法
java
运行
BigDecimal a = new BigDecimal(0.1);
// 结果不是 0.1，而是 0.1000000000000001...
原因：你传进去的 0.1 本身就是 double 不精确值，BigDecimal 也救不了。
正确写法
java
运行
// 字符串构造（唯一推荐）
BigDecimal a = new BigDecimal("0.1");
坑 2：用 equals() 比较大小，结果完全不准
错误写法
java
运行
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("1.00");

System.out.println(a.equals(b));
// 输出 false！！！
原因：equals() 会同时比较数值 + 精度位数，1.0 和 1.00 精度不同，判定不相等。
正确写法（金融比较必须用）
java
运行
System.out.println(a.compareTo(b) == 0);
// 输出 true
compareTo 规则：
返回 -1 → 小于
返回 0 → 等于
返回 1 → 大于
坑 3：除法 divide 不指定舍入模式，直接抛异常
错误写法
java
运行
BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("3");
a.divide(b);
// 抛异常：ArithmeticException（无限循环小数）
原因：除不尽时，BigDecimal 不知道怎么取舍，直接报错。
正确写法
java
运行
// 保留2位小数 + 四舍五入
a.divide(b, 2, RoundingMode.HALF_UP);
金融常用舍入模式：
HALF_UP：四舍五入
HALF_EVEN：银行家舍入（美股、银行常用）
UP：向上取整
DOWN：向下取整
坑 4：null 值调用方法，空指针异常
java
运行
BigDecimal money = null;
money.add(new BigDecimal("10"));
// 空指针！
解决：使用前必须判空，或默认赋值 BigDecimal.ZERO。
坑 5：忽略 scale（小数位数），导致展示异常
java
运行
new BigDecimal("10").toString();
// 输出 10
new BigDecimal("10.00").toString();
// 输出 10.00
如果前端 / 数据库要求统一 2 位小数，必须手动设置：
java
运行
bigDecimal.setScale(2, RoundingMode.HALF_UP);
坑 6：加减乘除不会修改原对象，必须接收返回值
BigDecimal 是不可变对象，所有计算都会生成新对象。
错误写法
java
运行
BigDecimal a = new BigDecimal("10");
a.add(new BigDecimal("5"));
// a 还是 10！！！
正确写法
java
运行
a = a.add(new BigDecimal("5"));
坑 7：用 valueOf(0.1) 依然会丢精度
错误写法
java
运行
BigDecimal.valueOf(0.1);
原因：本质还是传了 double。
正确写法
java
运行
BigDecimal.valueOf("10");
// 或者
new BigDecimal("0.1");
坑 8：与 0 比较错误
错误写法
java
运行
if (bigDecimal == BigDecimal.ZERO)
if (bigDecimal.equals(BigDecimal.ZERO))
正确写法
java
运行
if (bigDecimal.compareTo(BigDecimal.ZERO) == 0)
## 什么是自动装箱和拆箱
自动装箱、自动拆箱（Java 极简版）
一句话：基本类型 ↔ 包装类，编译器自动帮你互相转换，不用手动 new。
1. 对应关系
   表格
   基本类型	包装类
   byte	Byte
   short	Short
   int	Integer
   long	Long
   float	Float
   double	Double
   char	Character
   boolean	Boolean
2. 自动装箱
   基本类型 → 包装类，自动封装对象
   java
   运行
   Integer num = 100;
   // 编译器自动变成：Integer num = Integer.valueOf(100);
3. 自动拆箱
   包装类 → 基本类型，自动取出数值
   java
   运行
   int n = num;
   // 编译器自动变成：int n = num.intValue();
4. 常见场景
   集合只能存对象，自动装箱
   java
   运行
   List<Integer> list = new ArrayList<>();
   list.add(666); // 装箱
   运算时自动拆箱
   java
   运行
   Integer a = 20;
   int b = a + 10; // a先拆箱计算
5. 高频坑：Integer 缓存 (-128~127)
   java
   运行
   Integer i1 = 100;
   Integer i2 = 100;
   System.out.println(i1 == i2); // true 缓存复用

Integer i3 = 200;
Integer i4 = 200;
System.out.println(i3 == i4); // false 新建对象
## Integer的（-128-127）缓存池
Integer 缓存池（-128~127）极简讲解
1. 是什么
   Java 预先缓存创建好 -128 ~ 127 范围内的 Integer 对象，调用Integer.valueOf()时，直接复用缓存对象，不再新建，节省内存、提升效率。
2. 触发时机
   自动装箱本质调用 Integer.valueOf(int)，只有这个方法会走缓存；new Integer() 不走缓存。
3. 代码演示
   java
   运行
   // 范围内，复用缓存同一对象
   Integer a = 100;
   Integer b = 100;
   System.out.println(a == b); // true

// 超出范围，新建不同对象
Integer c = 128;
Integer d = 128;
System.out.println(c == d); // false
4. 底层原理
   JVM 默认缓存区间固定 最小值 - 128，最大值 127
   内部维护一个 Integer 数组，提前实例化好常用小整数。
5. 核心坑点
   == 比较的是对象地址，缓存内地址相同，超出则不同
   数值相等判断一律用 equals ()，禁止用 ==
   new Integer(10) 永远创建新对象，不使用缓存
## Integer a=128; Integer b=128; a==b 为什么返回 false？
同上

# 面向对象

## 多态体现在哪些方面
1. 多态体现方面
   父类引用指向子类对象
   方法重载、方法重写
   向上转型、向下转型
## 什么是编译时多态和运行时多态
2. 编译时多态（静态多态）
   编译阶段就确定调用哪个方法，方法重载属于它。
   根据参数个数、类型、顺序匹配方法。
3. 运行时多态（动态多态）
   运行时才判定实际对象类型，方法重写属于它。
   父类引用调用方法，执行子类重写逻辑。
## 重载和重写的区别
对比	重载 (Overload)	重写 (Override)
位置	同一个类	父子类
方法名	必须相同	必须相同
参数列表	必须不同	必须相同
返回值	可不同	协变兼容
权限	宽松无限制	权限不能变小
异常	可不同	异常范围不能扩大
多态类型	编译时多态	运行时多态
## Java为什么不支持多继承
菱形问题：多个父类同名方法，子类无法判定执行哪个
避免类层级逻辑混乱、歧义冲突
用接口多实现替代，规避冲突问题

### 菱形问题（极简通俗讲）
假设一个子类同时继承两个父类，两个父类有一模一样的方法，就会出现歧义，这就是菱形问题。
1. 画图理解菱形结构
   plaintext
   父类A    父类B
   \      /
   \    /
   子类C
   形状像菱形，故名菱形问题。
2. 代码模拟冲突场景
   java
   运行
   // 两个父类都有同一个show方法
   class A {
   public void show(){
   System.out.println("A方法");
   }
   }
   class B {
   public void show(){
   System.out.println("B方法");
   }
   }

// 假如Java允许类多继承
class C extends A,B{
// 调用show()，到底执行A还是B的？编译器分不清
}
子类调用show()，编译器不知道该跑哪个父类方法，产生二义性。
3. Java 解决方案
   类只能单继承，杜绝菱形冲突
   接口可以多实现
   接口只有方法声明、无实现体，就算同名方法也不会冲突，子类必须重写统一逻辑。
   一句话总结
   多个父类存在同名实现方法，子类无法抉择调用谁，就是菱形问题
## 静态代码块，实例代码块，构造方法的执行顺序是什么
优先级：静态 > 实例 > 构造
完整顺序：
父类静态代码块
子类静态代码块
父类实例代码块
父类构造方法
子类实例代码块
子类构造方法
单次创建同类对象：
静态块只执行一次；实例块、构造每次 new 都执行。

# 反射

## 什么是反射
程序运行期间，动态获取类信息、创建对象、调用方法、操作属性的机制。
正常编码是先写代码调用类，反射反过来：运行时拆解类结构。
## 反射为什么性能这么低
绕过编译期语法检查，运行时解析字节码
权限校验、类型推断、安全检查额外开销大
无法被 JVM 即时编译优化
## 反射的优缺点是什么
### 优点
动态创建对象、调用方法，灵活性极强
解耦代码，适配通用框架
### 缺点
性能差
破坏封装，可强行访问私有成员
代码可读性、维护性变差
编译期无法报错，问题运行才暴露
## 获取Class对象的三种方式是什么，各自的区别是什么
类名.class
Class<User> clazz = User.class;
编译期获取，不加载类，效率最高
对象.getClass ()
user.getClass();
已有实例才能调用，运行时获取
Class.forName ("全类名")
Class.forName("com.User");
运行时加载类、执行静态代码块

详细介绍：

1、 User.class
编译期拿到 Class 引用，不会触发类加载，静态代码块不执行。
2、obj.getClass()
对象已实例化，类早已加载完成，不会重复执行静态代码块。
3、 Class.forName ("全限定名")
主动触发类加载、初始化，唯一会执行静态代码块。
### 关键区分
触发静态代码块：仅 Class.forName
触发时机
类名.class：编译阶段，无类初始化
getClass ()：运行时，类已初始化完毕
forName：运行时，主动加载并初始化类

补充小例子
java
运行
class Demo{
static {
System.out.println("静态块执行");
}
}
// 1. 不打印
Class c1 = Demo.class;
// 2. 先new已经加载，再getClass也不打印
Demo d = new Demo();
Class c2 = d.getClass();
// 3. 立刻打印静态块内容
Class c3 = Class.forName("Demo");
## 如何通过反射创建一个对象
// 无参构造
Class<?> clazz = User.class;
User user = (User) clazz.newInstance();

// 有参构造
Constructor c = clazz.getConstructor(String.class,int.class);
User u = (User) c.newInstance("张三",20);
## 如何通过反射调用私有字段和私有方法
一、反射操作私有字段
getDeclaredField(字段名)：获取本类任意权限成员变量，包含 private
setAccessible(true)：关闭权限校验，突破私有访问限制
set(对象实例, 赋值内容)：给指定对象的字段设值
java
运行
// 获取私有name字段
Field nameField = clazz.getDeclaredField("name");
// 暴力访问私有成员
nameField.setAccessible(true);
// 给对象赋值
nameField.set(obj, "李四");
// 取值
String val = (String) nameField.get(obj);
二、反射调用私有方法
getDeclaredMethod(方法名,参数类型...)：获取私有方法
setAccessible(true)：破除私有权限
invoke(对象实例,方法入参...)：执行方法，返回执行结果
java
运行
// 获取无参私有say方法
Method sayMethod = clazz.getDeclaredMethod("say");
// 放开访问权限
sayMethod.setAccessible(true);
// 调用方法
Object result = sayMethod.invoke(obj);
补充区分
getField：只能拿 public 字段，拿不到私有
getDeclaredField：可获取所有权限字段
方法同理，getMethod仅 public，getDeclaredMethod全包权限
## Spring，mybatis哪些地方用到了反射
Spring
IOC 容器实例化 Bean
依赖注入 DI 赋值成员变量
AOP 动态代理增强方法
MyBatis
映射数据库实体类，封装查询结果
创建 Mapper 接口代理对象
反射调用实体 set/get 方法封装数据

# 异常

## Error和Exception的区别

一句话总结：
Error：JVM 级别的严重错误，程序处理不了，只能崩溃
Exception：代码级别的异常，程序可以捕获处理
详细区别
表格
Error	Exception
严重性	极严重（JVM 崩了）	一般（代码问题）
能否捕获	能捕获，但不应该处理	必须捕获 / 抛出
来源	JVM、系统、硬件	代码逻辑、参数、空指针等
例子	OOM、栈溢出	空指针、数组越界、类型转换

## 常见的Error/Exception有哪些

常见 Error（你处理不了）

StackOverflowError：栈溢出（递归死循环）
OutOfMemoryError：内存溢出（OOM）
NoClassDefFoundError：找不到类定义
NoSuchMethodError：找不到方法

常见 Exception（你必须处理）

运行时异常（RuntimeException，不强制捕获）

NullPointerException 空指针
IndexOutOfBoundsException 数组越界
ClassCastException 类型转换异常
IllegalArgumentException 参数不合法
ArithmeticException 算术异常（除 0）

编译时异常（必须捕获 / 抛出）
IOException
SQLException
ClassNotFoundException
InterruptedException

## throw和throws的区别

throw
方法内部手动抛出一个异常
语法：throw new Exception();
throws
方法声明上，表示这个方法可能抛出异常
交给调用者处理
语法：void fun() throws Exception { }
一句话记忆
throw：我扔出异常
throws：我声明可能有异常

## try-catch-finally的执行顺序

标准顺序：
try 执行
出现异常 → catch 执行
finally 一定执行（除非 JVM 退出）
plaintext
try → catch（有异常）→ finally

## finally中的代码一定会被执行吗

绝大多数情况：一定会执行。
只有 2 种情况不执行：
System.exit(0); 直接关闭 JVM
线程被强制杀死
只要 JVM 活着，finally 就一定执行

## finally 块中如果有 return 语句，会发生什么？

超级大坑：finally 里的 return 会覆盖 try/catch 里的 return！
例子：
java
运行
int test(){
try {
return 1;
} finally {
return 2;
}
}
结果返回 2
规则
finally 最后执行
finally 里的 return 直接结束方法
会覆盖之前的返回值
规范：finally 里绝对不要写 return！

## NoClassDefFoundError 和 ClassNotFoundException 的区别？

ClassNotFoundException（异常）
运行时找不到类
用 Class.forName() 找不到时抛出
可以捕获
NoClassDefFoundError（错误）
编译时存在，运行时找不到
类加载失败
JVM 错误，不能处理
一句话记忆
ClassNotFoundException：找不着类
NoClassDefFoundError：类加载失败

## OutOfMemoryError 和 StackOverflowError 分别是什么原因导致的？

StackOverflowError（栈溢出）
原因：方法调用层级太深 → 栈满了
最常见：递归没有出口
OutOfMemoryError（内存溢出 OOM）
原因：堆内存满了，无法分配新对象
常见：
死循环创建对象
加载数据太多
内存泄漏

# object

## ==和equals有什么区别

==
基本类型：比较数值
引用类型：比较内存地址
equals
默认和==一样比较地址
重写后比较对象内容值

## 为什么重写hashcode就要重写equals

1. 核心规约
Java 规定：
对象equals相等，hashCode 必定相等
hashCode 相等，equals未必相等
2. 只重写 hashCode、不重写 equals 的问题
   原有默认equals仅比较对象内存地址。
   即便你自定义算法让两个内容一致对象 hashCode 相同，地址不同时，equals依旧判定不相等。
3. 哈希容器业务异常
   以 HashSet、HashMap 为例：
   先比对 hashCode，相同再调用 equals 校验
   内容一致对象 hashCode 相同，但 equals 返回 false
   集合会判定为不同对象，存入重复数据，去重失效
4. 总结
   单独改写 hashCode，无法保证等值对象判定逻辑统一，违背设计规范，哈希容器存取逻辑出错，因此重写 hashCode 务必同步重写 equals。

## toString() 方法的作用是什么？为什么建议重写？

作用：打印对象时，输出对象描述信息
默认：输出类名 + 地址哈希值，无业务意义
重写：输出成员属性值，方便日志打印、调试查看

## 两个对象的 hashCode () 相同，equals () 一定为 true 吗？反过来呢？
hashCode 相同 → equals 不一定 true
哈希碰撞，不同对象可算出相同哈希码
equals 为 true → hashCode 一定相同
硬性约定，必须遵守

# String


## String的底层是怎么实现的
JDK8 及更早：内部用 **char []** 字符数组存数据
JDK9 开始：改用 **byte []** 字节数组，缩减内存开销
String 类、内部数组引用都被final修饰，无法直接改动原有数据
## String为什么不可变/String不可变的优势是什么
不可变原因
类 final 不可继承，底层数组私有且引用固定，修改内容只会生成新字符串，原对象始终不变。
优势
支持常量池复用，减少内存消耗
天然线程安全，多线程共用不会冲突
哈希值固定，适合当作 HashMap 的 key
数据不会被篡改，安全性高
## String s = new String ("abc") 创建了几个对象？
总共2 个
字符串字面量abc，先在常量池创建对象
new 关键字在堆中新建 String 实例，引用指向常量池内容
## String s="a"+"b"+"c"; 创建了几个对象？
仅1 个
编译器常量优化，编译阶段直接拼接成abc，只在常量池生成一个对象。
## + 号拼接字符串的底层原理是什么？
纯常量相加：编译期直接合并，无额外对象产生
含变量相加：底层自动创建StringBuilder拼接，最后转成 String 返回
频繁拼接会反复创建销毁对象，性能较差
## String 的 hashCode() 方法是如何实现的？
按字符依次计算：h = h * 31 + 字符ASCII值
选 31：质数减少哈希碰撞，乘法运算效率高
## 如何比较两个字符串的内容是否相等？为什么不能用 ==？
正确写法：equals()，逐个字符比对实际内容
不能用==：仅判断内存地址，内容相同但对象不同，结果也会 false
## 字符串常量池的工作原理
专门缓存字面量字符串
创建字面量先查询池子，有就直接复用引用；没有再创建并存入池子，实现对象共享省内存。
## intern () 方法的作用
调用后把字符串存入常量池
池内已有相同内容，直接返回池中引用；没有则存入后返回池引用，统一地址节省内存。
## String，Stingbuffer，Stringbuilder的区别，使用场景
String：内容不可变，改动生成新对象，效率低
场景：少量文本、极少修改
StringBuffer：内容可变，方法加锁保证线程安全，速度偏弱
场景：多线程下字符串拼接
StringBuilder：内容可变，无锁非线程安全，执行速度最快
场景：单线程大量拼接操作

# 集合


## HashMap 的扩容机制是什么？默认容量、负载因子、扩容倍数分别是多少？

## HashMap 的 put () 方法完整执行流程是什么？

## HashMap 的 get () 方法完整执行流程是什么？

## HashMap 如何解决哈希冲突？

## HashMap 为什么是线程不安全的？（JDK7 头插法死循环、JDK8 数据覆盖）

## HashMap 头插法和尾插法的缺点

## HashMap 和 Hashtable 的区别？

## HashMap 的负载因子为什么是 0.75？

## HashMap 中链表转红黑树的阈值为什么是 8？转回链表的阈值为什么是 6？

## HashMap为什么不直接用红黑树？而是要先链表再做树？

## ArrayList 和 LinkedList 的底层实现和性能对比？

## ArrayList 的扩容机制是什么？

## HashSet 如何保证元素不重复？底层实现是什么？

## HashMap 和 TreeMap 的区别？

## LinkedHashMap 的有序性是如何实现的？

## TreeSet 的排序原理是什么？Comparable 和 Comparator 接口的区别？

# 杂项


## 深拷贝和浅拷贝的区别
1. 基础结构
   Java 对象分为：基本数据类型、引用数据类型
   基本类型：值存在自身内存
   引用类型：仅存对象内存地址，真实数据在堆
2. 浅拷贝
   只复制当前对象顶层内存
   基本类型：拷贝真实值，互不影响
   引用类型：只拷贝地址，新旧对象指向同一个堆实例
   修改引用属性，原对象数据同步改变
   plaintext
   原对象引用 → 堆实体
   拷贝对象引用 → 同一个堆实体
3. 深拷贝
   逐层完整复制所有层级对象
   引用属性也新建独立堆对象，新旧对象完全隔离
   修改任意属性，互不干扰。
4. 核心区别
   浅拷贝：引用共享，改动互相影响
   深拷贝：全新对象，数据彻底独立
   实现：浅拷贝默认clone()；深拷贝重写 clone、序列化、手动新建对象
## 哈希冲突的解决解决方法
哈希冲突：不同 key 算出相同数组下标，位置重叠。
1. 链地址法（HashMap 使用）
   数组下标位置挂单向链表，冲突元素连成链表
   查询先算下标，再遍历链表匹配 key。
2. 开放寻址法
   位置被占，按规则向后查找空闲空位存放。
3. 再哈希法
   冲突后换另一套哈希算法重新计算下标。
4. 公共溢出区
   正常数组存正常数据，冲突数据统一放进溢出区域。
   面试重点：Java HashMap = 数组 + 链表 + 红黑树，底层链地址法。
## 什么是AIO,NIO,BIO
先明确基础概念
FD 文件描述符：操作系统内核用整数标识文件、网卡套接字、管道等 IO 资源，内核维护FD 数组，下标对应资源句柄，程序通过 FD 操作对应 IO 对象。
IO 两步核心：1. 内核从磁盘 / 硬件读取数据到内核缓冲区；2. 数据拷贝到用户进程缓冲区
一、BIO 同步阻塞 IO
底层流程
应用进程调用read(fd)读取磁盘文件
内核检测当前 FD 无就绪数据，直接把当前线程阻塞挂起，线程放弃 CPU
磁盘硬件寻址、读取数据，存入内核缓冲区
数据就绪后，内核唤醒阻塞线程，将内核缓冲区数据拷贝到用户缓冲区
线程拿到数据，执行业务逻辑
FD 数组视角
每次文件 / 网络连接都会分配独立 FD，一个线程绑定一个 FD。FD 无数据，对应线程就阻塞，无法处理其他 FD 请求。
特点
同步：线程主动发起读写，全程等待结果
阻塞：无数据时线程休眠，不占用 CPU
缺陷：海量 FD 连接时，必须创建大量线程，内存、上下文切换开销极大
二、NIO 同步非阻塞 + 多路复用
1. 非阻塞基础模式
   调用read(fd)，内核发现无数据，立刻返回空标识，线程不会阻塞，可轮询其他 FD 资源。
   频繁轮询空 FD 会浪费 CPU，由此衍生多路复用器。
2. 多路复用器（select/poll/epoll）& FD 数组
   程序把一批需要监听的FD 集合提交给内核多路复用器
   内核遍历内部 FD 数组，持续监控所有绑定 FD 的读写就绪状态
   只要任意一个 FD 对应磁盘 / 网络数据就绪，就唤醒用户线程，返回就绪 FD 列表
   线程仅针对就绪 FD 执行数据拷贝读取，其余未就绪 FD 暂时不管
   底层读写步骤
   线程委托内核监控批量 FD
   磁盘数据载入内核缓冲区，对应 FD 标记就绪
   线程拿到就绪 FD，主动执行数据拷贝到用户空间
   处理完成后，继续交由复用器监听
   特点
   同步：依旧是线程主动发起读写操作
   非阻塞、一对多：单线程管理成千上万个 FD，依托 FD 数组批量监听
   主流实现：epoll，高效管理大规模 FD 集合，Netty、服务器均基于此模型
   三、AIO 异步非阻塞 IO
   底层流程
   应用发起异步读写请求，附带回调函数，调用后线程立即返回，无需等待
   内核接管全部工作：磁盘寻址、数据读取、内核缓冲区缓存、拷贝至用户缓冲区
   整套 IO 操作全部完成后，内核主动触发预先绑定的回调函数，通知应用处理数据
   FD 数组视角
   FD 仅作为资源标识，线程不参与等待与轮询，内核全权调度 FD 对应 IO 任务，完成后异步通知上层。
   特点
   异步：线程发起请求后直接脱离 IO 流程
   无等待阻塞，CPU 资源利用率最高
   适合大文件磁盘批量读写、高吞吐离线业务
   核心对比总结
   表格
   模型	读写触发	线程状态	FD 数组作用	磁盘交互特点
   BIO	线程主动调用	阻塞等待	单线程绑定单个 FD	串行读写，资源利用率低
   NIO	线程主动读取就绪 FD	不阻塞，事件触发	批量托管 FD，内核监控状态	批量监听，按需读取
   AIO	内核完成后回调	全程脱离 IO	仅标识资源，内核调度	全异步托管，效率最优


# JUC相关八股文


# 线程 


## 进程和线程的区别？

## Go的协程和Java线程的区别

## 线程有几种创建方式？各自优缺点？

## 线程的五大生命周期状态及流转过程？

## 什么是守护线程？和用户线程区别？应用场景？

## sleep ()、wait ()、yield ()、join () 区别？

## 为什么 wait、notify 要在 synchronized 里执行？

## 什么是线程上下文切换？为什么会耗时间


# 锁机制&关键字


## synchronized 底层原理？

## synchronized 锁升级流程：偏向锁→轻量级锁→重量级锁？

## 偏向锁、轻量级锁、重量级锁各自原理和适用场景？

## synchronized 修饰普通方法、静态方法、代码块，锁的对象分别是谁？

## vloatile关键字的作用是什么？

## 为什么volatile不能用来保证原子性？

## 什么是可重入锁？为什么需要可重入？

## 什么是公平锁、非公平锁？优缺点？

## 悲观锁和乐观锁区别、适用场景？

## CAS 原理是什么？自旋、Unsafe 类作用？

## CAS 三大问题：ABA、循环耗时、只能保证单个变量原子性，怎么解决？

## 什么是自旋锁？优缺点？

## 什么是死锁，死锁的产生条件是什么？

## 什么是偏向锁撤销、重偏向？

## 轻量级锁自旋次数自适应？

## 什么是锁粗化、锁消除？JVM 优化手段？


# AQS


## AQS 是什么？核心设计思想？

## AQS 底层结构：state 状态、双向阻塞队列？

## AQS 独占模式、共享模式区别？

## AQS 排队、唤醒线程的流程？

## AQS为什么要用双向链表

# JUC常用工具类


## ConcurrentHashMap工作流程

## ReentrantLock 和 synchronized 区别？

## ReentrantLock 可重入、可中断、可超时、公平锁怎么实现？

## ConcurrentHashMap JDK7 和 JDK8 底层原理区别？

## ConcurrentHashMap 为什么 JDK8 放弃分段锁？改用 CAS + synchronized？

## ConcurrentHashMap 扩容机制？

## BlockingQueue 阻塞队列核心作用？

## 常见阻塞队列：ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue、DelayQueue 区别和场景？

## DelayQueue 原理、延时任务应用场景？

## CompletableFuture的常用API

# 线程池


## 线程池七大核心参数含义？

## 线程池工作原理 / 执行流程？

## 线程池都有哪些种类

## 四种拒绝策略分别是什么？适用场景？

## JDK 内置四大线程池：Fixed、Cached、Single、Scheduled 各自特点、坑点？

## 为什么阿里禁止用 Executors 创建线程池？

## 核心线程数怎么合理设置？IO 密集型、CPU 密集型公式？

## 线程池空闲线程回收机制？

## 线程池关闭 shutdown () 和 shutdownNow () 区别？

## 线程池任务提交 execute () 和 submit () 区别？

## 线程池异常怎么捕获？


# ThreadLocal


## ThreadLocal 原理？底层 ThreadLocalMap 结构？

## ThreadLocal 为什么会内存泄漏？

## 弱引用在 ThreadLocal 里的作用？

## ThreadLocal有什么缺点？（ThreadLocal是没有办法跨线程传递数值的，下面这两个类就是为了解决这个的。面试能够讲出来很加分）
这块推荐大家再学两个类：InheritableThreadLocal 和 TransmittableThreadLocal。可以看一看我之前写的公众号：
https://mp.weixin.qq.com/s/GUmz-esQzCs63HGzpe_S9g
https://mp.weixin.qq.com/s/3xeTHCKvwqu5jBbzQib9Yg

# 常考手撕题

手写一个CAS自旋锁
两个线程交替打印0-100（一个线程只打印奇数，一个线程只打印偶数）
三个线程有顺序打印ABC
手写LRU

# JVM

# 内存结构
## 介绍一下JVM内存模型

## String s = new String（“abc”）执行过程中分别对应哪些内存区域？

## 堆为什么分新生代和老年代，比例默认多少？

## Eden、From、To 区作用？为什么要有两个 Survivor？

## 方法区 / 元空间 作用？存放什么？

## 永久代和元空间的区别？JDK8 为什么废弃永久代改用元空间？

## 直接内存是什么？会不会 OOM？



# 类加载机制

## 类加载的五步

## 什么是双亲委派机制

## 双亲委派机制的缺点是什么

## 如何打破双亲委派机制

## 如何实现自定义类加载器




# 垃圾回收机制

## 如何判断对象是否存活？可达性分析？引用计数?

## GC root有哪些

## 三大垃圾回收算法以及各自的优缺点

## 为什么新生代要用复制算法，而老年代要用标记整理？

## 新生代中的对象符合哪些条件之后会晋升到老年代

## 什么是 Minor GC、Major GC、Full GC？触发条件？

## 什么是STW

## 常用的垃圾收集器有哪些？

## 什么是三色标记法？



# jvm实战调优



## 常见 OOM 有哪几种？分别什么原因？
    - 堆内存溢出
    - 元空间溢出
    - 栈溢出
    - 直接内存溢出
##   常见 JVM 参数：-Xms、-Xmx、-Xss、-XX:MetaspaceSize 等含义？

##   如何排查线上 OOM？完整排查流程？

##   如何排查频繁 Full GC？

##   什么是内存泄漏？和内存溢出区别？

##   线上 CPU 飙高、线程死锁怎么排查？

##   jps、jstack、jmap、jhat、jstat 常用命令作用？

##   堆 dump 怎么抓取、怎么分析？

# 计算机网络


# 基础认识


## 网络五层模型 / 七层 OSI 模型分别是什么？每层作用

## 从用户点击一个网页，到这个网页响应中间都经历了什么

# http/https


## HTTP 与 HTTPS 区别

## HTTP1.0 / 1.1 / HTTP2 / HTTP3 区别

## HTTP 常见请求方法 GET POST PUT DELETE HEAD

## GET 和 POST 区别（参数、缓存、长度、安全性、编码）

## HTTP 状态码分类 1xx/2xx/3xx/4xx/5xx

## 常用状态码：200、301、302、304、400、401、403、404、500

## 301 永久重定向和 302 临时重定向区别

## HTTPS 加密流程、SSL/TLS 握手过程

## 对称加密、非对称加密、数字证书作用

## HTTPS 为什么安全？中间人攻击如何防御

## DNS 域名解析全过程

# tcp和udp

## TCP 与 UDP 区别、适用场景

## TCP 为什么可靠？可靠传输四大机制

## TCP 三次握手全过程、为什么三次不是两次

## 什么是SYN泛洪攻击

## TCP 四次挥手全过程、为什么四次

## 为什么建立连接三次，断开四次

## TCP 滑动窗口原理、流量控制

## TCP 拥塞控制：慢启动、拥塞避免、快重传、快恢复

## TCP 粘包问题产生原因、解决方案

## TCP 头部重要字段：序号、确认号、标志位

## UDP 特点、无连接、不可靠、面向数据报

## 这块如果还有余力的话，可以学一学什么是QUIC
#  kafka

# 基础知识

## Kafka 是什么？核心定位与核心价值是什么？

## Kafka 核心架构组成有哪些？各组件（Producer、Consumer、Broker等）核心作用？

## Topic、Partition、Replica 三者的核心关系是什么？各自的作用的是什么？

## Kafka 为什么吞吐量极高？核心优化机制有哪些？

## Kafka 适用场景与不适用场景分别是什么？

## Kafka 和 RabbitMQ、RocketMQ 全方位对比区别？（吞吐量、可靠性、适用场景）

## Kafka 中的 Broker、Controller 是什么关系？Controller 的核心作用？

## Kafka 的 Zookeeper 架构与 KRaft 架构区别？为什么新版弃用 Zookeeper？
# 消息可靠性


## Kafka 消息丢失可能发生在哪些环节？每个环节的丢失原因是什么？

## 如何全方位保证 Kafka 消息不丢失？生产者、Broker、消费者各环节如何优化？

## Kafka 为什么会出现消息重复消费？常见场景有哪些？

## 消息重复消费的解决方案是什么？业务层如何实现幂等性？

## Kafka 如何保证消息的顺序性？分区内有序与全局有序的区别？

## 为什么多分区无法保证全局有序？如何实现全局有序（特殊场景）？

## 消息乱序的常见原因是什么？如何避免消息乱序？

## Kafka 消息积压的常见原因是什么？（消费端、生产端、Broker 端）

## 线上消息积压的完整排查步骤是什么？如何定位问题根源？

## 解决消息积压的最优方案有哪些？不同场景如何选择？

## 增加消费者数量能解决积压吗？为什么不能超过分区数量？

## 分区数量的设置原则是什么？过多或过少会有什么问题？

## 如何避免消息积压？日常运维需要注意哪些点？


# Redis八股

//TODO

# MySQL八股

//TODO


















































































































