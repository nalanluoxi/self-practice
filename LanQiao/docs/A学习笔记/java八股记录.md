# java八股

# JavaSE

## 为什么金融类型的数据要使用BigDecimal？

在金融、电商、会计等场景中，绝对不能用 float / double 存储、计算金额，必须用 Java 的 BigDecimal，核心原因只有一个：
float/double 会产生精度丢失，导致钱算错；而 BigDecimal 可以精确表示小数，保证金融计算绝对准确。

1. 先看一个致命问题：float/double 精度丢失
   直接运行这段代码，你就能直观感受到问题：

```java
public class Test {
    public static void main(String[] args) {
        double a = 0.1;
        double b = 0.2;
        System.out.println(a + b);
// 输出结果：0.30000000000000004（不是 0.3！）
    }
}
```

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

```java
BigDecimal a = new BigDecimal("0.1");
BigDecimal b = new BigDecimal("0.2");
System.out.

println(a.add(b));
// 输出：0.3 （完全正确）
```

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

| 类型           | 精度          | 适用场景        | 金融场景是否可用 |
|--------------|-------------|-------------|----------|
| float/double | 不精确，有精度丢失   | 科学计算、图形、游戏  | ❌ 绝对禁止   |
| BigDecimal   | 完全精确，无丢失    | 金融、电商、会计、计费 | ✅ 强制使用   |
| int/long     | 精确（分 / 厘存储） | 简易金额存储      | ✅ 可选（麻烦） |

补充：也有人用 long 存分（比如 1 元存 100），也能避免精度问题，但计算、格式化、展示都很麻烦，BigDecimal 是金融开发的标准最优解。

## bigDecimal的常见坑点有哪些

坑 1：用 new BigDecimal(double) 构造，精度直接丢失
错误写法

```java
BigDecimal a = new BigDecimal(0.1);
// 结果不是 0.1，而是 0.1000000000000001...
```

原因：你传进去的 0.1 本身就是 double 不精确值，BigDecimal 也救不了。
正确写法

```java
// 字符串构造（唯一推荐）
BigDecimal a = new BigDecimal("0.1");
```

坑 2：用 equals() 比较大小，结果完全不准
错误写法

```java
BigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("1.00");

System.out.

println(a.equals(b));
// 输出 false！！！
```

原因：equals() 会同时比较数值 + 精度位数，1.0 和 1.00 精度不同，判定不相等。
正确写法（金融比较必须用）

```java
System.out.println(a.compareTo(b) ==0);
// 输出 true
```

compareTo 规则：
返回 -1 → 小于
返回 0 → 等于
返回 1 → 大于

坑 3：除法 divide 不指定舍入模式，直接抛异常
错误写法

```java
BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("3");
a.

divide(b);
// 抛异常：ArithmeticException（无限循环小数）
```

原因：除不尽时，BigDecimal 不知道怎么取舍，直接报错。
正确写法

```java
// 保留2位小数 + 四舍五入
a.divide(b, 2,RoundingMode.HALF_UP);
```

金融常用舍入模式：
HALF_UP：四舍五入
HALF_EVEN：银行家舍入（美股、银行常用）
UP：向上取整
DOWN：向下取整

坑 4：null 值调用方法，空指针异常

```java
BigDecimal money = null;
money.

add(new BigDecimal("10"));
// 空指针！
```

解决：使用前必须判空，或默认赋值 BigDecimal.ZERO。

坑 5：忽略 scale（小数位数），导致展示异常

```java
new BigDecimal("10").

toString();
// 输出 10
new

BigDecimal("10.00").

toString();
// 输出 10.00
```

如果前端 / 数据库要求统一 2 位小数，必须手动设置：

```java
bigDecimal.setScale(2,RoundingMode.HALF_UP);
```

坑 6：加减乘除不会修改原对象，必须接收返回值
BigDecimal 是不可变对象，所有计算都会生成新对象。
错误写法

```java
BigDecimal a = new BigDecimal("10");
a.

add(new BigDecimal("5"));
// a 还是 10！！！
```

正确写法

```java
a =a.

add(new BigDecimal("5"));
```

坑 7：用 valueOf(0.1) 依然会丢精度
错误写法

```java
BigDecimal.valueOf(0.1);
```

原因：本质还是传了 double。
正确写法

```java
BigDecimal.valueOf("10");
// 或者
new

BigDecimal("0.1");
```

坑 8：与 0 比较错误
错误写法

```java
if(bigDecimal ==BigDecimal.ZERO)
        if(bigDecimal.

equals(BigDecimal.ZERO))
```

正确写法

```java
if(bigDecimal.compareTo(BigDecimal.ZERO) ==0)
```

## 什么是自动装箱和拆箱

自动装箱、自动拆箱（Java 极简版）
一句话：基本类型 ↔ 包装类，编译器自动帮你互相转换，不用手动 new。

1. 对应关系

| 基本类型    | 包装类       |
|---------|-----------|
| byte    | Byte      |
| short   | Short     |
| int     | Integer   |
| long    | Long      |
| float   | Float     |
| double  | Double    |
| char    | Character |
| boolean | Boolean   |

2. 自动装箱
   基本类型 → 包装类，自动封装对象

```java
Integer num = 100;
// 编译器自动变成：Integer num = Integer.valueOf(100);
```

3. 自动拆箱
   包装类 → 基本类型，自动取出数值

```java
int n = num;
// 编译器自动变成：int n = num.intValue();
```

4. 常见场景
   集合只能存对象，自动装箱

```java
List<Integer> list = new ArrayList<>();
list.

add(666); // 装箱
```

运算时自动拆箱

```java
Integer a = 20;
int b = a + 10; // a先拆箱计算
```

5. 高频坑：Integer 缓存 (-128~127)

```java
Integer i1 = 100;
Integer i2 = 100;
System.out.

println(i1 ==i2); // true 缓存复用

Integer i3 = 200;
Integer i4 = 200;
System.out.

println(i3 ==i4); // false 新建对象
```

## Integer的（-128-127）缓存池

Integer 缓存池（-128~127）极简讲解

1. 是什么
   Java 预先缓存创建好 -128 ~ 127 范围内的 Integer 对象，调用Integer.valueOf()时，直接复用缓存对象，不再新建，节省内存、提升效率。
2. 触发时机
   自动装箱本质调用 Integer.valueOf(int)，只有这个方法会走缓存；new Integer() 不走缓存。
3. 代码演示

```java
// 范围内，复用缓存同一对象
Integer a = 100;
Integer b = 100;
System.out.

println(a ==b); // true

// 超出范围，新建不同对象
Integer c = 128;
Integer d = 128;
System.out.

println(c ==d); // false
```

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

| 对比   | 重载 (Overload) | 重写 (Override) |
|------|---------------|---------------|
| 位置   | 同一个类          | 父子类           |
| 方法名  | 必须相同          | 必须相同          |
| 参数列表 | 必须不同          | 必须相同          |
| 返回值  | 可不同           | 协变兼容          |
| 权限   | 宽松无限制         | 权限不能变小        |
| 异常   | 可不同           | 异常范围不能扩大      |
| 多态类型 | 编译时多态         | 运行时多态         |

## Java为什么不支持多继承

菱形问题：多个父类同名方法，子类无法判定执行哪个
避免类层级逻辑混乱、歧义冲突
用接口多实现替代，规避冲突问题

### 菱形问题（极简通俗讲）

假设一个子类同时继承两个父类，两个父类有一模一样的方法，就会出现歧义，这就是菱形问题。

1. 画图理解菱形结构
   plaintext
   父类A 父类B
   \ /
   \ /
   子类C
   形状像菱形，故名菱形问题。
2. 代码模拟冲突场景

```java
// 两个父类都有同一个show方法
class A {
    public void show() {
        System.out.println("A方法");
    }
}

class B {
    public void show() {
        System.out.println("B方法");
    }
}

// 假如Java允许类多继承
class C extends A, B {
// 调用show()，到底执行A还是B的？编译器分不清
}
```

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

   静态块只执行一次；
   实例块、构造每次 new 都执行。

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

```java
class Demo {
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
```

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

```java
// 获取私有name字段
Field nameField = clazz.getDeclaredField("name");
// 暴力访问私有成员
nameField.

setAccessible(true);
// 给对象赋值
nameField.

set(obj, "李四");

// 取值
String val = (String) nameField.get(obj);
```

二、反射调用私有方法
getDeclaredMethod(方法名,参数类型...)：获取私有方法
setAccessible(true)：破除私有权限
invoke(对象实例,方法入参...)：执行方法，返回执行结果

```java
// 获取无参私有say方法
Method sayMethod = clazz.getDeclaredMethod("say");
// 放开访问权限
sayMethod.

setAccessible(true);

// 调用方法
Object result = sayMethod.invoke(obj);
```

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

| 对比   | Error       | Exception     |
|------|-------------|---------------|
| 严重性  | 极严重（JVM 崩了） | 一般（代码问题）      |
| 能否捕获 | 能捕获，但不应该处理  | 必须捕获 / 抛出     |
| 来源   | JVM、系统、硬件   | 代码逻辑、参数、空指针等  |
| 例子   | OOM、栈溢出     | 空指针、数组越界、类型转换 |

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

```java
int test() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```

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
   单独改写 hashCode，无法保证等值对象判定逻辑统一，违背设计规范，哈希容器存取逻辑出错，因此重写 hashCode 务必同步重写
   equals。

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

默认初始容量：16（必须是 $2^n$ 次幂）。

默认负载因子：0.75。

扩容阈值（Threshold）：$\text{Capacity} \times \text{Load Factor}$（默认 $16 \times 0.75 = 12$）。

扩容倍数：2 倍（每次扩容后的容量为原来的 2 倍）。

扩容触发时机当 HashMap 中的元素个数（size）超过当前扩容阈值时，或者在 put 时发现链表长度 $\ge 8$ 但数组长度 $< 64$ 时，触发
resize() 扩容。

扩容执行原理（JDK 8+）创建新数组：创建一个容量为原数组 2 倍的新 Node 数组。
数据迁移（高低位链表）：不需要像 JDK 7 那样重新计算每个元素的 hash 并对新长度取模。由于容量翻倍，元素在新数组中的位置只有两种可能：原位置
或 原位置 + 旧数组长度。

通过 (e.hash & oldCap) == 0 来判断：

结果为 0：留在原位置（低位链表 loHead -> loTail）。
结果不为 0：迁移到原位置 + oldCap（高位链表 hiHead -> hiTail）。

避免死循环：采用尾插法保持元素的相对顺序，彻底解决了 JDK 7 扩容时的死循环问题。

## HashMap 的 put () 方法完整执行流程是什么？

计算 Hash 值：调用 hash(key)，利用 Key 的 hashCode() 进行高 16 位和低 16 位的异或运算（扰动函数），减少哈希冲突。

检查数组是否为空：如果底层的 table 数组为空或长度为 0，先调用 resize() 进行初始化。

计算桶位索引：通过 i = (n - 1) & hash 计算目标桶位。

判断桶位是否碰撞：无碰撞：若当前桶位为 null，直接创建新节点 Node 放入该桶位。

发生碰撞：

Key 相同：若桶中第一个节点的 Key 与待插入的 Key 相等（== 或 equals()），记录该节点。

树节点：若当前节点是 TreeNode，说明已经树化，调用 putTreeVal() 插入红黑树。

链表节点：遍历链表。如果找到相同 Key 则跳出；若遍历到链表尾部仍未找到，则在尾部插入新节点。插入后，如果链表长度 $\ge 8$，调用
treeifyBin() 方法，尝试将链表转为红黑树（若此时数组长度 $< 64$，会优先选择扩容而不是树化）。

覆盖旧值：如果找到了相同 Key 的节点，根据参数决定是否用新值覆盖旧值，并返回旧值。

检查扩容：将修改计数器 modCount 加 1，当前 size 加 1，如果 size > threshold，触发 resize() 扩容。

## HashMap 的 get () 方法完整执行流程是什么？

计算 Hash 值：通过 hash(key) 计算目标 Key 的扰动哈希值。

检查数组有效性：若数组 table 不为空、长度大于 0，且通过 (n - 1) & hash 计算出的桶位节点不为 null，则继续，否则返回 null。

检查桶顶节点：对比桶中第一个节点的 Key。若第一个节点的 hash 值与 Key 完全匹配，直接返回该节点。

遍历后续节点：若第一个节点匹配不上，且存在下一个节点（next != null）：

若当前节点是 TreeNode，调用红黑树的查找方法 getTreeNode(hash, key)，通过二分查找（$O(\log n)$）匹配节点。

若当前节点是普通链表节点，通过 while 循环向后遍历链表，利用 equals() 挨个比对，找到匹配的节点则返回。

未找到：遍历结束仍未匹配，返回 null。

## HashMap 如何解决哈希冲突？

HashMap 采用链地址法（拉链法）结合红黑树来解决哈希冲突：

扰动函数优化：在计算哈希时，通过 (h = key.hashCode()) ^ (h >>> 16)，让高位特征传播到低位，降低初次计算的冲突率。

数组 + 链表：当不同的 Key 计算出相同的桶位索引时，它们会被连接在同一个单向链表中（JDK 8 采用尾插法）。

链表转红黑树：当冲突加剧，单一桶位内的链表长度达到 8 且数组总长度达到 64
时，链表会进化为红黑树，将最坏情况下的查找时间复杂度从 $O(n)$ 降为 $O(\log n)$。

## HashMap 为什么是线程不安全的？（JDK7 头插法死循环、JDK8 数据覆盖）

JDK 7：头插法导致死循环
在 JDK 7 中，多线程并发触发 resize() 扩容时，由于采用头插法迁移链表，会导致新链表中元素的顺序被翻转。

假设有线程 A 和线程 B 同时扩容，链表结构为 A -> B。

线程 A 记录了当前指针并挂起，线程 B 优先完成了扩容，将链表反转成了 B -> A。

当线程 A 被唤醒继续执行时，它仍认为 A 的下一个节点是 B，而实际上在新数组中 B 的下一个节点已经指向了 A。这会导致 A 和 B
互相指向，形成环形链表。

后续调用 get() 方法遍历到此桶位时，就会陷入 while(e != null) 的死循环，导致 CPU 飙升至 100%。

JDK 8：数据覆盖问题
虽然 JDK 8 改用尾插法消除了死循环，但由于其方法没有加锁，依然存在数据覆盖问题：

并发 put 覆盖：假设线程 A 和线程 B 同时执行 put() 且计算出相同的桶位。线程 A 判断该桶位为 null，正准备插入新节点时被挂起；线程
B 此时也判断为 null 并成功写入。随后线程 A 恢复，不再重新检查，直接写入，从而覆盖了线程 B 写入的数据。

并发 size 计数丢失：执行 size++（实际为 ++size）不是原子操作，多线程并发自增会导致更新丢失，使得 size 统计值偏小。

## HashMap 头插法和尾插法的缺点

| 插法  | 引入版本      | 优点                                                  | 缺点                                                            |
|-----|-----------|-----------------------------------------------------|---------------------------------------------------------------|
| 头插法 | JDK 7 及以前 | 插入时只需 $O(1)$，不需要遍历链表（设计初衷基于热点数据理论，认为新插入的数据被访问的概率更高） | 1. 扩容迁移时会颠倒链表顺序。2. 多线程并发扩容时会导致死循环（环形链表）                       |
| 尾插法 | JDK 8 及以后 | 扩容迁移时保持原有的顺序（高低位链表），彻底解决了死循环问题                      | 插入时必须遍历链表到末尾，时间复杂度看似为 $O(n)$（但因为超过 8 个就会转红黑树，所以链表长度很短，性能损耗极小） |

## HashMap 和 Hashtable 的区别？

核心区别有以下几点：

1. 线程安全：Hashtable 所有方法都加了 synchronized，是线程安全的但性能差；HashMap 非线程安全，性能更好，多线程场景应使用
   ConcurrentHashMap 替代 Hashtable。
2. null 支持：HashMap 允许一个 null key 和多个 null value；Hashtable 不允许 null key 或 null value，否则抛
   NullPointerException。
3. 继承关系：HashMap 继承 AbstractMap；Hashtable 继承 Dictionary（已过时的遗留类）。
4. 初始容量与扩容：HashMap 默认初始容量 16，每次扩容为原来的 2 倍；Hashtable 默认 11，每次扩容为 2n+1。
5. hash 计算：HashMap 用扰动函数对 hashCode 再做高位混合，减少碰撞；Hashtable 直接用 key.hashCode()。
6. 迭代器：HashMap 的 Iterator 是 fail-fast 的；Hashtable 的 Enumerator 不是 fail-fast 的。
   总结：Hashtable 是 JDK 1.0 的遗留类，已被淘汰；实际开发中单线程用 HashMap，多线程用 ConcurrentHashMap。

## HashMap 的负载因子为什么是 0.75？

负载因子是空间利用率和时间效率之间的折中值，经过统计学和工程实践共同得出。

1. 如果负载因子太小（比如 0.5）：数组利用率低，频繁扩容，浪费内存。
2. 如果负载因子太大（比如 1.0）：数组放满了才扩容，哈希冲突概率极大，链表变长，查询退化为 O(n)。
3. 0.75 是折中点：在泊松分布下，负载因子 0.75 时，单个桶中链表长度为 8 的概率低于千万分之一，冲突概率极低；同时数组利用率保持在
   75%，空间利用合理。
4. 源码注释也提到：0.75 是数学上时间与空间的最优平衡点，是经过大量测试验证的经验值。
   一句话总结：0.75 是在哈希冲突概率可控前提下最节省内存的选择。

## HashMap 中链表转红黑树的阈值为什么是 8？转回链表的阈值为什么是 6？

转树阈值为 8：

1. 基于泊松分布统计：在负载因子 0.75 时，单个桶中节点数达到 8 的概率仅约 0.00000006，属于极罕见情况。也就是说正常使用
   HashMap 几乎不会触发树化，树化是最后的安全保障。
2. 红黑树节点占用内存是链表节点的两倍，树化代价大，只有链表足够长时才值得转换。
3. 链表在节点数较少时（≤8）遍历代价可接受，转换收益不划算。

转回链表阈值为 6：
设计为 6 而不是 7，是为了避免频繁在树和链表之间来回转换（防止反复 shrink/expand 触发振荡），留了 1 的缓冲区间。
若收缩阈值也设为 8，则删除一个节点就触发退化，插入一个节点就触发树化，会在临界点反复震荡，性能抖动。

## HashMap为什么不直接用红黑树？而是要先链表再做树？

1. 内存开销：红黑树节点需要存储 left、right、parent 指针及颜色标记，每个节点占用约是链表节点的 2 倍。大量小链表直接树化，内存浪费严重。
2. 维护成本高：红黑树插入/删除时需要旋转和变色，操作复杂，在节点数少时这些开销远大于收益。
3. 链表在数据量少时效率已经够用：节点数 ≤8 时，链表遍历最多 8 次，时间复杂度 O(n) 实际上很快，不需要 O(logn) 的红黑树。
4. 统计上绝大多数桶不会超过 8 个节点：在正常哈希分布下，出现长链表的概率极低，直接用红黑树属于为极端小概率情况付出极大常规代价，不划算。
   总结：链表是低成本默认结构，红黑树是高冲突兜底结构，先链表后树是空间与时间的最优平衡策略。

## ArrayList 和 LinkedList 的底层实现和性能对比？

底层实现：

- ArrayList：基于动态数组（Object[]），内存连续，支持随机下标访问。
- LinkedList：基于双向链表，每个节点存 prev、next 指针和数据，内存不连续。

性能对比：
| 操作 | ArrayList | LinkedList |
|------|-----------|------------|
| 随机访问 get(i) | O(1)，数组下标直接定位 | O(n)，从头/尾遍历 |
| 头部插入/删除 | O(n)，需整体移位 | O(1)，修改指针 |
| 尾部插入 | O(1)（未扩容）| O(1) |
| 中间插入/删除 | O(n)，移位开销 | O(n)，需先遍历定位 |
| 内存占用 | 紧凑，仅数组本身 | 每个节点额外存两个指针，内存开销大 |
| 缓存友好性 | 高（连续内存，CPU缓存命中率高） | 低（节点分散，缓存不友好） |

选型建议：

- 需要频繁随机访问、读多写少：用 ArrayList。
- 频繁在头尾增删、不需要随机访问：可考虑 LinkedList，但实际中 ArrayList 因缓存友好往往综合性能更好。
- 实际开发 90% 场景用 ArrayList，LinkedList 使用场景极少。

## ArrayList 的扩容机制是什么？

1. 初始容量：默认初始容量为 10（空参构造实际上是懒加载，第一次 add 时才初始化为 10）。
2. 触发时机：每次 add 前调用 ensureCapacityInternal()，若当前元素数量达到数组长度，触发扩容。
3. 扩容倍数：新容量 = 旧容量 + 旧容量 >> 1 = 旧容量 × 1.5，即每次扩容为原来的 1.5 倍。
4. 扩容过程：调用 Arrays.copyOf() 创建新数组，将原数组数据拷贝进新数组，旧数组等待 GC 回收。
5. 指定容量：若调用 new ArrayList(capacity) 传入初始容量，第一次扩容仍按 1.5 倍计算。
6. 批量操作优化：建议在已知数据量时调用 ensureCapacity(n) 或直接传入初始容量，避免多次扩容、多次拷贝带来的性能损耗。

关键点：扩容操作 Arrays.copyOf() 底层是 System.arraycopy()，是原生方法，速度很快，但大量数据仍有开销，合理预设容量很重要。

## HashSet 如何保证元素不重复？底层实现是什么？

底层实现：HashSet 内部直接持有一个 HashMap，所有 HashSet 的元素存储在 HashMap 的 key 中，value 统一是一个固定的 Object 占位符
PRESENT。

保证不重复的原理：

1. 调用 add(e) 时，实际执行 map.put(e, PRESENT)。
2. HashMap 的 put 逻辑会先计算 key 的 hashCode 定位桶位，再用 equals() 对比已有 key。
3. 如果 hashCode 相同且 equals() 返回 true，认为 key 已存在，拒绝插入（覆盖 value 但 key 不变）。
4. 因此 HashSet 中不会存在两个 equals() 为 true 的元素。

重要注意事项：

- 自定义对象放入 HashSet，必须同时重写 hashCode() 和 equals()，否则无法正确去重。
- hashCode 不同的对象，即使内容一样，也会被判定为不同元素（因为会落入不同桶位）。

## HashMap 和 TreeMap 的区别？

| 对比项      | HashMap       | TreeMap                      |
|----------|---------------|------------------------------|
| 底层结构     | 数组 + 链表 + 红黑树 | 红黑树                          |
| 有序性      | 无序，遍历顺序不确定    | 按 key 自然排序或自定义 Comparator 排序 |
| 查询性能     | O(1) 平均       | O(logn)                      |
| null key | 允许一个          | 不允许（null 无法比较大小）             |
| 线程安全     | 非线程安全         | 非线程安全                        |
| 适用场景     | 高性能无序存取       | 需要按 key 排序遍历的场景              |

使用建议：

- 不需要排序：用 HashMap，性能最优。
- 需要按 key 排序（如排行榜、按字母序遍历）：用 TreeMap。
- 需要排序且线程安全：用 ConcurrentSkipListMap（跳表实现，比加锁 TreeMap 性能好）。

## LinkedHashMap 的有序性是如何实现的？

LinkedHashMap 继承自 HashMap，在其基础上额外维护了一条双向链表，用于记录元素的插入顺序（或访问顺序）。

核心结构：

- 每个 Entry 节点在 HashMap 节点基础上增加了 before 和 after 两个指针，形成贯穿所有节点的双向链表。
- 链表头 head 指向最旧的节点，链表尾 tail 指向最新插入的节点。

两种有序模式：

1. 插入顺序（默认，accessOrder=false）：元素按插入先后顺序遍历，最先插入的最先访问。
2. 访问顺序（accessOrder=true）：每次 get/put 访问某节点后，将其移到链表尾部，实现 LRU（最近最少使用）语义。

LRU 缓存实现：利用 accessOrder=true 的 LinkedHashMap，重写 removeEldestEntry() 方法，当容量超限时自动移除链表头部（最久未访问）的元素，天然实现
LRU 缓存淘汰。

## TreeSet 的排序原理是什么？Comparable 和 Comparator 接口的区别？

TreeSet 底层基于 TreeMap，TreeMap 底层是红黑树（自平衡二叉搜索树）。
排序原理：红黑树的中序遍历（左-根-右）天然输出有序序列，因此 TreeSet 遍历结果总是有序的。
排序依据：元素比较大小依赖 Comparable 接口或 Comparator 接口。

Comparable 和 Comparator 区别：

| 对比项   | Comparable                  | Comparator                    |
|-------|-----------------------------|-------------------------------|
| 所在位置  | 在被比较类内部实现                   | 独立于被比较类的外部比较器                 |
| 方法    | compareTo(Object o)         | compare(Object o1, Object o2) |
| 与类的关系 | 类实现此接口，定义"自然排序"             | 外部传入，不修改原类                    |
| 灵活性   | 低，排序规则固定在类内                 | 高，可随时传入不同比较规则                 |
| 修改原类  | 需要修改类源码                     | 无需修改原类                        |
| 适用场景  | 类有明确唯一排序规则，如 Integer、String | 需要多种排序方式，或无法修改原类              |

使用建议：自定义类有固定自然排序用 Comparable；需要多种排序策略或第三方类排序用 Comparator（Lambda 表达式更简洁）。

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
FD 文件描述符：操作系统内核用整数标识文件、网卡套接字、管道等 IO 资源，内核维护FD 数组，下标对应资源句柄，程序通过 FD 操作对应
IO 对象。
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

| 模型  | 读写触发        | 线程状态     | FD 数组作用        | 磁盘交互特点      |
|-----|-------------|----------|----------------|-------------|
| BIO | 线程主动调用      | 阻塞等待     | 单线程绑定单个 FD     | 串行读写，资源利用率低 |
| NIO | 线程主动读取就绪 FD | 不阻塞，事件触发 | 批量托管 FD，内核监控状态 | 批量监听，按需读取   |
| AIO | 内核完成后回调     | 全程脱离 IO  | 仅标识资源，内核调度     | 全异步托管，效率最优  |

# JUC相关八股文

# 线程

## 进程和线程的区别？

进程：操作系统资源分配的最小单位，每个进程拥有独立的内存空间（代码段、数据段、堆、栈）、文件描述符等资源。进程间相互隔离，一个进程崩溃不影响其他进程。
线程：CPU调度的最小单位，是进程内的执行单元。同一进程内的多个线程共享进程的内存空间（堆、方法区），但每个线程有自己独立的栈和程序计数器。

核心区别：
| 对比 | 进程 | 线程 |
|------|------|------|
| 资源 | 独立内存空间 | 共享进程内存 |
| 创建开销 | 大（需分配独立资源） | 小（共享进程资源） |
| 通信方式 | 进程间通信（IPC：管道、socket、共享内存） | 直接读写共享内存，但需加锁 |
| 崩溃影响 | 不影响其他进程 | 一个线程崩溃可能导致整个进程崩溃 |
| 切换开销 | 大（需切换地址空间） | 小（同一地址空间内切换） |

一句话总结：进程是资源隔离的容器，线程是进程内轻量级的执行单元，线程更轻量但需要处理共享数据的并发安全问题。

## Go的协程和Java线程的区别

| 对比项  | Go 协程（Goroutine）                                  | Java 线程（Thread）            |
|------|---------------------------------------------------|----------------------------|
| 调度方式 | 用户态调度，Go runtime 自己调度                             | 内核态调度，依赖操作系统               |
| 创建开销 | 极小，初始栈约 2-8KB，可动态增长                               | 较大，每个线程默认栈 512KB-1MB       |
| 切换开销 | 用户态切换，无需陷入内核，极快                                   | 需要内核上下文切换，开销较大             |
| 并发数量 | 可轻松创建百万级 goroutine                                | 通常数千个线程就会出现性能问题            |
| 通信方式 | Channel（CSP模型："不要通过共享内存来通信"）                      | 共享内存 + 锁（需手动处理并发安全）        |
| 阻塞处理 | goroutine 阻塞时 Go runtime 自动将 OS 线程分配给其他 goroutine | 线程阻塞时 OS 线程被占用，浪费资源        |
| 编程模型 | 更简洁，协程间天然通过 channel 通信                            | 需要 synchronized/Lock/并发工具类 |

本质区别：Go 协程是 M:N 线程模型（多个 goroutine 映射到少量 OS 线程），由 Go runtime 调度；Java 线程是 1:1 模型（每个 Java
线程对应一个 OS 线程），由操作系统调度。注：Java 21 引入的虚拟线程（Virtual Thread）对标 Go 协程，也实现了 M:N 模型。

## 线程有几种创建方式？各自优缺点？

1. 继承 Thread 类，重写 run() 方法
   优点：简单直接。
   缺点：Java 单继承限制，继承了 Thread 就不能继承其他类；无法获取返回值；不推荐。

2. 实现 Runnable 接口，传入 Thread 构造器
   优点：避免单继承限制，可以多个线程共享同一 Runnable 实例，解耦任务与线程。
   缺点：无法获取返回值，无法抛出受检异常。

3. 实现 Callable 接口 + FutureTask
   优点：可以获取线程执行返回值；可以抛出受检异常；FutureTask.get() 可阻塞等待结果。
   缺点：代码稍繁琐。

4. 线程池（推荐，实际开发首选）
   通过 ThreadPoolExecutor 或 Executors 工厂方法创建线程池，submit()/execute() 提交任务。
   优点：线程复用，避免频繁创建销毁线程；统一管理线程生命周期；支持任务队列、拒绝策略等。
   缺点：需要合理配置参数，配置不当会造成线程池积压或 OOM。

面试总结：实际开发中必须用线程池，直接 new Thread 是反模式，阿里规范明确禁止直接创建线程。

## 线程的五大生命周期状态及流转过程？

Java 线程有 6 种状态（Thread.State 枚举定义）：

1. NEW（新建）：线程对象刚创建，还未调用 start()。
2. RUNNABLE（运行中）：调用 start() 后，包含"就绪"和"运行"两个子状态，在 JVM 层面不区分（等待 CPU 时间片或正在执行都算
   RUNNABLE）。
3. BLOCKED（阻塞）：尝试进入 synchronized 代码块/方法，但锁被其他线程持有，等待获取监视器锁。
4. WAITING（无限等待）：调用 Object.wait()、Thread.join()、LockSupport.park() 后进入，需要其他线程显式唤醒（notify/notifyAll/unpark）。
5. TIMED_WAITING（超时等待）：调用 Thread.sleep(ms)、Object.wait(ms)、Thread.join(ms) 等带超时参数的方法，超时后自动回到
   RUNNABLE。
6. TERMINATED（终止）：run() 方法执行完毕或抛出未捕获异常，线程结束，无法重新启动。

流转流程：
NEW → start() → RUNNABLE → 执行完 → TERMINATED
RUNNABLE → 等锁 → BLOCKED → 获得锁 → RUNNABLE
RUNNABLE → wait()/join() → WAITING → notify()/join结束 → RUNNABLE
RUNNABLE → sleep(ms)/wait(ms) → TIMED_WAITING → 超时/唤醒 → RUNNABLE

## 什么是守护线程？和用户线程区别？应用场景？

守护线程（Daemon Thread）：是为其他线程服务的后台线程，当 JVM 中所有用户线程（非守护线程）都结束时，无论守护线程是否执行完毕，JVM
都会直接退出。
设置方式：thread.setDaemon(true)，必须在 start() 之前调用。

与用户线程的核心区别：
| 对比 | 用户线程 | 守护线程 |
|------|---------|---------|
| JVM 退出 | 全部用户线程结束后 JVM 才退出 | 用户线程结束后 JVM 立即退出，不等守护线程 |
| 优先级 | 正常 | 通常设为较低优先级 |
| 用途 | 主业务逻辑 | 后台辅助服务 |

典型应用场景：

- JVM 内置的垃圾回收器（GC 线程）就是守护线程。
- Java 中的 Finalizer 线程（负责执行 finalize 方法）。
- 心跳监测线程、定时清理缓存线程、日志异步刷盘线程等。

注意：守护线程中不要执行需要可靠完成的业务逻辑（如数据写入、文件关闭），因为它可能被 JVM 强制中断，导致数据不完整。

## sleep ()、wait ()、yield ()、join () 区别？

| 方法        | 所属类    | 释放锁         | 线程状态                    | 唤醒方式                    |
|-----------|--------|-------------|-------------------------|-------------------------|
| sleep(ms) | Thread | 不释放锁        | TIMED_WAITING           | 超时自动唤醒                  |
| wait()    | Object | 释放锁         | WAITING / TIMED_WAITING | notify()/notifyAll()/超时 |
| yield()   | Thread | 不释放锁        | RUNNABLE（让出CPU但仍可被立刻调度） | 调度器决定                   |
| join()    | Thread | 不释放锁（对当前线程） | WAITING                 | 被等待的线程执行完毕              |

详细说明：

- sleep()：当前线程休眠指定时间，期间不释放持有的锁，其他线程无法获得该锁；适合控制执行节奏。
- wait()：必须在 synchronized 块内调用，调用后释放锁并进入等待队列；需要其他线程调用 notify() 唤醒；用于线程间协作通信。
- yield()：暗示调度器当前线程愿意让出 CPU，但调度器可以忽略此提示；让出后该线程仍处于 RUNNABLE 状态，可能立刻再次被调度执行；实际工程中很少使用。
- join()：t.join() 让当前线程等待线程 t 执行完毕再继续；底层通过 wait() 实现；常用于主线程等待子线程结果。

## 为什么 wait、notify 要在 synchronized 里执行？

核心原因：防止竞态条件（Race Condition），保证 wait 和 notify 操作的原子性与可见性。

典型竞态场景（若不加锁）：

1. 线程 A 检查条件（如 !hasData），判断需要 wait，准备调用 wait。
2. 就在 A 调用 wait() 之前，线程 B 执行了 notify()（此时 A 还没 wait，notify 被忽略）。
3. 线程 A 随后调用 wait() 进入等待，但再也没有线程来唤醒它，永远阻塞。

加锁后的保证：

- synchronized 保证了"检查条件"和"执行 wait"是原子的，中间不会被其他线程插入。
- wait() 调用后释放锁，让 notify 方线程能获得锁执行唤醒。
- JVM 强制要求：调用 wait/notify 必须持有对应对象的监视器锁，否则抛 IllegalMonitorStateException。

本质：wait/notify 是基于"条件判断 + 等待"的协作模式，条件判断和等待必须是原子操作，synchronized 提供了这个原子性保证。

## 什么是线程上下文切换？为什么会耗时间

线程上下文切换：CPU 从执行一个线程转而执行另一个线程的过程，需要保存当前线程的运行状态，恢复目标线程的运行状态。

需要保存/恢复的"上下文"包括：

- 程序计数器（PC）：记录当前执行到哪条指令。
- CPU 寄存器（通用寄存器、浮点寄存器等）：保存当前运算的中间状态。
- 栈指针：当前线程的栈帧信息。
- 虚拟内存映射（进程切换时需要）：TLB 等缓存失效。

为什么耗时：

1. 保存/恢复寄存器和栈信息需要执行大量内存读写操作。
2. CPU 高速缓存（L1/L2/L3 Cache）中存储的是切出线程的数据，切换后缓存失效（Cache Miss），新线程需要重新加载数据到缓存，导致大量缓存缺失。
3. 如果是进程切换，还需要切换页表，TLB 全部失效，开销更大。

触发上下文切换的原因：

- 线程主动让出 CPU（sleep/yield/wait）。
- 线程的时间片用完，被调度器强制切换。
- 线程等待 IO 或锁阻塞。

减少上下文切换的方法：使用线程池复用线程；减少锁竞争；使用无锁数据结构（CAS）；减少线程数量，避免过多线程争抢 CPU。

# 锁机制&关键字

## synchronized 底层原理？

synchronized 底层依赖 JVM 的 Monitor（监视器锁/管程）机制实现。

字节码层面：

- 修饰代码块：编译后生成 monitorenter 和 monitorexit 指令，进入代码块时执行 monitorenter，退出（正常或异常）时执行
  monitorexit。
- 修饰方法：编译后在方法的 flags 中添加 ACC_SYNCHRONIZED 标志，JVM 在调用方法时自动执行加锁/解锁。

Monitor 对象：每个 Java 对象都关联一个 Monitor 对象（C++ 实现的 ObjectMonitor）。Monitor 包含：

- _owner：持有锁的线程。
- _EntryList：等待锁的线程队列（BLOCKED 状态）。
- _WaitSet：调用 wait() 后挂起的线程队列（WAITING 状态）。

对象头（Object Header）：锁信息存储在对象头的 Mark Word 中，Mark Word 记录锁状态标志（无锁/偏向锁/轻量级锁/重量级锁）、线程ID等。

执行流程：线程执行 monitorenter，检查 Monitor 的 _owner，为空则获取锁（_owner 设为当前线程）；已被其他线程持有则进入 _
EntryList 阻塞等待；持有锁的线程执行 monitorexit 后从 _EntryList 唤醒一个等待线程。

## synchronized 锁升级流程：偏向锁→轻量级锁→重量级锁？

JVM 对 synchronized 做了锁升级优化，锁状态只能升级不能降级（特殊情况下可以降级）：

1. 无锁状态：对象刚创建，Mark Word 标记为无锁。
2. 偏向锁（Biased Locking）：当第一个线程获取锁时，将线程ID记录在 Mark Word 中，后续该线程再次获取锁只需检查 Mark Word
   中的线程ID，无需 CAS，开销极小。适合只有一个线程反复访问的场景。
3. 轻量级锁（Lightweight Locking）：当有第二个线程尝试获取锁，偏向锁升级为轻量级锁。线程在自己的栈帧中创建锁记录（Lock
   Record），通过 CAS 将对象头的 Mark Word 替换为指向锁记录的指针。若 CAS 失败，说明有竞争，线程自旋重试。适合多线程交替执行（竞争不激烈）的场景。
4. 重量级锁（Heavyweight Locking）：自旋次数超过阈值（或自适应自旋判定无效），轻量级锁升级为重量级锁。线程直接挂起进入 Monitor
   的 _EntryList，等待操作系统唤醒，涉及用户态/内核态切换，开销大。适合竞争激烈的场景。

升级路径：无锁 → 偏向锁 → 轻量级锁 → 重量级锁（不可逆）
注：JDK 15 开始默认禁用偏向锁（-XX:+UseBiasedLocking），JDK 18 正式废弃偏向锁。

## 偏向锁、轻量级锁、重量级锁各自原理和适用场景？

偏向锁：

- 原理：第一个获取锁的线程将线程ID写入对象 Mark Word，设置偏向标志。后续该线程进入同步块只需检查 Mark Word
  的线程ID是否是自己，匹配则直接执行，无需任何 CAS 操作。
- 适用场景：只有一个线程始终反复进入同步块，几乎无竞争。
- 缺点：有其他线程竞争时需要撤销偏向锁，撤销本身有开销（需要等到全局安全点 STW）。

轻量级锁：

- 原理：在当前线程栈帧中分配 Lock Record，将对象 Mark Word 复制到 Lock Record（称为 Displaced Mark Word），然后 CAS 将对象头
  Mark Word 替换为指向 Lock Record 的指针。获取锁失败则自旋重试。解锁时 CAS 将 Displaced Mark Word 写回。
- 适用场景：多线程交替执行同步块，竞争短暂，不需要等待太久。
- 缺点：自旋消耗 CPU，竞争激烈时自旋无效，浪费 CPU。

重量级锁：

- 原理：线程无法通过自旋获取锁，膨胀为重量级锁，关联 ObjectMonitor。未获得锁的线程进入 _EntryList，由 OS
  调度，线程被挂起（BLOCKED），等待持锁线程释放后由 OS 唤醒。
- 适用场景：竞争激烈，线程需要长时间等待，自旋无意义。
- 缺点：涉及用户态/内核态切换，线程挂起唤醒开销大，吞吐量下降。

## synchronized 修饰普通方法、静态方法、代码块，锁的对象分别是谁？

1. 修饰普通实例方法：锁的是当前对象实例（this）。不同实例对象之间互不影响，只有同一个实例的多线程访问才会互斥。
2. 修饰静态方法：锁的是当前类的 Class 对象（如 MyClass.class）。Class 对象全局唯一，即使不同实例对象调用该静态方法也会互斥，粒度更粗。
3. 修饰代码块 synchronized(obj)：锁的是括号中指定的对象。可以是 this、Class 对象、或任意其他对象，灵活控制锁的粒度，减小同步范围，提升性能。

常见面试陷阱：一个类中同时有普通方法和静态方法都加了 synchronized，它们锁的是不同对象（一个是实例，一个是
Class），彼此之间不互斥，可以并发执行。

## vloatile关键字的作用是什么？

volatile 有两个核心作用：

1. 保证可见性：
    - 问题背景：每个线程都有自己的工作内存（CPU缓存），读取变量时先从工作内存读，写变量先写工作内存，再刷回主内存，存在延迟，导致线程间数据不一致。
    - volatile 的作用：被 volatile 修饰的变量，每次写操作立即刷新到主内存，每次读操作直接从主内存读取（不使用工作内存缓存），确保所有线程看到最新值。

2. 禁止指令重排序（内存屏障）：
    - 问题背景：JVM 和 CPU 会对指令进行重排序优化，在单线程下不影响结果，但在多线程场景下可能导致问题（如双重检查锁 DCL
      中的单例对象未完全初始化就被其他线程看见）。
    - volatile 的作用：在 volatile 写操作前后插入 StoreStore/StoreLoad 内存屏障，在读操作前后插入 LoadLoad/LoadStore
      屏障，防止重排序。

典型应用场景：双重检查锁（DCL）单例中的 instance 变量必须用 volatile；状态标志变量（如 volatile boolean running）；一写多读场景。

## 为什么volatile不能用来保证原子性？

原子性是指一个操作不可分割，要么全部执行，要么完全不执行。volatile 只能保证单次读/写操作的可见性，但无法保证复合操作的原子性。

经典例子：volatile int count = 0; count++ 操作。
count++ 实际分三步：

1. 读取 count 的值（read）
2. 对值加 1（increment）
3. 将新值写回 count（write）

假设 count=0，线程 A 和线程 B 同时执行 count++：

1. 线程 A 读取 count=0。
2. 线程 B 读取 count=0（A还没写回）。
3. 线程 A 计算 0+1=1，写回 count=1。
4. 线程 B 计算 0+1=1，写回 count=1（覆盖了 A 的结果）。

最终 count=1，而不是期望的 2。volatile 的可见性保证每次读都读最新值，但"读-改-写"三步之间仍然可以被其他线程插入，无法阻止这种竞态。

解决方案：使用 AtomicInteger（CAS 原子操作）或 synchronized 加锁。

## 什么是可重入锁？为什么需要可重入？

可重入锁（Reentrant Lock）：同一个线程在已经持有某把锁的情况下，可以再次获取该锁，而不会被自己阻塞。每次获取锁时计数器加
1，每次释放锁时计数器减 1，计数器归零时才真正释放锁。

Java 中 synchronized 和 ReentrantLock 都是可重入锁。

为什么需要可重入：
场景：一个同步方法 A 调用同一对象的另一个同步方法 B，两个方法都加了 synchronized（this）。

- 如果不可重入：线程调用 A 获得锁，A 内部调用 B 时发现锁已被"自己"持有，结果把自己阻塞，造成死锁。
- 可重入锁解决了这个问题：线程进入 B 时检测持锁线程是自己，直接进入并将计数器 +1，退出 B 时 -1，退出 A 时再 -1 变为 0，真正释放锁。

实现原理（以 ReentrantLock 为例）：AQS 的 state 字段记录重入次数，getExclusiveOwnerThread() 检查当前线程是否是持锁线程，是则
state++ 直接获取，否则入队等待。

## 什么是公平锁、非公平锁？优缺点？

公平锁：线程按照申请锁的先后顺序排队，先来先得，严格遵守 FIFO 队列。
非公平锁：新来的线程直接尝试抢锁，不管队列中是否有等待线程，抢不到再入队。synchronized 和 ReentrantLock 默认都是非公平锁。

| 对比     | 公平锁                 | 非公平锁             |
|--------|---------------------|------------------|
| 线程饥饿   | 不会，保证每个线程最终获锁       | 可能，队列中线程一直被新线程插队 |
| 吞吐量    | 低，线程切换频繁（唤醒等待线程有开销） | 高，新线程直接获锁避免唤醒开销  |
| 性能     | 较低                  | 较高（优先选择）         |
| CPU利用率 | 低（更多挂起唤醒）           | 高（减少上下文切换）       |
| 适用场景   | 要求严格按顺序处理，防止饥饿      | 大多数通用场景，追求高吞吐    |

为什么非公平锁性能更好：公平锁每次获锁都要检查队列、唤醒等待线程，涉及线程上下文切换；非公平锁允许新线程"插队"
，在锁空闲时直接获取，减少了唤醒等待线程的开销，整体吞吐量更高。

## 悲观锁和乐观锁区别、适用场景？

悲观锁：悲观地认为每次访问共享数据都会发生冲突，因此每次操作前都先加锁，阻止其他线程访问，操作完成后释放锁。
代表：synchronized、ReentrantLock、数据库行锁（SELECT FOR UPDATE）。
适用场景：写多读少、竞争激烈、数据一致性要求高的场景。

乐观锁：乐观地认为大多数情况下不会发生冲突，不加锁，操作数据时不阻塞其他线程，只在提交更新时检查数据是否被修改，若被修改则重试或报错。
代表：CAS（AtomicInteger 等）、数据库版本号机制（version 字段）、时间戳机制。
适用场景：读多写少、竞争不激烈的场景。

| 对比   | 悲观锁              | 乐观锁             |
|------|------------------|-----------------|
| 加锁时机 | 操作前加锁            | 提交时校验           |
| 线程阻塞 | 会阻塞              | 不阻塞             |
| 性能   | 竞争激烈时性能好         | 冲突少时性能好         |
| 冲突处理 | 等待               | 重试或失败           |
| 开销   | 加锁/释放锁开销，线程上下文切换 | 冲突多时频繁重试，CPU 空转 |

## CAS 原理是什么？自旋、Unsafe 类作用？

CAS（Compare And Swap，比较并交换）：是一种无锁原子操作，包含三个操作数：内存地址 V、期望旧值 A、要写入的新值 B。只有当内存地址 V
中的实际值等于期望值 A 时，才将 V 的值更新为 B，否则不更新。整个操作由 CPU 硬件指令（如 x86 的 cmpxchg）保证原子性，不依赖 OS
锁。

自旋：CAS 失败时（说明值已被其他线程修改），不阻塞线程，而是立即重新读取当前值并重试
CAS，这种循环重试的过程叫自旋。自旋避免了线程挂起和唤醒的开销，适合锁竞争短暂的场景。

Unsafe 类的作用：Java 无法直接调用 CPU 的 CAS 指令，需要借助 sun.misc.Unsafe 类。Unsafe 提供了直接操作内存的能力：

- compareAndSwapInt/Long/Object：直接在指定内存偏移量上执行 CAS 操作，最终映射到 CPU 的原子指令。
- objectFieldOffset：获取对象字段在内存中的偏移量，配合 CAS 使用。
- Unsafe 是 JDK 内部类，普通代码无法直接使用，AtomicInteger 等原子类通过反射获取 Unsafe 实例使用它。

## CAS 三大问题：ABA、循环耗时、只能保证单个变量原子性，怎么解决？

1. ABA 问题：
    - 问题：线程 A 读取值为 A，线程 B 将其改为 B，再改回 A，线程 A 的 CAS 检测到值仍为 A 认为未被修改，成功执行，但实际上数据已经被修改过。
    - 解决：使用带版本号的原子类 AtomicStampedReference（每次修改同时更新版本号 stamp），CAS 时同时比较值和版本号，版本号不同则失败。也可用
      AtomicMarkableReference（仅标记是否修改过，不记录次数）。

2. 循环耗时（自旋开销）：
    - 问题：CAS 失败后不断自旋，若竞争激烈，自旋时间过长，大量 CPU 空转，浪费资源，性能反而比加锁更差。
    - 解决：JVM 自适应自旋（根据历史自旋成功率决定自旋时长）；设置最大自旋次数限制；竞争激烈时退化为加锁（LongAdder
      分段思想，减少单个变量竞争）；使用 LongAdder 替代 AtomicLong（高并发计数场景）。

3. 只能保证单个变量的原子性：
    - 问题：CAS 每次只能原子地操作一个变量，无法同时原子地更新多个变量。
    - 解决：使用 AtomicReference 将多个变量封装在一个对象中，对整个对象引用做 CAS；或使用 synchronized/Lock 保证多变量操作的原子性。

## 什么是自旋锁？优缺点？

自旋锁：线程尝试获取锁失败时，不立即阻塞挂起，而是不断循环（自旋）检测锁是否已释放，直到获取成功。本质是让线程忙等待（busy
waiting）而非阻塞。

优点：

- 避免线程挂起和唤醒的开销（无需内核态切换，无线程上下文切换）。
- 锁持有时间极短时，自旋等待比阻塞等待代价更小，延迟更低。

缺点：

- 自旋期间线程占用 CPU，若锁持有时间较长，自旋会大量浪费 CPU 资源。
- 不适合单核 CPU（只有一个 CPU，自旋的线程占用 CPU，持锁线程无法执行释放锁，造成死循环）。
- 长时间自旋可能导致 CPU 缓存一致性流量增加（缓存颠簸 Cache Thrashing）。

适用场景：临界区极短（微秒级），竞争不激烈，多核 CPU 环境。典型实现：CAS 自旋、轻量级锁自旋。

JVM 自适应自旋：JDK 6+ 引入自适应自旋，根据上次自旋成功率动态调整自旋次数：上次成功则多等一会；上次失败则少等甚至直接阻塞。

## 什么是死锁，死锁的产生条件是什么？

死锁：两个或多个线程互相持有对方需要的锁，都在等待对方释放，导致所有线程永远阻塞，无法继续执行的状态。

死锁产生的四个必要条件（四个同时满足才会死锁）：

1. 互斥条件：资源一次只能被一个线程占用，其他线程必须等待。
2. 占有并等待（请求并保持）：线程已持有至少一个资源，并在等待获取其他线程持有的资源，等待期间不释放已持有资源。
3. 不可剥夺（不可抢占）：线程持有的资源不能被强制剥夺，只能由持有线程主动释放。
4. 循环等待：存在线程等待链 T1→T2→T3→...→T1，形成等待闭环。

破坏死锁的方法（破坏任意一个条件即可）：

- 破坏占有并等待：一次性申请所有所需资源（原子申请），申请不到则释放已持有资源。
- 破坏不可剥夺：使用可中断的锁（ReentrantLock.tryLock()），超时后释放已持有锁。
- 破坏循环等待：对所有资源排序，所有线程按固定顺序申请锁，从根本上消除环路。

排查手段：jstack + 线程 dump 分析，jstack 会自动检测并打印 Found one Java-level deadlock 信息。

## 什么是偏向锁撤销、重偏向？

偏向锁撤销（Revocation）：当持有偏向锁的线程不再活跃，或有其他线程竞争该锁时，JVM 需要撤销偏向锁。
撤销过程：需要等到全局安全点（Safe Point，所有线程暂停），检查偏向线程是否仍在执行同步块。若不在则清空偏向标记，恢复为无锁或升级为轻量级锁；若仍在则直接升级为轻量级锁。
撤销的代价：需要 STW（Stop The World），开销较大，因此如果撤销次数过多，JVM 会批量撤销（Bulk Revocation）甚至关闭偏向锁。

重偏向（Rebias）：当某个类的对象发生偏向锁撤销的次数超过阈值（默认 20 次）时，JVM 会对该类的所有对象执行批量重偏向，将偏向锁统一切换到新竞争的线程，避免频繁
STW 撤销。

批量撤销（Bulk Revocation）：当撤销次数超过更大阈值（默认 40 次）时，JVM 认为该类不适合偏向锁，对该类的所有对象直接禁用偏向锁，后续直接升级为轻量级锁。
注：JDK 15 默认关闭偏向锁，JDK 18 完全废弃。

## 轻量级锁自旋次数自适应？

早期（JDK 6 之前）：轻量级锁自旋次数固定为 10 次（-XX:PreBlockSpin 参数控制），超过次数仍未获锁则升级为重量级锁。

自适应自旋（Adaptive Spinning，JDK 6+）：JVM 根据历史自旋情况动态调整自旋次数：

- 如果上一次自旋成功获取到锁：本次允许更长的自旋等待（认为这个锁持有时间短，自旋值得）。
- 如果上一次自旋很快失败：本次减少自旋次数甚至直接跳过自旋（认为竞争激烈，自旋无意义）。
- 长期无法通过自旋获取锁：JVM 可能完全不自旋，直接升级重量级锁。

自适应自旋的意义：不再是一刀切的固定次数，而是根据运行时的实际情况智能决策，在锁竞争短暂时减少线程挂起开销，在竞争激烈时及时放弃自旋避免
CPU 浪费。

## 什么是锁粗化、锁消除？JVM 优化手段？

锁消除（Lock Elimination）：
JIT 编译器通过逃逸分析（Escape Analysis）判断同步代码块中的锁对象是否只在当前线程内使用（不会逃逸到其他线程），如果确定不会被其他线程访问，则直接消除这个锁，无需加锁。
典型例子：在方法内部创建的 StringBuffer 对象，其 append() 方法内部有 synchronized，但该对象只在局部使用，JVM 会消除这些锁。

锁粗化（Lock Coarsening）：
如果 JVM 发现一系列连续的操作在频繁地加锁和解锁同一把锁（如循环体内每次迭代都加锁），会将多次加锁解锁操作合并为一次，扩大锁的作用范围（粗化为一把大锁），减少反复加锁解锁的开销。
典型例子：循环内多次调用 StringBuffer.append()，JVM 将循环外的一次加锁合并，避免每次 append 都加锁解锁。

两者的本质区别：

- 锁消除：完全不需要这个锁，直接删除。
- 锁粗化：锁有存在意义，但加锁/解锁太频繁，合并为一次大锁减少开销。

# AQS

## AQS 是什么？核心设计思想？

AQS（AbstractQueuedSynchronizer，抽象队列同步器）：是 JUC 包中构建锁和同步器的核心框架，是
ReentrantLock、CountDownLatch、Semaphore、ReentrantReadWriteLock 等工具类的底层骨架。

核心设计思想：

1. 用一个 volatile int state 变量表示同步状态（资源是否可用）。
2. 用一个 FIFO 双向链表队列（CLH 变体）管理所有等待获取同步状态的线程。
3. 提供模板方法（Template Method 模式）：AQS 实现通用的线程排队、阻塞、唤醒逻辑，子类只需实现：
    - tryAcquire()/tryRelease()：独占模式下的加锁/释放逻辑。
    - tryAcquireShared()/tryReleaseShared()：共享模式下的加锁/释放逻辑。
4. 子类通过对 state 的 CAS 修改来实现不同语义：ReentrantLock 中 state=0 表示无锁，≥1 表示重入次数；Semaphore 中 state
   表示剩余许可数；CountDownLatch 中 state 表示倒计数。

设计精髓：将线程排队、阻塞、唤醒等通用逻辑内置在 AQS，将"什么条件能获取/释放资源"的语义交给子类定制，实现了高度复用。

## AQS 底层结构：state 状态、双向阻塞队列？

state 状态字段：

- 类型：volatile int，保证多线程可见性。
- 语义由子类定义：ReentrantLock 中为锁重入计数；Semaphore 中为剩余许可数；CountDownLatch 中为倒计数值。
- 所有对 state 的修改通过 CAS 操作保证原子性（getState/setState/compareAndSetState）。

同步等待队列（CLH 变体双向链表）：

- 是一个 FIFO 的双向链表，每个节点（Node）封装一个等待线程。
- 头节点（head）：哨兵节点，代表当前持锁线程（或空节点），不存储实际等待线程。
- 尾节点（tail）：新入队的线程总是追加到队尾。
- 每个 Node 包含：thread（等待线程引用）、waitStatus（节点状态：CANCELLED/SIGNAL/CONDITION/PROPAGATE/0）、prev（前驱节点）、next（后继节点）。
- 核心状态 SIGNAL：表示当前节点的后继节点需要被唤醒，当前节点释放锁时必须唤醒后继。

条件队列（ConditionObject）：每个 Condition 对象维护一个单向链表，存放调用 condition.await() 的线程，被 signal()
唤醒后转移到同步等待队列竞争锁。

## AQS 独占模式、共享模式区别？

| 对比    | 独占模式（Exclusive）       | 共享模式（Shared）                                        |
|-------|-----------------------|-----------------------------------------------------|
| 同时持有者 | 只能一个线程持有              | 多个线程可同时持有                                           |
| 实现方法  | tryAcquire/tryRelease | tryAcquireShared/tryReleaseShared                   |
| 代表类   | ReentrantLock         | Semaphore、CountDownLatch、ReentrantReadWriteLock（读锁） |
| 唤醒策略  | 释放后只唤醒队头下一个等待线程       | 释放后唤醒所有可以获取的等待线程（传播唤醒）                              |

独占模式：state=0 表示无锁，某个线程 CAS 将 state 从 0 改为 1 即获取锁，独占期间其他线程无法获取，只有持锁线程才能修改
state。

共享模式：state 表示剩余资源数，tryAcquireShared 返回值 ≥0 表示获取成功。获取成功后如果发现仍有剩余资源，会传播唤醒（doReleaseShared
方法），继续唤醒下一个等待节点，实现多个线程同时持有。

## AQS 排队、唤醒线程的流程？

获取锁（排队）流程：

1. 线程调用 acquire(1)，先执行 tryAcquire() 尝试获取锁（CAS 修改 state）。
2. tryAcquire() 成功：直接返回，线程持锁执行。
3. tryAcquire() 失败：调用 addWaiter() 将当前线程封装为 Node 节点，CAS 追加到等待队列尾部。
4. 进入 acquireQueued() 自旋循环：检查当前节点的前驱是否为 head（即自己是队列中第一个等待者），是则再次尝试 tryAcquire()
   ；成功则将自己设为 head，返回。
5. 仍然失败：通过 shouldParkAfterFailedAcquire() 将前驱节点的 waitStatus 设为 SIGNAL，然后调用 LockSupport.park()
   将当前线程挂起阻塞。

释放锁（唤醒）流程：

1. 持锁线程调用 release(1)，执行 tryRelease() 修改 state（如 state-1，变为 0 则完全释放）。
2. tryRelease() 成功后，调用 unparkSuccessor() 唤醒等待队列中 head 的后继节点。
3. 后继节点调用 LockSupport.unpark() 被唤醒，从 acquireQueued() 的自旋中醒来，再次尝试 tryAcquire()。
4. 获取成功，将该节点设为新的 head，原 head 被 GC 回收。

## AQS为什么要用双向链表

双向链表相比单向链表多了 prev 指针，AQS 使用双向链表的核心原因：

1. 节点取消（CANCELLED）需要安全跳过前驱：
   线程可以被中断或超时取消等待，被取消的节点状态变为 CANCELLED。在唤醒时需要跳过取消节点找到真正有效的前驱或后继。如果只有单向链表，只能从
   head 开始重新遍历，效率 O(n)；双向链表可以通过 prev 直接从当前节点向前查找，快速定位有效前驱。

2. 唤醒后继节点时需要检查前驱状态：
   acquireQueued() 中，每个等待节点在被挂起前需要确认其前驱节点的 waitStatus 已设置为 SIGNAL（这样前驱释放锁时才会唤醒自己）。通过
   prev 指针可以直接访问前驱节点修改其状态，无需从头遍历。

3. 避免遍历队列时的竞争问题：
   使用双向链表，释放锁时通过 head.next 直接找到第一个有效等待节点并唤醒，如果 head.next 是取消节点，从 tail
   往前找有效节点，而不用重新从头遍历整条链，减少竞争。

简记：双向链表让 AQS 在节点取消、前驱状态维护、从尾部查找有效节点等操作上更高效安全。

# JUC常用工具类

## ConcurrentHashMap工作流程

JDK 8 ConcurrentHashMap 核心工作流程（数组 + 链表 + 红黑树 + CAS + synchronized）：

初始化：懒加载，第一次 put 时用 CAS 初始化 table 数组（默认容量 16），只有一个线程能成功初始化，其他线程自旋等待。

put 流程：

1. 计算 key 的 hash 值（spread 方法，高位扰动）。
2. 若 table 为空，先初始化。
3. 计算桶位 i = (n-1) & hash，若 table[i] 为空，直接 CAS 写入新节点，成功则结束，失败则重试。
4. 若 table[i] 的 hash == MOVED（-1），说明正在扩容，当前线程协助扩容（transfer）。
5. 否则对 table[i]（桶头节点）加 synchronized 锁：
    - 链表：尾插法追加节点，key 相同则更新 value。
    - 红黑树：调用 TreeBin 的 putTreeVal 方法。
6. 链表长度 ≥ 8 且数组长度 ≥ 64 时树化（treeifyBin）。
7. size 累加使用 CounterCell 分段计数（类似 LongAdder），避免单个计数器竞争，最后 check 是否需要扩容。

get 流程：全程无锁，直接计算桶位，检查桶头节点或遍历链表/红黑树，利用 volatile 保证可见性。

扩容：多线程协助扩容（transfer），将数组按段分配给不同线程并行迁移，已迁移的桶置为 ForwardingNode（hash=MOVED），其他线程 put
时遇到 ForwardingNode 会加入协助。

## ReentrantLock 和 synchronized 区别？

| 对比    | ReentrantLock                  | synchronized          |
|-------|--------------------------------|-----------------------|
| 实现层面  | Java API 层（JUC）                | JVM 底层（Monitor）       |
| 可中断   | 支持（lockInterruptibly()）        | 不支持，阻塞期间无法中断          |
| 超时获取  | 支持（tryLock(time, unit)）        | 不支持                   |
| 公平锁   | 支持（构造器传 true）                  | 不支持（非公平）              |
| 条件变量  | 支持多个 Condition（newCondition()） | 只有一个等待队列（wait/notify） |
| 可重入   | 支持                             | 支持                    |
| 性能    | 高并发下更优（可精细控制）                  | JDK 6+ 锁升级后性能接近       |
| 锁释放   | 必须在 finally 中手动 unlock()       | 自动释放（代码块结束或异常）        |
| 使用复杂度 | 较复杂，需手动加锁/解锁                   | 简单，语法内置               |

选用建议：简单同步场景优先用 synchronized（代码更简洁，编译器优化更好）；需要高级特性（中断、超时、多条件、公平锁）时用
ReentrantLock。

## ReentrantLock 可重入、可中断、可超时、公平锁怎么实现？

可重入：内部用 AQS 的 state 记录重入次数，同时用 exclusiveOwnerThread 记录持锁线程。tryAcquire() 时若当前线程就是持锁线程，直接
state++ 重入成功；解锁时 state--，state=0 时才真正释放锁。

可中断：提供 lockInterruptibly() 方法，进入 AQS 等待队列后，如果线程被 interrupt()，会抛出
InterruptedException，线程从等待中退出，不再阻塞等锁。普通 lock() 方法不响应中断（只记录中断标志，获锁后再处理）。

可超时：提供 tryLock(timeout, unit) 方法，底层调用 AQS 的 doAcquireNanos()，线程挂起等待时设置超时时间，超时后不再等待直接返回
false，避免无限等待死锁。

公平锁实现：ReentrantLock 构造器传 true 使用 FairSync。FairSync 的 tryAcquire() 在尝试 CAS 获取锁前，先调用
hasQueuedPredecessors() 检查等待队列中是否有比自己更早排队的线程，有则不抢锁，老老实实入队排队，保证先来先得。非公平锁（NonfairSync）不检查队列，直接
CAS 抢锁。

## ConcurrentHashMap JDK7 和 JDK8 底层原理区别？

JDK 7：分段锁（Segment + HashEntry）

- 结构：Segment 数组 + HashEntry 数组 + 链表。Segment 继承 ReentrantLock，每个 Segment 是一个独立的小 HashMap。
- 默认 16 个 Segment，最多支持 16 个线程并发（每个 Segment 独立加锁互不影响）。
- 初始化：Segment 数组大小固定，无法扩展并发度。
- 问题：Segment 数量固定，并发度有上限；分段导致内存占用偏大；get 某些情况需要加锁。

JDK 8：CAS + synchronized（数组 + 链表 + 红黑树）

- 结构：Node 数组 + 链表/红黑树，取消了 Segment。
- 加锁粒度：只对单个桶的头节点加 synchronized，粒度从段级降到桶级（最小化锁范围）。
- 无锁操作：table 初始化、桶为空时直接 CAS 写入，无需加锁。
- 并发度：等于数组长度，最多支持 n 个线程并发（n 为 table 长度），并发度大幅提升。
- 红黑树优化：链表过长转红黑树，查询 O(logn)。
- size 统计：使用 CounterCell 分段计数（类似 LongAdder），避免单计数器成为瓶颈。

## ConcurrentHashMap 为什么 JDK8 放弃分段锁？改用 CAS + synchronized？

1. 并发度瓶颈：JDK 7 中 Segment 数组大小在初始化时固定，默认只有 16 个 Segment，最大并发度为 16，无法随数据量增长而提升并发度。JDK
   8 锁粒度降到单个桶，并发度等于数组长度（最大可达 n），理论并发度更高。

2. 内存开销：每个 Segment 是一个 ReentrantLock，维护独立的等待队列等状态，16 个 Segment 带来额外内存开销。JDK 8 只在实际发生冲突的桶上加
   synchronized，无冲突时完全无锁（CAS 操作），内存更节省。

3. JVM 对 synchronized 的优化：JDK 6+ 对 synchronized 做了大量优化（锁升级、锁消除、锁粗化），synchronized 的性能已接近甚至超过
   ReentrantLock 的轻量级场景，使用 synchronized 替代 ReentrantLock 更简洁，JVM 可进一步优化。

4. 红黑树引入：JDK 8 引入链表转红黑树机制，依赖精细化的桶级锁而非粗粒度的段锁才能高效配合，按段加锁在树化操作上不够灵活。

## ConcurrentHashMap 扩容机制？

触发条件：元素数量超过 sizeCtl（容量 × 0.75）时触发扩容，新数组容量为原来的 2 倍。

核心特性：多线程协助扩容（transfer 方法）：

1. 触发扩容的线程创建新的 table（容量 × 2），将 sizeCtl 设为负值表示扩容中，低 16 位记录参与扩容的线程数。
2. 将旧 table 按段（stride，每段至少 16 个桶）分配给各个线程并行迁移，每个线程认领自己的段。
3. 线程迁移完一个桶后，将该桶设为 ForwardingNode（hash=MOVED，-1），其他线程 put 时遇到 MOVED 桶会调用 helpTransfer() 加入协助扩容。
4. 数据迁移同 HashMap 类似，使用高低位链表将节点分到原位置或原位置+旧容量的新桶。
5. 所有桶迁移完成后，用新 table 替换旧 table，sizeCtl 更新为新的扩容阈值。

并发安全：迁移时对每个桶头加 synchronized 锁，保证单个桶内的迁移是串行的，但不同桶的迁移是并行的，提升扩容速度。

## BlockingQueue 阻塞队列核心作用？

BlockingQueue 是线程安全的阻塞队列接口，核心作用：

1. 生产者-消费者解耦：生产者往队列放数据，消费者从队列取数据，二者通过队列解耦，无需直接通信。
2. 自动阻塞/唤醒：队列为空时消费者自动阻塞等待；队列已满时生产者自动阻塞等待，无需手动 wait/notify，大幅简化并发编程。
3. 线程池的任务缓冲区：ThreadPoolExecutor 内部用 BlockingQueue 存储等待执行的任务，是线程池的核心组件之一。

核心方法：

- put(e)：插入，队满则阻塞等待。
- take()：取出，队空则阻塞等待。
- offer(e, timeout, unit)：超时插入，超时则返回 false。
- poll(timeout, unit)：超时取出，超时则返回 null。

## 常见阻塞队列：ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue、DelayQueue 区别和场景？

ArrayBlockingQueue：

- 底层：数组，有界（创建时必须指定容量）。
- 锁：一把全局 ReentrantLock，读写共用同一把锁，并发度较低。
- 特点：容量固定，内存预分配，无需扩容；公平/非公平可选。
- 场景：容量明确、需要控制内存使用的生产者-消费者场景。

LinkedBlockingQueue：

- 底层：链表，可选有界（默认 Integer.MAX_VALUE，即近似无界）。
- 锁：读写分别用两把 ReentrantLock（takeLock、putLock），读写并发度更高。
- 特点：吞吐量更高；无界时存在 OOM 风险（阿里规范要求必须指定容量）。
- 场景：高吞吐的任务队列、线程池工作队列。

SynchronousQueue：

- 特点：容量为 0，不存储元素，每次 put 必须等待对应的 take，直接将元素传递给消费线程（握手模式）。
- 场景：newCachedThreadPool 的任务队列，用于任务直传，不缓冲任务。

DelayQueue：

- 底层：PriorityQueue（小顶堆），无界。
- 特点：元素必须实现 Delayed 接口，只有到达延迟时间的元素才能被 take 出来。
- 场景：延时任务调度、订单超时取消、缓存过期清理。

## DelayQueue 原理、延时任务应用场景？

底层原理：

- 内部使用 PriorityQueue 小顶堆存储元素，堆顶是最早到期的元素。
- 元素必须实现 Delayed 接口，重写 getDelay(TimeUnit) 方法返回距离到期的剩余时间，以及 compareTo() 用于堆排序。
- take() 时检查堆顶元素的 getDelay() 是否 ≤ 0，是则取出；否则在条件变量上 await(delay) 等待到期时间，到期后自动唤醒取出。
- 使用 ReentrantLock + Condition 保证线程安全。
- Leader-Follower 模式优化：只有 leader 线程（第一个等待的线程）精确等待到期时间，其他线程无限等待，避免所有线程都设置等待时间造成惊群。

延时任务应用场景：

1. 订单超时自动取消：下单时将订单封装为 DelayedOrder 放入 DelayQueue，超时时间到取出执行取消逻辑。
2. 缓存过期清理：为每个缓存项记录到期时间，放入 DelayQueue，消费线程定期取出过期 key 清理。
3. 重试机制：失败任务按延迟时间放入队列，到期后重新执行。
4. 会话超时管理：用户会话超时检测。

注意：DelayQueue 是无界队列，大量延时任务可能造成内存压力，生产环境建议用 Redis ZSet 或专业调度框架替代。

## CompletableFuture的常用API

CompletableFuture 是 JDK 8 引入的异步编程工具，支持链式调用和组合异步任务。

创建异步任务：

- CompletableFuture.runAsync(Runnable)：无返回值的异步任务。
- CompletableFuture.supplyAsync(Supplier<T>)：有返回值的异步任务。
- 两者都可传入自定义线程池（第二个参数），不传则用 ForkJoinPool.commonPool()。

结果处理（链式调用）：

- thenApply(Function)：上一步有结果，转换后返回新结果（同步，在同一线程）。
- thenApplyAsync(Function)：异步执行转换（在新线程）。
- thenAccept(Consumer)：消费结果，无返回值。
- thenRun(Runnable)：不关心结果，执行下一步操作。

异常处理：

- exceptionally(Function)：类似 try-catch，发生异常时执行，返回默认值。
- handle(BiFunction)：无论成功还是异常都执行（类似 finally），入参为 (result, exception)。

组合多个 Future：

- thenCombine(other, BiFunction)：等两个 Future 都完成，合并两个结果。
- thenCompose(Function)：将上一步结果传入，返回新的 CompletableFuture（类似 flatMap，避免嵌套）。
- allOf(futures...)：等待所有 Future 完成（无返回值）。
- anyOf(futures...)：任一 Future 完成即返回（返回第一个完成的结果）。

获取结果：

- get()：阻塞等待结果（可能抛受检异常）。
- get(timeout, unit)：超时等待。
- join()：阻塞等待，抛非受检异常（更常用）。
- getNow(defaultValue)：立即获取，未完成则返回默认值。

# 线程池

## 线程池七大核心参数含义？

ThreadPoolExecutor 构造器的七个参数：

1. corePoolSize（核心线程数）：线程池始终保持的最小线程数，即使线程空闲也不会回收（除非设置 allowCoreThreadTimeOut=true）。
2. maximumPoolSize（最大线程数）：线程池允许创建的最大线程数。任务队列满了且当前线程数 < maximumPoolSize 时，创建非核心线程。
3. keepAliveTime（空闲线程存活时间）：非核心线程空闲超过此时间后自动销毁，节省资源。
4. unit（时间单位）：keepAliveTime 的时间单位（秒/毫秒等）。
5. workQueue（工作队列）：任务缓冲队列，核心线程全忙时新任务先进队列等待。常用：LinkedBlockingQueue（无界）、ArrayBlockingQueue（有界）、SynchronousQueue（直传）。
6. threadFactory（线程工厂）：创建新线程的工厂，可自定义线程名称、优先级、守护线程等，便于排查问题。
7. handler（拒绝策略）：队列满且线程数达到 maximumPoolSize
   时的处理策略。四种内置策略：AbortPolicy（抛异常，默认）、CallerRunsPolicy（调用者线程执行）、DiscardPolicy（静默丢弃）、DiscardOldestPolicy（丢弃最旧任务）。

## 线程池工作原理 / 执行流程？

提交任务后的执行流程（优先级顺序）：

1. 当前运行线程数 < corePoolSize：创建新核心线程执行任务（即使已有空闲线程，也优先创建新线程直到达到核心线程数）。
2. 当前运行线程数 ≥ corePoolSize：将任务放入 workQueue 等待队列。
3. workQueue 已满 且 当前线程数 < maximumPoolSize：创建非核心线程执行任务。
4. workQueue 已满 且 当前线程数 ≥ maximumPoolSize：触发拒绝策略（handler）处理任务。

线程执行任务后的行为：

- 核心线程执行完任务后不销毁，从队列中继续获取任务，循环执行。
- 非核心线程空闲超过 keepAliveTime 后自动销毁。

图示（面试必背）：
提交任务 → 核心线程满了？没满→创建核心线程执行
满了→ 队列满了？没满→放入队列等待
满了→ 最大线程满了？没满→创建非核心线程
满了→ 执行拒绝策略

## 线程池都有哪些种类

JDK 通过 Executors 工厂类提供四种内置线程池（底层都是 ThreadPoolExecutor）：

1. FixedThreadPool（固定线程池）：核心线程数 = 最大线程数 = n（固定），队列为无界
   LinkedBlockingQueue。线程数固定，适合负载稳定的场景；风险：无界队列可能 OOM。

2. CachedThreadPool（缓存线程池）：核心线程数 = 0，最大线程数 = Integer.MAX_VALUE，队列为 SynchronousQueue，空闲线程存活
   60s。来一个任务就创建一个线程（有空闲则复用），适合大量短时任务；风险：线程数无上限，可能创建大量线程导致 OOM。

3. SingleThreadExecutor（单线程池）：核心线程数 = 最大线程数 = 1，队列为无界
   LinkedBlockingQueue。保证所有任务串行执行，适合需要顺序执行的场景；风险：无界队列可能 OOM。

4. ScheduledThreadPool（定时调度线程池）：核心线程数固定，最大线程数 =
   Integer.MAX_VALUE，支持定时和周期性任务（schedule/scheduleAtFixedRate/scheduleWithFixedDelay）；风险：同
   CachedThreadPool，最大线程数无上限。

注意：阿里开发规范禁止直接使用 Executors 创建线程池，必须使用 ThreadPoolExecutor 手动配置参数，防止资源耗尽。

## 四种拒绝策略分别是什么？适用场景？

1. AbortPolicy（默认）：直接抛出 RejectedExecutionException 异常。
    - 适用场景：必须感知任务被拒绝的场景，让调用方感知并处理异常；要求任务不能丢失的关键业务。

2. CallerRunsPolicy（调用者运行）：被拒绝的任务由提交该任务的线程（调用者线程）直接执行，不转交给线程池。
    - 适用场景：不允许丢弃任务，且可以接受提交线程被阻塞的场景；能自动降低任务提交速率（调用者忙于执行任务时无法提交新任务），起到背压效果。

3. DiscardPolicy（静默丢弃）：直接丢弃任务，不抛异常，不记录日志。
    - 适用场景：允许任务丢失的非关键场景，如埋点统计、日志收集等允许少量丢失的场景。

4. DiscardOldestPolicy（丢弃最旧任务）：丢弃等待队列中排队最久的任务（队头元素），然后尝试重新提交当前任务。
    - 适用场景：新任务优先级高于旧任务，允许丢弃老旧任务；如实时数据处理，旧数据不再有价值的场景。

## JDK 内置四大线程池：Fixed、Cached、Single、Scheduled 各自特点、坑点？

FixedThreadPool：

- 特点：线程数固定，控制最大并发数，多余任务入队等待。
- 坑点：workQueue 是无界 LinkedBlockingQueue（Integer.MAX_VALUE），任务堆积过多会 OOM。

CachedThreadPool：

- 特点：线程数弹性，空闲 60s 自动销毁，适合大量短时任务。
- 坑点：maximumPoolSize = Integer.MAX_VALUE，并发量高时线程数暴涨，可能创建数万个线程导致 OOM 或 CPU 过载。

SingleThreadExecutor：

- 特点：只有一个线程，保证任务串行有序执行，线程异常终止会自动创建新线程。
- 坑点：同 FixedThreadPool，无界队列导致 OOM。单线程意味着任务堆积速度一旦超过处理速度，队列无限膨胀。

ScheduledThreadPool：

- 特点：支持定时/周期任务，底层用 DelayedWorkQueue（小顶堆）存储任务。
- 坑点：maximumPoolSize = Integer.MAX_VALUE，同 CachedThreadPool 有线程数暴涨风险。任务执行时间超过调度间隔时，周期任务不会并发执行，而是等待上次执行完毕再计时。

## 为什么阿里禁止用 Executors 创建线程池？

阿里《Java 开发手册》明确规定：不允许使用 Executors 创建线程池，而应使用 ThreadPoolExecutor 手动配置。原因如下：

1. FixedThreadPool / SingleThreadExecutor：使用无界 LinkedBlockingQueue（容量 Integer.MAX_VALUE =
   21亿）作为工作队列。如果任务提交速度 > 消费速度，队列会无限积压，最终导致 OOM（堆内存耗尽）。

2. CachedThreadPool / ScheduledThreadPool：maximumPoolSize = Integer.MAX_VALUE，允许创建任意数量的线程。高并发场景下线程数量暴增，大量线程占用内存（每个线程默认
   512KB-1MB 栈），同时线程切换开销也会飙升，最终系统资源耗尽崩溃。

核心问题：Executors 使用了不合理的默认参数，屏蔽了线程池的危险配置，让开发者无感知地埋下 OOM 的定时炸弹。

正确做法：显式使用 ThreadPoolExecutor，明确指定 corePoolSize、maximumPoolSize、workQueue（有界队列）和拒绝策略，让参数配置透明可控。

## 核心线程数怎么合理设置？IO 密集型、CPU 密集型公式？

CPU 密集型任务（大量计算，少量 IO，如加密/压缩/图像处理）：

- 公式：corePoolSize = CPU 核数 + 1
- 原因：CPU 密集任务线程一直占用 CPU，线程数超过 CPU 核数反而增加上下文切换开销。+1 是为了防止某线程因缺页中断等偶发情况暂停时，有备用线程可以使用
  CPU，提高利用率。

IO 密集型任务（大量 IO 等待，如数据库查询/HTTP 调用/文件读写）：

- 公式：corePoolSize = CPU 核数 × 2
- 或更精确公式：corePoolSize = CPU 核数 × (1 + 等待时间/计算时间)
- 原因：IO 等待期间线程阻塞不占用 CPU，可以让其他线程利用 CPU，提高 CPU 利用率。IO 等待时间越长，可以创建更多线程填满 CPU 空隙。

实际建议：

1. 上述公式只是经验起点，真正合理的线程数需要通过压测确定。
2. 需要监控线程池各指标（活跃线程数、队列长度、任务拒绝次数）动态调整。
3. 混合型任务（既有 IO 又有计算）可以按 IO 占比多的方向估算，或拆分成不同线程池处理。

## 线程池空闲线程回收机制？

核心原理：线程池工作线程通过 workQueue.poll(keepAliveTime, unit) 从队列获取任务，而不是 take()（无限阻塞）。

回收逻辑：

1. 线程执行完当前任务后，调用 poll(keepAliveTime, unit) 等待新任务。
2. 若等待时间超过 keepAliveTime 仍没有新任务到来，poll() 返回 null。
3. 线程检测到 poll() 返回 null 且当前线程数 > corePoolSize，则退出循环，线程结束，从 workers 集合中移除，等待 GC 回收。
4. 如果当前线程数 ≤ corePoolSize，核心线程使用 take()（无限等待），不会超时退出（除非设置 allowCoreThreadTimeOut=true）。

allowCoreThreadTimeOut：

- 设置为 true 后，核心线程也会在空闲 keepAliveTime 后被回收，线程池在无任务时可以缩减至 0 个线程，节省资源。

## 线程池关闭 shutdown () 和 shutdownNow () 区别？

| 对比       | shutdown()                    | shutdownNow()            |
|----------|-------------------------------|--------------------------|
| 新任务      | 拒绝接受新任务                       | 拒绝接受新任务                  |
| 等待队列中的任务 | 继续执行，等待完成                     | 清空队列，返回未执行任务列表           |
| 正在执行的任务  | 等待执行完毕                        | 尝试中断（interrupt()），不保证成功  |
| 返回值      | void                          | List<Runnable>（未执行的任务列表） |
| 阻塞等待     | 需配合 awaitTermination() 等待完全终止 | 同上                       |

使用建议：

- 正常关闭用 shutdown()，让所有任务执行完再关，优雅关闭。
- 紧急关闭用 shutdownNow()，快速停止，但任务可能丢失（需要业务自行处理返回的未执行任务列表）。
- shutdownNow() 发送的 interrupt 信号只对响应中断的代码有效（如 Thread.sleep()、BlockingQueue.take()），不能强制停止正在执行的普通代码。
- 生产推荐写法：先 shutdown()，然后 awaitTermination(timeout, unit) 等待，超时后再 shutdownNow() 强制中断。

## 线程池任务提交 execute () 和 submit () 区别？

| 对比   | execute(Runnable)                          | submit(Callable/Runnable)                        |
|------|--------------------------------------------|--------------------------------------------------|
| 返回值  | void，无返回值                                  | 返回 Future<T>，可获取任务结果                             |
| 异常处理 | 任务异常直接抛出，会被线程的 UncaughtExceptionHandler 处理 | 异常被封装在 Future 中，调用 get() 时才抛出 ExecutionException |
| 参数类型 | 只接受 Runnable                               | 接受 Callable 或 Runnable                           |
| 所属接口 | Executor 接口                                | ExecutorService 接口                               |

注意：submit() 的异常被"吞掉"封装在 Future 中，若不调用 future.get() 则异常永远不会被感知！这是常见 bug 来源。

实践建议：

- 需要获取返回值或处理受检异常：用 submit() + future.get()。
- 只是提交异步任务、不关心返回值：用 execute()，异常更容易暴露。
- 使用 submit() 时，务必调用 future.get() 或在 exceptionally/handle 中处理异常，避免异常被静默忽略。

## 线程池异常怎么捕获？

线程池中任务抛出异常后，异常不会直接传播到调用线程，需要主动捕获。常见的四种方式：

1. 在任务内部 try-catch（最推荐，最直接）：
   在 Runnable/Callable 的 run() 方法内部用 try-catch 捕获所有异常，统一处理（记录日志、上报监控等）。

2. Future.get() 捕获异常（submit 方式）：
   使用 submit() 提交任务，调用 future.get() 时会将任务中的异常封装为 ExecutionException 抛出，在 catch(ExecutionException
   e) 中通过 e.getCause() 获取原始异常。

3. 自定义 ThreadFactory 设置 UncaughtExceptionHandler：
   通过 ThreadFactory 创建线程时设置 thread.setUncaughtExceptionHandler()，当线程因未捕获异常终止时，JVM 会自动回调该处理器。注意：仅对
   execute() 有效，submit() 的异常被 Future 拦截了不会触发此处理器。

4. 重写 ThreadPoolExecutor.afterExecute() 方法：
   继承 ThreadPoolExecutor，重写 afterExecute(Runnable r, Throwable t)，每个任务执行完后都会调用此方法，t 不为 null
   时说明任务抛了异常。注意：submit() 方式下 t 永远为 null，需要额外从 Future 中取异常。

推荐实践：任务内部 try-catch（方式1）+ UncaughtExceptionHandler 兜底（方式3）组合使用。

# ThreadLocal

## ThreadLocal 原理？底层 ThreadLocalMap 结构？

ThreadLocal 原理：ThreadLocal 为每个线程提供独立的变量副本，线程之间互不干扰，无需加锁。每个线程通过自己私有的
ThreadLocalMap 存储数据，ThreadLocal 对象本身只是一个访问入口。

ThreadLocalMap 结构：

- 每个 Thread 对象内部有一个 threadLocals 字段，类型为 ThreadLocal.ThreadLocalMap。
- ThreadLocalMap 是一个定制的哈希表，内部是 Entry[] 数组（初始容量 16，扩容阈值 2/3）。
- Entry 结构：Entry extends WeakReference<ThreadLocal<?>>，key 是 ThreadLocal 对象的弱引用，value 是存储的线程局部变量值（强引用）。

存取流程：

- set(value)：Thread.currentThread().threadLocals（即当前线程的 ThreadLocalMap）中，以当前 ThreadLocal 对象为 key，存入
  value。
- get()：从当前线程的 ThreadLocalMap 中，以当前 ThreadLocal 为 key 查找对应 value。
- 哈希冲突：ThreadLocalMap 使用开放寻址法（线性探测）解决冲突，而不是链表。

## ThreadLocal 为什么会内存泄漏？

内存泄漏根源：ThreadLocalMap 的 Entry 中，key（ThreadLocal 对象）是弱引用，value 是强引用。

泄漏过程：

1. 业务代码中对 ThreadLocal 对象的强引用被销毁（如方法结束，本地变量 threadLocal = null）。
2. 由于 key 是弱引用，下次 GC 时 ThreadLocal 对象会被回收，key 变为 null。
3. 但 Entry 中的 value 是强引用，Entry 对象本身还在 ThreadLocalMap 数组中（通过 Entry[] 引用着）。
4. 只要线程还活着（如线程池中的复用线程），Thread 对象不销毁，Thread.threadLocals 不销毁，整个 ThreadLocalMap 不销毁，value
   就会一直存在内存中无法被 GC，造成内存泄漏。

核心问题：key 为 null 的 Entry，其 value 无法被访问（key 都没了找不到），也无法被 GC（强引用链存在），就成了"僵尸数据"。

正确使用方式：每次使用完 ThreadLocal，必须调用 remove() 方法：
threadLocal.remove() 会从 ThreadLocalMap 中彻底删除对应 Entry，将 key 和 value 引用都断开，让 GC 可以回收。
在线程池场景中（线程复用）这一点尤为重要，否则旧线程的数据会"污染"下一次任务的执行。

## 弱引用在 ThreadLocal 里的作用？

ThreadLocalMap 的 Entry key 使用弱引用（WeakReference<ThreadLocal<?>>），这是一种主动防御机制，目的是防止 ThreadLocal
对象本身发生内存泄漏（即 ThreadLocal 对象无法被 GC）。

弱引用的作用：

- 如果 key 是强引用：即使业务代码中的 ThreadLocal 引用已置 null，ThreadLocalMap 中的 key 仍然强引用着 ThreadLocal
  对象，ThreadLocal 无法被 GC，造成 ThreadLocal 对象本身的内存泄漏。
- 改为弱引用：当业务代码中不再持有 ThreadLocal 的强引用时，ThreadLocal 对象只剩弱引用，下次 GC 时即可被回收，避免了
  ThreadLocal 对象本身的泄漏。

为什么还会发生内存泄漏（value 泄漏）：
弱引用只解决了 key（ThreadLocal 对象）的泄漏问题，但 value 是强引用，key 被 GC 后 value 还留在 Entry 中无法被访问和回收，这才是真正的内存泄漏点。

ThreadLocalMap 的自救机制：在 get/set/remove 操作时，ThreadLocalMap 会扫描 key == null 的 Entry，将其 value 也置
null（expungeStaleEntry），帮助 GC 回收。但这只在有访问操作时触发，无法完全依赖，仍需手动调用 remove()。

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

JVM 运行时数据区分为线程私有和线程共享两类：

线程私有（每个线程独立一份）：

1. 程序计数器（PC Register）：记录当前线程执行到哪条字节码指令，是 JVM 中唯一不会 OOM 的区域；native 方法时为空（undefined）。
2. 虚拟机栈（JVM Stack）：存储方法调用的栈帧，每个栈帧包含局部变量表、操作数栈、动态链接、方法出口等。方法调用压栈，返回弹栈。栈溢出抛
   StackOverflowError，无限动态扩展失败抛 OOM。
3. 本地方法栈（Native Method Stack）：为 Native 方法（C/C++ 实现的方法）服务，HotSpot 将虚拟机栈和本地方法栈合并为一个。

线程共享（所有线程共用）：

4. 堆（Heap）：存储所有对象实例和数组，是 JVM 内存最大的区域，也是 GC 的主要工作区域。分为新生代（Eden + Survivor）和老年代。
5. 方法区（Method Area）：存储类信息（字节码）、运行时常量池、静态变量、JIT 编译后的代码。JDK 8 之前叫永久代（PermGen），JDK 8+
   改为元空间（Metaspace，使用本地内存）。

额外区域：

6. 运行时常量池：属于方法区的一部分，存储类文件编译期产生的字面量和符号引用。字符串常量池（String Pool）JDK 7+ 移到堆中。
7. 直接内存（Direct Memory）：不属于 JVM 规范定义的运行时数据区，是堆外内存，NIO 的 ByteBuffer 使用直接内存，避免堆内存与操作系统内存之间的数据拷贝，提升
   IO 性能。

## String s = new String（”abc”）执行过程中分别对应哪些内存区域？

执行 String s = new String(“abc”) 涉及以下区域：

1. 方法区（元空间）/运行时常量池：类加载时，字符串字面量 “abc” 在编译期已写入 class 文件的常量池，运行时加载到运行时常量池。JDK
   7+ 字符串常量池移到堆中（字符串对象在堆里，常量池中存的是引用）。

2. 堆（Heap）：
    - 第一步：检查字符串常量池（String Pool）中是否已有 “abc”，没有则在堆的字符串常量池中创建一个 String 对象（内容为 abc）。
    - 第二步：new String(“abc”) 在堆中额外再创建一个新的 String 对象，该对象的 char[] / byte[] 内容与常量池中的对象相同，但是一个全新的对象实例。

3. 虚拟机栈（局部变量表）：变量 s 是一个引用类型，存储在当前方法的栈帧局部变量表中，存的是堆中 new 出来的 String 对象的地址。

总结：

- 涉及区域：字符串常量池（堆）+ 堆（new 出来的 String）+ 虚拟机栈（变量 s 引用）。
- 共创建对象数：若常量池无 “abc”，共创建 2 个对象（常量池 1 个 + new 1 个）；若已有则只创建 1 个（new 出来的那个）。

## 堆为什么分新生代和老年代，比例默认多少？

分代的原因：基于"分代假说"（Generational Hypothesis）——绝大多数对象朝生夕死（短命），只有少量对象会长时间存活。针对不同寿命的对象采用不同的垃圾回收策略，可以大幅提升
GC 效率：

- 短命对象集中在新生代，用复制算法快速回收，频繁但速度快（Minor GC）。
- 长寿对象晋升到老年代，老年代 GC 频率低但每次更彻底（Major/Full GC）。

默认比例：

- 新生代 : 老年代 = 1 : 2（即堆的 1/3 是新生代，2/3 是老年代），由 -XX:NewRatio=2 控制。
- 新生代内部：Eden : From Survivor : To Survivor = 8 : 1 : 1，由 -XX:SurvivorRatio=8 控制。

如果不分代：所有对象混在一起，每次 GC 都要扫描全部对象，效率极低；也无法针对短命对象做快速回收优化。

## Eden、From、To 区作用？为什么要有两个 Survivor？

各区作用：

- Eden（伊甸园）：所有新对象优先在 Eden 中分配（除大对象直接进老年代外）。Eden 满了触发 Minor GC，存活对象复制到 Survivor。
- From Survivor（S0）：上一次 Minor GC 后存活对象所在的区域。
- To Survivor（S1）：本次 Minor GC 时，存活对象从 Eden 和 From 复制到 To，然后 From 和 To 互换角色。

为什么需要两个 Survivor？
如果只有一个 Survivor：

- Eden GC 后存活对象复制到 Survivor，Survivor 内部有存活对象和空闲空间混杂，内存碎片化严重。
- 再次 GC 时，无法用纯复制算法（需要整理碎片），效率降低。

两个 Survivor 的精妙之处：

- 保证 To Survivor 始终是完全空的，每次 Minor GC 都将存活对象从 Eden + From 复制到空的 To，相当于对 To 做了内存整理，To
  中没有碎片。
- GC 完成后，From 和 To 互换（原 To 变成新 From，原 From 全部清空变成新 To），始终保持一个 Survivor 为空。
- 代价：有一个 Survivor 始终是空的，相当于浪费了 1/10 的新生代空间（默认 8:1:1 比例下，To 占 1/10）。

## 方法区 / 元空间 作用？存放什么？

方法区是 JVM 规范定义的一块内存区域（概念），用于存储类的元数据信息，所有线程共享。

存放的内容：

1. 类信息：类名、父类名、接口列表、访问修饰符等类的结构描述。
2. 字段信息：类中定义的成员变量（字段名、类型、修饰符）。
3. 方法信息：方法名、返回类型、参数列表、方法字节码（方法体）。
4. 运行时常量池：编译期生成的字面量（字符串、数字常量）和符号引用，运行时解析为直接引用。
5. 静态变量（类变量）：static 修饰的变量存储在方法区（JDK 7+ 静态变量移到堆中）。
6. JIT 编译后的代码：即时编译器编译生成的本地机器码缓存。

实现变化：

- JDK 7 及之前：方法区实现为永久代（PermGen），存在于 JVM 堆内，受 -XX:MaxPermSize 限制。
- JDK 8+：废弃永久代，改为元空间（Metaspace），使用本地内存（操作系统内存），不受 JVM 堆大小限制，受 -XX:MaxMetaspaceSize
  控制（不设则默认无上限，受操作系统内存限制）。

## 永久代和元空间的区别？JDK8 为什么废弃永久代改用元空间？

| 对比     | 永久代（PermGen）                              | 元空间（Metaspace）                        |
|--------|-------------------------------------------|---------------------------------------|
| 所在内存   | JVM 堆内（受 -Xmx 限制）                         | 本地内存（操作系统直接管理）                        |
| 大小限制   | 受 -XX:MaxPermSize 固定上限限制                  | 默认不限（受物理内存限制），可设 -XX:MaxMetaspaceSize |
| GC 方式  | Full GC 时才回收                              | 类卸载时直接释放本地内存                          |
| OOM 类型 | java.lang.OutOfMemoryError: PermGen space | java.lang.OutOfMemoryError: Metaspace |
| 字符串常量池 | 存在永久代中（JDK 6）                             | JDK 7 已移到堆，元空间不存储                     |

JDK 8 废弃永久代的原因：

1. 永久代大小难以估算：程序运行期间动态加载的类数量不确定，MaxPermSize 设小了频繁 OOM（PermGen space 是开发常见错误），设大了浪费内存。
2. Full GC 触发问题：永久代满了会触发 Full GC（Stop The World），影响应用响应时间，类加载频繁的应用（如动态代理、CGLIB）问题尤为突出。
3. 与 Oracle HotSpot 合并 JRockit 的需要：JRockit 中没有永久代的概念，统一为元空间方便两个 JVM 的合并工作。
4. 元空间扩展性更好：使用本地内存，理论上只要操作系统有内存就不会 OOM，更稳定灵活。

## 直接内存是什么？会不会 OOM？

直接内存（Direct Memory）：不属于 JVM 运行时数据区，而是通过 Java NIO 的 ByteBuffer.allocateDirect() 或
Unsafe.allocateMemory() 直接在操作系统的堆外内存（本地内存）中分配的内存区域。

特点：

- 不受 JVM 堆（-Xmx）大小限制，直接使用操作系统内存。
- 可以通过 -XX:MaxDirectMemorySize 设置最大限制（不设则默认与 -Xmx 相同）。
- GC 对其管理有限：直接内存通过 Cleaner（幻象引用机制）在对应的 DirectByteBuffer 对象被 GC 回收时触发本地内存释放，但这依赖
  GC 对堆对象的回收，可能存在延迟。

为什么用直接内存：NIO 做 IO 操作时，数据从磁盘/网络 → 内核缓冲区 → JVM 堆需要两次拷贝（DMA + CPU拷贝）。使用直接内存后，JVM
可以直接操作内核缓冲区，减少一次 CPU 数据拷贝，IO 性能更高。

会不会 OOM：会。当直接内存使用量超过 -XX:MaxDirectMemorySize 限制时，会抛出 OutOfMemoryError: Direct buffer
memory。即使没有设置限制，操作系统物理内存耗尽时同样 OOM。

注意：频繁创建 DirectByteBuffer 但 GC 不及时（堆内存充足，GC 不频繁），可能导致直接内存无法及时释放，造成直接内存 OOM，而此时
JVM 堆可能还很充裕。

# 类加载机制

## 类加载的五步

类的完整生命周期：加载 → 验证 → 准备 → 解析 → 初始化（→ 使用 → 卸载），其中验证+准备+解析合称"链接"阶段，共五大步骤：

1. 加载（Loading）：通过类的全限定名获取对应的二进制字节流（从 .class 文件、jar 包、网络等）；将字节流转为方法区中的运行时数据结构；在堆中生成对应的
   java.lang.Class 对象作为访问入口。

2. 验证（Verification）：确保 class 字节流格式正确、符合 JVM 规范，防止恶意代码危害 JVM。包括：文件格式验证、元数据验证、字节码验证、符号引用验证。

3. 准备（Preparation）：为类的静态变量（类变量）分配内存并赋初始零值（int=0，boolean=false，引用=null
   等），注意：这里不是赋程序中设置的初始值，而是类型默认值。特殊：static final 常量在准备阶段直接赋真实值（因为在编译期已确定）。

4. 解析（Resolution）：将方法区运行时常量池中的符号引用替换为直接引用（内存地址）。符号引用是 class
   文件中以文字形式描述的目标（如方法名/类名），解析后变为 JVM 可直接使用的内存指针。

5. 初始化（Initialization）：执行类的静态初始化代码——执行静态代码块和静态变量赋值（按代码顺序合并为 <clinit>
   方法执行）。这是类加载的最后一步，也是开发者代码第一次被执行的阶段。

## 什么是双亲委派机制

双亲委派机制（Parents Delegation
Model）：类加载器在收到加载类的请求时，不直接自己加载，而是先委托给父类加载器去尝试加载，只有当父类加载器反馈无法完成加载（在自己的搜索范围内找不到该类）时，子类加载器才自己去加载。

三层类加载器：

1. Bootstrap ClassLoader（启动类加载器）：最顶层，由 C++ 实现，加载 JDK 核心类库（JAVA_HOME/lib/rt.jar 等，如 java.lang.
   *、java.util.*）。
2. Extension ClassLoader（扩展类加载器，JDK 9+ 改名 Platform ClassLoader）：加载 JAVA_HOME/lib/ext/ 下的扩展类库。
3. Application ClassLoader（应用类加载器）：加载 classpath 下的用户类（日常开发的业务代码）。

委派流程：AppClassLoader → ExtClassLoader → BootstrapClassLoader。每级先检查是否已加载，没有则委托父级，父级加载失败再由自己加载。

设计意义（安全性+一致性）：

1. 防止核心类被替换（安全）：用户自定义了一个 java.lang.String 类，通过委派最终由 BootstrapClassLoader 加载，永远是 JDK 中的
   String，自定义的无法加载，防止恶意代码替换核心类。
2. 避免重复加载（唯一性）：同一个类只会被加载一次，父类加载器已加载则直接返回，不会重复加载。

## 双亲委派机制的缺点是什么

1. 无法实现类隔离（主要缺点）：父类加载器加载的类对所有子加载器可见，但子加载器加载的类父加载器不可见，也不能互相加载。在同一个
   JVM 进程中运行多个应用（如 Tomcat 下的多个 webapp）时，不同应用可能依赖同一个库的不同版本，双亲委派无法实现同名类不同版本的共存和隔离。

2. 基础类无法回调应用代码（SPI 问题）：Java 的核心类（如 JDBC、JNDI）由 BootstrapClassLoader 加载，但这些接口的实现类（如 MySQL
   Driver）在 classpath 由 AppClassLoader 加载。BootstrapClassLoader 无法委托 AppClassLoader
   加载实现类（违反委派方向），必须通过线程上下文类加载器（Thread Context ClassLoader）绕过这个限制。

3. 无法热部署/热替换：双亲委派要求类一旦加载就缓存，同一个 ClassLoader 不能重复加载同名类，无法实现运行时类的动态替换（热部署）。OSGi、Tomcat
   等框架为支持热部署必须打破双亲委派。

## 如何打破双亲委派机制

打破双亲委派需要重写 ClassLoader 的 loadClass() 方法（双亲委派逻辑就在 loadClass() 中），改变委派顺序。

三种典型场景：

1. 自定义 ClassLoader 重写 loadClass()：
   直接重写 loadClass()，跳过委托父类的逻辑，自己优先加载指定包路径下的类。Tomcat 的 WebAppClassLoader 就是这样做的，让每个
   webapp 优先用自己的类加载器加载，实现多应用类隔离。

2. 线程上下文类加载器（Thread Context ClassLoader）解决 SPI 问题：
   Java JDBC、JNDI 等 SPI 机制中，父加载器（BootstrapClassLoader）通过 Thread.currentThread().getContextClassLoader()
   获取当前线程的上下文类加载器（默认是 AppClassLoader），再委托它加载实现类，逆向使用了子类加载器，实质上打破了委派的方向。

3. OSGi 框架：
   OSGi 实现了网状类加载器结构，不同 Bundle 有各自的类加载器，依赖关系按导入/导出包的声明动态解析，完全突破了双亲委派的树形结构，支持热部署和版本并存。

## 如何实现自定义类加载器

自定义类加载器的步骤：

1. 继承 ClassLoader 类（或 URLClassLoader）。
2. 重写 findClass(String name) 方法（推荐方式，不破坏双亲委派）：
    - 根据类的全限定名找到对应的 .class 文件（从文件系统/网络/数据库等任意来源）。
    - 读取 class 文件为 byte[] 字节数组。
    - 调用 defineClass(name, bytes, 0, bytes.length) 将字节数组转为 Class 对象并返回。
3. 如果需要打破双亲委派，则重写 loadClass() 方法，改变委派逻辑（但通常不建议）。

典型应用场景：

- 从加密的 .class 文件加载类（解密 + 加载，防止反编译）。
- 从网络、数据库或其他非标准位置加载类。
- 热部署：每次修改代码后创建新的类加载器实例加载新版本的类，旧类加载器及其加载的类随之被 GC。
- 隔离加载：同一 JVM 中加载同名类的不同版本（如 Tomcat 多 webapp 隔离）。

# 垃圾回收机制

## 如何判断对象是否存活？可达性分析？引用计数?

引用计数法：给每个对象维护一个引用计数器，有引用则 +1，引用失效则 -1，计数为 0 则可回收。
优点：实现简单，判断实时。
缺点：无法解决循环引用问题（A 引用 B，B 引用 A，互相引用计数永远不为 0，无法回收，内存泄漏）。Java 没有使用引用计数法（Python、PHP
使用）。

可达性分析法（Java 使用）：以一组 GC Roots 对象为起点，从这些根节点开始向下搜索，能被搜索到的对象（引用链可达）是存活的，搜索不到的（引用链不可达）标记为可回收。
优点：解决了循环引用问题，循环引用的对象如果与 GC Roots 不连通，同样会被回收。

四种引用类型（强度递减）：

- 强引用（Strong）：普通的 Object obj = new Object()，只要强引用存在，对象永远不被 GC。
- 软引用（Soft）：SoftReference，内存充足时不回收，内存不足时 GC 时回收，用于缓存。
- 弱引用（Weak）：WeakReference，不论内存是否充足，GC 时一定回收，ThreadLocalMap 的 key 用弱引用。
- 幻象引用（Phantom/虚引用）：PhantomReference，无法通过虚引用获取对象，仅用于对象回收时收到通知（Cleaner 机制）。

## GC root有哪些

GC Roots 是可达性分析的起始节点，必须是确定"存活"的对象。Java 中常见的 GC Roots 包括：

1. 虚拟机栈（栈帧的局部变量表）中引用的对象：当前所有正在执行的方法的局部变量和参数中引用的对象（如 Object obj = new
   Object() 中的 obj）。

2. 方法区中类静态属性引用的对象：static 修饰的引用类型变量指向的对象（类级别的全局引用）。

3. 方法区中常量引用的对象：如字符串常量池中的引用对象（String 字面量）。

4. 本地方法栈中 JNI（Native 方法）引用的对象：C/C++ 代码中通过 JNI 使用的 Java 对象。

5. JVM 内部引用：如基本数据类型对应的 Class 对象、常驻异常对象（NullPointerException、OutOfMemoryError 等），以及系统类加载器。

6. 所有被 synchronized 持有的对象（监视器对象）。

7. 反映 JVM 内部情况的 JMXBean、JVMTI 中注册的回调、本地代码缓存等。

## 三大垃圾回收算法以及各自的优缺点

1. 标记-清除（Mark-Sweep）：
    - 流程：先标记所有可达对象，再扫描堆，清除未标记的垃圾对象。
    - 优点：实现简单，不需要移动对象。
    - 缺点：产生大量不连续的内存碎片，分配大对象时可能找不到足够连续内存触发 GC；需要维护空闲列表，分配效率低。
    - 适用：CMS 收集器的并发清除阶段。

2. 复制算法（Copying）：
    - 流程：将内存分为两块，只用一块；GC 时将存活对象复制到另一块，然后清空旧块。
    - 优点：无内存碎片，分配速度极快（只需移动 top 指针，类似栈）；复制后内存连续，缓存友好。
    - 缺点：内存利用率只有 50%；复制开销与存活对象数量成正比，存活对象多时效率低。
    - 适用：新生代（存活率低，每次 GC 只有少量对象存活，复制代价小）。

3. 标记-整理（Mark-Compact）：
    - 流程：先标记所有可达对象，再将存活对象移向内存一端，最后清理边界外的所有内存。
    - 优点：无内存碎片，内存利用率高（100%）。
    - 缺点：需要移动对象，移动时必须更新所有对该对象的引用（指针重定向），STW 时间较长；移动操作比复制更重。
    - 适用：老年代（对象存活率高，复制代价大，用标记整理更合适）。

## 为什么新生代要用复制算法，而老年代要用标记整理？

新生代用复制算法的原因：

1. 新生代存活率极低（90%+ 的对象在第一次 GC 就会死亡），每次 GC 只有少量对象需要复制，复制代价极小。
2. 大量死亡对象直接整块清除，速度极快，无碎片。
3. 分配效率高，Eden 区的对象分配只需移动指针，O(1) 操作。
4. Eden:S0:S1 = 8:1:1 的设计下，实际内存浪费只有 10%（To Survivor 始终为空），利用率 90%，可接受。

老年代用标记整理（或标记清除+CMS）的原因：

1. 老年代对象都是经历多次 GC 仍存活的长寿对象，存活率极高（大部分对象都活下来），复制存活对象代价极大，不适合复制算法。
2. 老年代内存大，如果用复制算法需要另一块同等大小的内存区域，内存浪费无法接受。
3. 标记整理虽然移动对象有开销，但消除了碎片，后续分配大对象不需要触发 GC，综合更优。

## 新生代中的对象符合哪些条件之后会晋升到老年代

以下几种情况对象会从新生代晋升到老年代：

1. 年龄阈值（最常见）：对象每经历一次 Minor GC 存活，年龄（Age）+1，默认年龄达到 15（-XX:MaxTenuringThreshold=15，CMS 默认
   6）时晋升老年代。HotSpot 在对象头的 Mark Word 中用 4 位记录年龄，最大值为 15。

2. 动态年龄判断：Minor GC 时，如果 Survivor 区中相同年龄的所有对象大小总和 >= Survivor 区容量的 50%，则年龄 >=
   该年龄的对象直接晋升，不需要等到 MaxTenuringThreshold。

3. 大对象直接进老年代：超过 -XX:PretenureSizeThreshold（默认 0，即不设置）大小的对象，直接在老年代分配，跳过新生代，避免大对象在
   Eden 和 Survivor 间来回复制带来的性能损耗。

4. Survivor 空间不足：Minor GC 后存活对象太多，To Survivor 装不下，剩余对象直接晋升老年代（担保晋升）。

5. 空间分配担保：Minor GC 之前 JVM 检查老年代最大连续可用空间是否 >= 新生代所有对象总大小（或历次晋升平均大小），若不满足条件则直接触发
   Full GC 而非 Minor GC。

## 什么是 Minor GC、Major GC、Full GC？触发条件？

Minor GC（小 GC / Young GC）：只回收新生代（Eden + Survivor）。
触发条件：Eden 区满，无法分配新对象时触发。
特点：频繁、速度快（新生代小，复制算法高效）；会 STW 但时间极短（通常毫秒级）。

Major GC（大 GC / Old GC）：只回收老年代。
触发条件：老年代空间不足时单独触发（CMS 收集器场景）。
特点：速度比 Minor GC 慢 10 倍以上（老年代大，对象多）；STW 时间较长。
注意：Major GC 和 Full GC 概念经常混用，但严格来说不完全相同。

Full GC：回收整个堆（新生代 + 老年代）以及元空间。
触发条件：

1. 老年代空间不足（常见）。
2. 元空间/方法区空间不足（加载大量类）。
3. System.gc()（建议 JVM 执行 Full GC，不保证立即执行）。
4. Minor GC 晋升老年代时空间分配担保失败。
5. CMS GC 出现 Concurrent Mode Failure（并发 GC 时老年代提前满）。
   特点：STW 时间最长，对应用影响最大，生产中频繁 Full GC 是严重性能问题，需重点排查。

## 什么是STW

STW（Stop The World）：JVM 进行垃圾回收时，必须暂停所有用户线程（业务线程），只让 GC 线程运行，整个应用程序在此期间完全停止响应的状态，称为
STW。

为什么必须 STW：
可达性分析需要枚举 GC Roots 并追踪所有引用链，如果用户线程和 GC 线程并发运行，引用关系随时在变化（新对象创建、引用修改），GC
无法得到一个一致的内存快照，可能把活对象误标为垃圾（漏标），造成数据损坏。因此必须在安全点（Safe
Point）暂停所有线程，确保对象引用关系不再变化，才能安全地进行可达性分析。

STW 的影响：应用完全停顿，所有请求无响应，出现明显的延迟抖动（Latency Spike）。生产环境中 Full GC 的 STW 可能长达数秒，严重影响服务可用性。

各收集器对 STW 的优化方向：

- Serial/Parallel：全程 STW，简单粗暴，吞吐量优先。
- CMS：只在初始标记和重新标记阶段 STW，并发标记和清除阶段与用户线程并发，大幅缩短停顿时间。
- G1：每次回收价值最高的 Region，将 GC 停顿控制在可预期的时间内（-XX:MaxGCPauseMillis）。
- ZGC / Shenandoah：几乎全程并发，STW 仅 1-2ms 以内，延迟极低。

## 常用的垃圾收集器有哪些？

新生代收集器：

1. Serial：单线程，GC 时 STW，Client 模式下的默认收集器。简单高效（单 CPU 场景），不适合服务端。
2. ParNew：Serial 的多线程版本，多个 GC 线程并行，STW；常与 CMS 配合使用（新生代 ParNew + 老年代 CMS 是经典搭配）。
3. Parallel Scavenge：多线程并行，关注吞吐量（吞吐量 = 运行用户代码时间 / 总时间），适合后台批处理作业，-XX:GCTimeRatio
   控制吞吐量目标。

老年代收集器：

4. Serial Old：Serial 的老年代版本，单线程，标记整理，作为 CMS 失败时的备用。
5. Parallel Old：Parallel Scavenge 的老年代版本，多线程并行，标记整理，与 Parallel Scavenge 配合实现吞吐量优先。
6. CMS（Concurrent Mark Sweep）：以最短停顿时间为目标，四个阶段（初始标记 STW → 并发标记 → 重新标记 STW → 并发清除），STW
   时间很短。缺点：并发清除期间产生浮动垃圾，标记清除产生内存碎片，CPU 资源与用户线程共享。

整堆收集器（JDK 9+ 默认）：

7. G1（Garbage First）：将堆划分为多个等大的 Region（1-32MB），优先回收垃圾最多的 Region；可预测停顿时间（-XX:
   MaxGCPauseMillis）；适合大堆（6GB+）低延迟场景；兼顾吞吐量和延迟。
8. ZGC（JDK 15+ 生产可用）：几乎全程并发，停顿时间 < 1ms，适合超大堆低延迟场景，是未来主流。
9. Shenandoah：类似 ZGC，停顿时间极短，OpenJDK 支持。

## 什么是三色标记法？

三色标记法是 CMS、G1、ZGC 等并发垃圾收集器实现并发可达性分析的核心算法，用三种颜色标记对象的扫描状态：

- 白色：尚未被 GC 访问的对象。GC 开始时所有对象都是白色；GC 结束后仍是白色的对象即为垃圾。
- 灰色：已被 GC 访问，但其引用的子对象还未全部扫描完。是"工作队列"的状态。
- 黑色：已被 GC 访问，且其引用的所有子对象也已扫描完毕。黑色对象被确认存活。

扫描流程：

1. 初始阶段（STW）：将所有 GC Roots 直接引用的对象标记为灰色，加入工作队列。
2. 并发标记阶段：从灰色对象出发，将其引用的白色子对象变为灰色，自身变为黑色，反复直到灰色队列为空。
3. 结束时：白色 = 垃圾，黑色 = 存活。

并发标记的问题——漏标（错误回收存活对象）：
并发阶段用户线程修改了引用关系，可能导致：黑色对象 A 新增了对白色对象 C 的引用，同时灰色对象 B 断开了对 C 的引用。结果 C
从未被扫描但也不再被 B 引用，C 应该存活（A 引用它），但 C 仍是白色，被错误地当作垃圾回收！

解决方案：

- 增量更新（CMS 使用）：记录并发阶段黑色对象新增的引用，重新标记阶段重新扫描。
- 原始快照（SATB，G1/ZGC 使用）：记录并发阶段删除的灰色→白色引用，重新标记阶段重新扫描，保证原始快照中的引用都被扫描到。

# jvm实战调优

## 常见 OOM 有哪几种？分别什么原因？

    - 堆内存溢出
    - 元空间溢出
    - 栈溢出
    - 直接内存溢出

## 常见 JVM 参数：-Xms、-Xmx、-Xss、-XX:MetaspaceSize 等含义？

堆内存参数：

- -Xms：堆的初始大小（minimum），JVM 启动时分配的堆内存，如 -Xms512m。
- -Xmx：堆的最大大小（maximum），超过此大小抛 OutOfMemoryError，如 -Xmx2g。
  建议：生产环境将 -Xms 和 -Xmx 设置为相同值，避免堆动态扩缩容带来的性能抖动。
- -Xmn：新生代大小，如 -Xmn512m（也可用 -XX:NewSize/-XX:MaxNewSize）。
- -XX:NewRatio：老年代与新生代的比例，默认 2（即老:新 = 2:1）。
- -XX:SurvivorRatio：Eden 与 Survivor 的比例，默认 8（Eden:S0:S1 = 8:1:1）。

线程栈参数：

- -Xss：每个线程的栈大小，默认 512K 或 1M（不同平台不同），如 -Xss256k。调小可以支持更多线程，调大可以执行更深的递归调用。

元空间参数：

- -XX:MetaspaceSize：元空间初始大小（触发 GC 的阈值），非固定分配量，如 -XX:MetaspaceSize=256m。
- -XX:MaxMetaspaceSize：元空间最大大小，默认不限制，如 -XX:MaxMetaspaceSize=512m。

GC 参数：

- -XX:+UseG1GC：启用 G1 收集器（JDK 9+ 默认）。
- -XX:MaxGCPauseMillis：G1 目标最大 GC 停顿时间，如 -XX:MaxGCPauseMillis=200。
- -XX:+PrintGCDetails：打印 GC 详细日志（JDK 9+ 用 -Xlog:gc*）。
- -XX:+HeapDumpOnOutOfMemoryError：OOM 时自动 dump 堆内存快照。
- -XX:HeapDumpPath：堆 dump 文件路径，如 -XX:HeapDumpPath=/tmp/heapdump.hprof。

## 如何排查线上 OOM？完整排查流程？

完整排查流程：

1. 确认 OOM 类型（看错误信息）：
    - java.lang.OutOfMemoryError: Java heap space → 堆内存不足。
    - java.lang.OutOfMemoryError: Metaspace → 元空间不足（加载类太多）。
    - java.lang.OutOfMemoryError: Direct buffer memory → 直接内存不足。
    - java.lang.StackOverflowError → 栈溢出（递归太深）。

2. 获取堆 dump 文件：
    - 提前配置：-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heap.hprof（OOM 时自动 dump）。
    - 手动抓取：jmap -dump:format=b,file=heap.hprof <pid>（需要进程还活着）。

3. 分析堆 dump：
    - 使用 MAT（Eclipse Memory Analyzer）或 JVisualVM 打开 hprof 文件。
    - 查看"Leak Suspects"（内存泄漏嫌疑报告），找到占用内存最大的对象类型。
    - 追踪该对象的引用链（dominator tree），找到是哪段代码持有大量对象导致无法 GC。

4. 结合 GC 日志分析：
    - 查看 GC 日志，观察 Full GC 前后的堆内存变化，判断是内存泄漏（每次 GC 后老年代回收极少）还是内存不足（业务量增长导致正常的内存使用量超限）。

5. 定位代码根因：
    - 常见原因：内存泄漏（集合无限增长、ThreadLocal 未 remove、缓存无上限）；单次请求加载数据量过大；字符串拼接产生大量临时对象；第三方库资源未关闭；频繁反射/动态代理（元空间）。

6. 修复+验证：修复代码后，压测或灰度观察 GC 指标是否恢复正常。

## 如何排查频繁 Full GC？

排查步骤：

1. 确认 Full GC 是否真的频繁：jstat -gcutil <pid> 1000 观察 FGC 计数，每分钟超过 1 次就算频繁。

2. 看 GC 日志（-Xlog:gc* 或 -XX:+PrintGCDetails）：观察每次 Full GC 后老年代的回收量，若每次回收后老年代仍占用高比例，说明有内存泄漏或堆内存分配太小。

3. 常见 Full GC 原因及排查方向：
    - 老年代被打满：查看堆 dump，找占大头的对象类型，追踪引用链。
    - 内存泄漏：每次 GC 后老年代占用持续增长，逐步逼近 Full GC 触发阈值。
    - 大对象直接进老年代：检查是否有超大数组、大 SQL 结果集一次性加载到内存。
    - 新生代晋升速率过快：Minor GC 频繁触发晋升，老年代空间快速被填满，查看对象创建速率。
    - 元空间不足：应用频繁加载 Class（动态代理、JSP、groovy 脚本等），适当调大 MaxMetaspaceSize。
    - System.gc() 被显式调用：grep 代码是否有 System.gc()，用 -XX:+DisableExplicitGC 禁用。
    - CMS Concurrent Mode Failure：CMS 并发 GC 期间老年代提前填满，触发 Serial Old Full GC，需要调整
      CMSInitiatingOccupancyFraction 提前触发 CMS。

4. 调优方向：增大堆或老年代比例；减少不必要的大对象；修复内存泄漏；优化对象生命周期（尽量让对象在新生代死亡）。

## 什么是内存泄漏？和内存溢出区别？

内存泄漏（Memory Leak）：程序中已经不再使用的对象，由于仍然被某些引用持有，导致 GC 无法回收，这些对象持续占用内存，随着时间推移内存使用量不断增长的现象。
特点：对象本身还存在引用，GC 视角认为它是存活的；内存占用缓慢增长，最终可能导致 OOM；不易立即发现，需要长时间运行才暴露。
常见场景：静态集合无限增长（static List/Map）、ThreadLocal 未 remove、未关闭的资源（Connection/Stream）、监听器/回调未取消注册、内部类持有外部类引用。

内存溢出（Out Of Memory，OOM）：JVM 申请内存时，没有足够的内存可分配，直接抛出 OutOfMemoryError 错误。
特点：立即抛出异常，应用直接崩溃；是一种错误（Error），不是异常。

二者关系：

- 内存泄漏累积到一定程度，会导致内存溢出。
- 内存溢出不一定都是内存泄漏导致的，也可能是正常的内存需求超过了分配的堆大小（如一次加载 2GB 数据到 1GB 堆）。
- 内存泄漏是因，内存溢出可能是果，但也可以是直接原因（配置不足）。

简记：泄漏是"该死的死不了"，溢出是"内存装不下了"。

## 线上 CPU 飙高、线程死锁怎么排查？

CPU 飙高排查流程：

1. top 命令找到 CPU 占用最高的 Java 进程 PID。
2. top -Hp <pid> 找到该进程内 CPU 占用最高的线程 TID（十进制）。
3. 将 TID 转为十六进制：printf '%x\n' <tid>，得到 hex_tid。
4. jstack <pid> > stack.txt 生成线程堆栈快照。
5. 在 stack.txt 中搜索 "nid=0x<hex_tid>"，找到对应线程的调用栈。
6. 分析调用栈：若是业务代码，定位具体逻辑（死循环？大量计算？）；若是 GC 线程（VMThread），说明频繁 Full GC。

线程死锁排查流程：

1. jstack <pid> > stack.txt 生成线程堆栈。
2. jstack 会自动检测死锁并打印 "Found one Java-level deadlock:" 信息，直接搜索即可。
3. 查看死锁线程的堆栈，找到互相持有的锁（waiting to lock <0x...> while holding <0x...>）。
4. 对应到业务代码，分析为什么会出现循环等待，修复加锁顺序或使用 tryLock 超时机制。

工具辅助：Arthas（阿里开源）的 thread -b 命令可以直接找出阻塞其他线程最多的线程，比手动分析 jstack 更高效。

## jps、jstack、jmap、jhat、jstat 常用命令作用？

jps（JVM Process Status）：列出当前所有 Java 进程及其 PID 和主类名。
常用：jps -l（显示完整类名）、jps -v（显示 JVM 参数）。

jstack（JVM Stack Trace）：打印指定 Java 进程的所有线程堆栈快照。
用途：排查 CPU 飙高（找高占用线程）、死锁分析、线程状态分析。
常用：jstack <pid>、jstack -l <pid>（显示锁信息）。

jmap（JVM Memory Map）：用于查看堆内存信息和生成堆 dump 文件。
常用：

- jmap -heap <pid>：查看堆内存配置和使用情况。
- jmap -histo <pid>：查看堆中对象类型统计（类名、实例数、占用字节）。
- jmap -dump:format=b,file=heap.hprof <pid>：生成堆 dump 文件。

jhat（JVM Heap Analysis Tool）：分析 jmap 生成的堆 dump 文件，启动一个 HTTP 服务，通过浏览器查看对象信息。现已不推荐（功能弱），建议用
MAT 替代。

jstat（JVM Statistics Monitoring）：实时监控 JVM 统计信息（GC、类加载、编译等）。
常用：

- jstat -gcutil <pid> 1000：每秒打印 GC 各区使用率和 GC 次数/耗时。
- jstat -gc <pid> 1000：打印 GC 详细数据（各区容量和使用量）。
- jstat -class <pid>：类加载统计。

## 堆 dump 怎么抓取、怎么分析？

抓取堆 dump 的方式：

1. JVM 自动 dump（推荐提前配置）：启动参数加 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heap.hprof，OOM 发生时自动生成。
2. 手动抓取（进程运行中）：jmap -dump:format=b,live,file=heap.hprof <pid>（live 只 dump 存活对象，文件更小）。
3. 通过 JVM 诊断工具：Arthas 的 heapdump 命令；jcmd <pid> GC.heap_dump filename=heap.hprof。
4. JVisualVM / JConsole 图形界面：远程连接 JMX，图形化点击生成 dump。

注意：手动 jmap dump 时会触发 Full GC（加 live 选项），线上谨慎操作，可能造成短暂停顿。

分析堆 dump：

1. MAT（Eclipse Memory Analyzer Tool）：最主流的堆分析工具，免费开源。
    - 打开 hprof 文件，选择 "Leak Suspects" 报告，自动分析最可能的内存泄漏点。
    - Dominator Tree：按对象"支配的内存量"排序，快速找到占内存最大的对象及其引用链。
    - Histogram：按类分组，显示各类实例数量和总占用内存，找异常增多的类。
    - OQL 查询：类似 SQL 语法查询对象，如 SELECT * FROM java.util.ArrayList a WHERE a.size > 1000。
2. JProfiler / YourKit：商业工具，功能更强，分析更直观，适合团队使用。
3. JVisualVM（JDK 内置）：轻量级，基本够用。

# 计算机网络

# 基础认识

## 网络五层模型 / 七层 OSI 模型分别是什么？每层作用

OSI 七层模型（从上到下）：
| 层次 | 名称 | 作用 | 典型协议/设备 |
|------|------|------|------------|
| 7 | 应用层 | 为应用程序提供网络服务 | HTTP、HTTPS、FTP、DNS、SMTP |
| 6 | 表示层 | 数据格式转换、加密、压缩 | SSL/TLS、JPEG、MPEG |
| 5 | 会话层 | 建立/管理/终止会话 | NetBIOS、RPC |
| 4 | 传输层 | 端到端可靠传输，端口区分应用 | TCP、UDP |
| 3 | 网络层 | 逻辑寻址、路由选择（IP 地址） | IP、ICMP、路由器 |
| 2 | 数据链路层 | 物理地址寻址（MAC），帧传输 | Ethernet、交换机 |
| 1 | 物理层 | 二进制位流的物理传输 | 网线、光纤、Hub |

TCP/IP 五层模型（实际使用的简化版）：
| 层次 | 对应 OSI | 典型协议 |
|------|---------|---------|
| 应用层 | 应用层+表示层+会话层 | HTTP、DNS、FTP |
| 传输层 | 传输层 | TCP、UDP |
| 网络层 | 网络层 | IP、ICMP |
| 数据链路层 | 数据链路层 | Ethernet |
| 物理层 | 物理层 | 网线、光纤 |

注：实际面试常考 TCP/IP 四层模型（将数据链路层和物理层合并为网络接口层）。

## 从用户点击一个网页，到这个网页响应中间都经历了什么

1. URL 解析：浏览器解析输入的 URL，确定协议（HTTP/HTTPS）、域名、端口、路径。

2. DNS 解析：将域名转为 IP 地址。
    - 查本地 DNS 缓存（浏览器缓存 → 操作系统缓存 → 本地 hosts 文件）。
    - 未命中则向本地 DNS 服务器查询，本地 DNS 依次向根域名服务器、顶级域名服务器、权威 DNS 服务器递归查询，返回 IP。

3. TCP 三次握手：浏览器与目标服务器（IP:端口）建立 TCP 连接，完成三次握手。

4. HTTPS 的 TLS 握手（若是 HTTPS）：在 TCP 连接上协商加密套件，交换证书验证身份，协商会话密钥，建立加密通道。

5. 发送 HTTP 请求：浏览器发送 HTTP 请求报文（GET /path HTTP/1.1，含请求头、Cookie 等）。

6. 服务器处理请求：请求经过 CDN、负载均衡器、反向代理（如 Nginx）转发到应用服务器；应用服务器处理业务逻辑（查询数据库/缓存/调用微服务等），生成响应数据。

7. 返回 HTTP 响应：服务器返回响应报文（状态码、响应头、响应体 HTML/JSON）。

8. 浏览器渲染：解析 HTML 构建 DOM 树，解析 CSS 构建 CSSOM，合并生成渲染树，执行 JS，布局绘制页面，加载图片/字体等资源（可能产生新的
   HTTP 请求）。

9. TCP 四次挥手（或保持长连接复用）：HTTP/1.1 默认 Keep-Alive，保持 TCP 连接一段时间供后续请求复用；最终请求结束后四次挥手关闭连接。

# http/https

## HTTP 与 HTTPS 区别

| 对比    | HTTP      | HTTPS             |
|-------|-----------|-------------------|
| 安全性   | 明文传输，不安全  | SSL/TLS 加密，安全     |
| 默认端口  | 80        | 443               |
| 证书    | 不需要       | 需要 CA 颁发的数字证书     |
| 速度    | 较快（无加密开销） | 略慢（TLS 握手 + 加解密）  |
| SEO   | 较低        | Google 优先收录 HTTPS |
| 数据完整性 | 无保障，可被篡改  | 有 MAC 校验，防篡改      |
| 身份验证  | 无         | 证书验证服务器身份，防中间人攻击  |

HTTPS 的三大安全保障：

1. 加密：使用对称加密传输数据，密钥通过非对称加密协商，保证数据机密性。
2. 完整性：使用 MAC（消息认证码）验证数据未被篡改。
3. 身份认证：CA 证书验证服务器身份，防止中间人伪造。

## HTTP1.0 / 1.1 / HTTP2 / HTTP3 区别

HTTP/1.0：

- 短连接：每个请求建立独立 TCP 连接，请求完成后立即关闭，三次握手开销大。
- 无 Host 请求头，无缓存控制。

HTTP/1.1：

- 长连接（Keep-Alive）：默认复用 TCP 连接，减少握手开销。
- 管道化（Pipelining）：可以连续发送多个请求，但服务器必须按顺序响应，队头阻塞（Head-of-Line Blocking）问题严重，实际很少使用。
- 新增缓存控制（Cache-Control）、分块传输（Chunked Transfer Encoding）、Etag 等特性。

HTTP/2：

- 多路复用：一个 TCP 连接上并发多个请求/响应流（Stream），彻底解决了 HTTP 层的队头阻塞问题。
- 头部压缩（HPACK）：压缩请求/响应头，减少重复头部传输开销。
- 二进制帧：将数据拆分为帧（Frame），更高效解析；HTTP/1.x 是文本协议。
- 服务器推送（Server Push）：服务器可主动推送客户端可能需要的资源。
- 仍基于 TCP，TCP 层的队头阻塞（丢包时阻塞所有流）问题依然存在。

HTTP/3：

- 基于 QUIC 协议（UDP 上实现可靠传输），彻底放弃 TCP。
- 解决 TCP 层队头阻塞：QUIC 中不同流的丢包互不影响。
- 0-RTT 或 1-RTT 连接建立：大幅减少连接建立时间（TLS 1.3 集成）。
- 连接迁移：客户端网络切换（WiFi → 4G）时，QUIC 连接不断开（基于 Connection ID，而非 IP+端口）。

## HTTP 常见请求方法 GET POST PUT DELETE HEAD

- GET：获取资源，幂等、安全（不修改数据），参数在 URL 中。
- POST：提交数据，创建新资源，非幂等，数据在请求体中，可提交敏感数据。
- PUT：更新（替换）指定资源，幂等，用整体数据覆盖目标资源。
- PATCH：部分更新资源，只修改提交的字段，非幂等（部分实现幂等）。
- DELETE：删除指定资源，幂等。
- HEAD：与 GET 相同，但服务器只返回响应头，不返回响应体，用于检查资源是否存在、获取元信息。
- OPTIONS：查询服务器支持的方法，常用于 CORS 跨域预检请求（Preflight）。
- CONNECT：用于建立隧道代理（如 HTTPS 代理）。
- TRACE：回显服务器收到的请求，主要用于诊断，有安全风险，生产通常禁用。

幂等：同一请求执行多次，结果与执行一次相同。GET/PUT/DELETE 幂等，POST 不幂等。

## GET 和 POST 区别（参数、缓存、长度、安全性、编码）

| 对比维度  | GET                             | POST                                    |
|-------|---------------------------------|-----------------------------------------|
| 参数位置  | URL 查询字符串（明文可见，如 ?key=value）    | 请求体（Body），不在 URL 中                      |
| 数据安全性 | 低，URL 会被浏览器历史、服务器日志记录           | 相对高，Body 不在 URL 中（HTTPS 加密下都安全）         |
| 数据长度  | 受 URL 长度限制（浏览器/服务器通常限制 2KB-8KB） | Body 理论无限制（实际受服务器配置限制）                  |
| 缓存    | 可以被浏览器缓存，也可被代理缓存                | 默认不缓存                                   |
| 幂等性   | 幂等（多次请求结果相同）                    | 不幂等（多次提交可能创建多条记录）                       |
| 书签/分享 | URL 可收藏为书签，可直接分享                | 不可直接分享（状态不在 URL 中）                      |
| 编码    | 只支持 URL 编码（ASCII），特殊字符需 % 编码    | 支持多种编码（form-urlencoded、multipart、JSON等） |
| 回退刷新  | 无害（重新发起同样 GET）                  | 会再次提交表单，可能造成重复操作（弹出确认提示）                |

本质区别（RFC 定义）：GET 是"获取资源"语义，POST 是"提交数据处理"语义，两者语义差异才是根本；参数位置差异是语义的自然体现，并非强制限制。

## HTTP 状态码分类 1xx/2xx/3xx/4xx/5xx

- 1xx（信息性）：请求已收到，继续处理。如 100 Continue（客户端可继续发送请求体）。
- 2xx（成功）：请求成功处理。如 200 OK、201 Created、204 No Content。
- 3xx（重定向）：需要客户端进一步操作完成请求。如 301 永久重定向、302 临时重定向、304 未修改。
- 4xx（客户端错误）：请求有误，服务器无法处理。如 400 请求语法错误、401 未认证、403 无权限、404 未找到。
- 5xx（服务端错误）：服务器处理请求时出错。如 500 服务器内部错误、502 网关错误、503 服务不可用、504 网关超时。

## 常用状态码：200、301、302、304、400、401、403、404、500

- 200 OK：请求成功，响应体中包含请求的数据。
- 201 Created：POST 请求成功，新资源已创建，通常在 Location 头返回新资源 URL。
- 204 No Content：请求成功，无响应体（如 DELETE 操作成功）。
- 301 Moved Permanently：永久重定向，资源已永久移到新 URL，浏览器会缓存新地址，下次直接访问新 URL。
- 302 Found：临时重定向，资源临时移到新 URL，浏览器不缓存，下次仍请求原 URL。
- 304 Not Modified：缓存有效，资源未修改，浏览器直接使用本地缓存（与 If-Modified-Since / ETag 配合使用）。
- 400 Bad Request：请求语法错误或参数不合法，服务器无法解析。
- 401 Unauthorized：未认证（未登录/Token 过期），需要先完成身份验证。
- 403 Forbidden：已认证但无权限访问，服务器拒绝请求。
- 404 Not Found：请求的资源不存在。
- 405 Method Not Allowed：请求方法不允许（如对只支持 GET 的接口发 POST）。
- 500 Internal Server Error：服务器内部发生未处理的错误（通用服务端错误）。
- 502 Bad Gateway：网关/代理从上游服务器收到无效响应（如 Nginx 代理后端崩溃）。
- 503 Service Unavailable：服务临时不可用（过载或维护中）。
- 504 Gateway Timeout：网关等待上游服务器响应超时。

## 301 永久重定向和 302 临时重定向区别

| 对比     | 301 Moved Permanently         | 302 Found           |
|--------|-------------------------------|---------------------|
| 含义     | 资源永久迁移到新 URL                  | 资源临时移到新 URL         |
| 浏览器缓存  | 会缓存重定向，下次直接访问新 URL            | 不缓存，每次仍请求原 URL      |
| SEO 影响 | 权重传递到新 URL，原 URL 逐渐失效         | 权重不转移，原 URL 保留权重    |
| 请求方法   | 可能将 POST 改为 GET（实践中各浏览器不统一）   | 同样可能改变请求方法          |
| 适用场景   | 域名更换、URL 永久迁移、HTTP→HTTPS 强制跳转 | 临时跳转（登录后重定向、A/B 测试） |

补充：307 Temporary Redirect 和 308 Permanent Redirect 是更严格的版本，明确要求重定向后必须保持原请求方法（POST 不会被改为
GET）。

## HTTPS 加密流程、SSL/TLS 握手过程

TLS 1.2 握手过程（4次握手，简化版）：

1. Client Hello：客户端发送支持的 TLS 版本、加密套件列表（Cipher Suites）、随机数 Client Random。
2. Server Hello：服务器选择 TLS 版本和加密套件，发送随机数 Server Random，并发送服务器的数字证书（包含服务器公钥）。
3. 客户端验证证书：验证证书是否由受信任的 CA 签发、是否过期、域名是否匹配。验证通过后，客户端生成预主密钥（Pre-Master
   Secret），用服务器公钥加密后发送给服务器。
4. 双方生成会话密钥：双方各自用 Client Random + Server Random + Pre-Master Secret 通过 PRF
   算法推导出相同的会话密钥（对称密钥），用于后续数据的对称加密传输。
5. 客户端发送 Change Cipher Spec（通知后续使用加密通信）+ Finished（用会话密钥加密的握手摘要）。
6. 服务器发送 Change Cipher Spec + Finished，握手完成。

TLS 1.3 优化：2-RTT 握手减为 1-RTT，0-RTT 恢复连接；废弃了老旧不安全的加密套件。

后续通信：用协商好的对称会话密钥加密/解密所有 HTTP 数据（对称加密速度快），保证通信安全。

## 对称加密、非对称加密、数字证书作用

对称加密：加密和解密使用同一把密钥。

- 优点：速度快，适合大量数据加密。
- 缺点：密钥如何安全传输是难题（密钥也要加密传输，形成鸡生蛋问题）。
- 代表算法：AES（最常用）、DES（已淘汰）。

非对称加密：公钥加密，私钥解密（或私钥签名，公钥验签）。一对密钥，公钥公开，私钥保密。

- 优点：解决了密钥分发问题，不需要安全信道传输密钥。
- 缺点：速度比对称加密慢 1000 倍以上，不适合大量数据加密。
- 代表算法：RSA、ECC（椭圆曲线）。

HTTPS 中的使用方式：用非对称加密安全地协商对称密钥，用对称密钥加密实际通信数据（两者取长补短）。

数字证书作用：

- 数字证书由受信任的第三方机构（CA，Certificate Authority）颁发，绑定了服务器的公钥和身份信息（域名、组织等）。
- 解决了公钥真实性问题：如果没有证书，中间人可以替换服务器公钥，客户端无法分辨真假。
- 证书的信任链：CA 用自己的私钥对服务器证书签名，浏览器内置受信任的根 CA 列表，逐级验证签名的合法性（根证书 → 中间证书 →
  服务器证书）。
- 数字签名：CA 对证书内容做哈希 + 用私钥加密生成签名，客户端用 CA 公钥解密签名，与本地计算的哈希对比，验证证书未被篡改。

## HTTPS 为什么安全？中间人攻击如何防御

HTTPS 安全的三大保障：

1. 加密（机密性）：TLS 握手后使用对称加密传输所有数据，即使数据被截获也无法解密。
2. 完整性（防篡改）：使用 MAC（消息认证码）对每条消息做完整性校验，任何篡改都会被检测出来。
3. 身份认证（防伪装）：数字证书验证服务器身份，确保通信对象是真正的目标服务器，而非冒充者。

中间人攻击（MITM）原理：攻击者拦截客户端与服务器的通信，分别与两端建立连接，读取/篡改数据，两端都不知道。

HTTPS 如何防御中间人攻击：

- 攻击者无法伪造合法的数字证书（没有 CA 私钥无法签发有效证书）。
- 浏览器会验证证书：域名是否匹配、是否由受信 CA 签发、是否过期、是否在吊销列表中，任何一项不通过就弹出安全警告，阻止连接。
- 即使攻击者自签一张证书，浏览器根 CA 列表中没有攻击者的根证书，验证失败。

使用 HTTPS 仍可能被攻击的场景：

- 用户主动信任了攻击者的自签证书（如公司/某些软件的中间人证书）。
- 证书 CA 被攻破（极罕见）。
- HSTS（HTTP Strict Transport Security）+ HTTP Public Key Pinning 可进一步加强防护。

## DNS 域名解析全过程

DNS 解析过程（以访问 www.example.com 为例）：

1. 本地缓存查询（最快）：
    - 浏览器 DNS 缓存（浏览器维护的 TTL 内缓存）。
    - 操作系统 DNS 缓存（/etc/hosts 文件或系统缓存，优先级高于 DNS 查询）。
    - 本地 hosts 文件匹配。

2. 向本地 DNS 服务器（Recursive Resolver）发起查询：
    - 即 ISP（运营商）的 DNS 服务器，或自定义的 8.8.8.8（Google）等。
    - 本地 DNS 先检查自己的缓存，命中则直接返回。

3. 向根域名服务器查询：
    - 根服务器返回 .com 顶级域名服务器（TLD Server）的地址（不返回结果，只告诉下一步去哪问）。

4. 向 TLD 域名服务器查询（.com 服务器）：
    - 返回 example.com 的权威 DNS 服务器地址。

5. 向权威 DNS 服务器查询：
    - 返回 www.example.com 的真实 IP 地址（A 记录）。

6. 本地 DNS 缓存结果并返回给客户端，客户端缓存（依据 TTL），开始建立 TCP 连接。

DNS 使用 UDP 协议（端口 53）传输查询请求（速度快，包体小），响应超 512 字节时使用 TCP。

# tcp和udp

## TCP 与 UDP 区别、适用场景

| 对比   | TCP              | UDP         |
|------|------------------|-------------|
| 连接   | 面向连接（三次握手，四次挥手）  | 无连接，直接发送    |
| 可靠性  | 可靠传输（确认、重传、顺序保证） | 不可靠，可能丢包、乱序 |
| 传输单位 | 字节流（无边界）         | 数据报（有边界）    |
| 速度   | 较慢（建立连接+确认机制开销大） | 快（无连接、无确认）  |
| 拥塞控制 | 有                | 无           |
| 流量控制 | 有（滑动窗口）          | 无           |
| 头部大小 | 20-60 字节         | 8 字节（固定）    |
| 全双工  | 支持               | 支持          |

TCP 适用场景：要求数据完整性的场景：网页浏览（HTTP）、文件传输（FTP）、邮件（SMTP）、数据库连接。

UDP 适用场景：要求速度和低延迟，允许少量数据丢失：视频直播/通话（RTC）、DNS 查询、在线游戏、IoT 传感器数据、HTTP/3（QUIC 基于 UDP
自实现可靠性）。

## TCP 为什么可靠？可靠传输四大机制

TCP 可靠传输依赖以下四大机制：

1. 序号与确认应答（ACK）：
    - 每个字节都有序号，发送方发送数据段，接收方收到后发送 ACK 确认，并指明下一个期望收到的序号。
    - 发送方收到 ACK 才确认数据已送达，未收到 ACK 则认为数据丢失。

2. 超时重传：
    - 发送方为每个发送的数据维护一个定时器（RTO，重传超时时间），超时未收到 ACK 则重传该数据。
    - RTO 自适应：根据 RTT（往返时延）动态调整，避免重传过早或过晚。
    - 快速重传：收到 3 个重复 ACK（确认同一序号），不等超时立即重传（说明后续数据收到了但某个包丢了）。

3. 流量控制（滑动窗口）：
    - 接收方在 ACK 中通告自己的接收窗口大小（rwnd），发送方发送速率不超过接收方处理速率，防止接收方缓冲区溢出。
    - 窗口大小动态变化，接收缓冲区空了窗口变大，满了窗口缩小甚至为 0（发送方暂停）。

4. 拥塞控制：
    - 控制发送速率不超过网络承载能力，防止网络拥塞。
    - 四个阶段：慢启动（指数增长）→ 拥塞避免（线性增长）→ 快重传 → 快恢复。

## TCP 三次握手全过程、为什么三次不是两次

三次握手全过程：

1. 第一次握手（SYN）：客户端发送 SYN=1（同步标志位），选择初始序号 ISN_c，发送 seq=ISN_c，进入 SYN_SENT 状态。
2. 第二次握手（SYN+ACK）：服务器收到 SYN，发送 SYN=1, ACK=1，选择自己的初始序号 ISN_s，发送 seq=ISN_s，ack=ISN_c+1，进入
   SYN_RCVD 状态。
3. 第三次握手（ACK）：客户端收到服务器的 SYN+ACK，发送 ACK=1，seq=ISN_c+1，ack=ISN_s+1，进入 ESTABLISHED 状态；服务器收到 ACK
   后也进入 ESTABLISHED 状态。

为什么是三次而不是两次：
两次握手无法解决以下问题：

1. 确保双方都具备发送和接收能力：两次握手只能让服务器确认"客户端能发能收"，但客户端无法确认"服务器能发"
   （第三次握手就是客户端告诉服务器：我收到你发的 SYN+ACK 了，你的发送也正常）。
2. 防止历史连接（旧 SYN）干扰：网络中可能存在延迟的旧 SYN 报文，若只有两次握手，旧 SYN
   到达服务器时服务器直接建立连接并等待，浪费资源；三次握手时客户端可以识别出这是旧 SYN，发送 RST 拒绝，让服务器关闭连接。
3. 同步双方的初始序号（ISN）：三次握手确保双方都确认了对方的初始序号，建立可靠的序号机制。

四次握手：理论上可以，但 SYN+ACK 合并为一步更高效，没必要拆成四次。

## 什么是SYN泛洪攻击

SYN 泛洪攻击（SYN Flood）是一种 DDoS 攻击方式，利用 TCP 三次握手的漏洞：

攻击原理：

1. 攻击者向服务器发送大量 SYN 报文，但不完成第三次握手（不发送 ACK），或使用伪造的源 IP。
2. 服务器收到 SYN 后分配资源，发送 SYN+ACK，进入 SYN_RCVD 状态，等待第三次握手。
3. 攻击者不响应，服务器超时重试多次 SYN+ACK 仍无应答，最终连接超时释放，但需要等待（默认约 75 秒）。
4. 攻击者持续发送大量 SYN，导致服务器的半连接队列（SYN Queue）被填满，后续合法的连接请求无法入队，被直接丢弃，服务不可用。

防御手段：

1. SYN Cookie：服务器收到 SYN 时不立即分配资源，而是根据 SYN 报文计算一个加密 Cookie 作为 ISN 发送 SYN+ACK；只有收到正确
   ACK（Cookie 验证通过）时才建立连接，避免资源消耗。Linux 默认支持 -XX:tcp_syncookies=1。
2. 减小半连接超时时间：缩短等待超时，加快资源释放。
3. 增大半连接队列：-XX:net.ipv4.tcp_max_syn_backlog 调大。
4. 防火墙过滤：识别并限制来自单一 IP 的 SYN 频率；IP 黑名单；CDN 流量清洗。
5. 限制重传次数：-XX:net.ipv4.tcp_synack_retries 调小，减少重试次数。

## TCP 四次挥手全过程、为什么四次

四次挥手全过程（假设客户端主动关闭）：

1. 第一次挥手（FIN）：客户端发送 FIN=1，seq=u，表示客户端不再发送数据，但仍可接收；进入 FIN_WAIT_1 状态。
2. 第二次挥手（ACK）：服务器收到 FIN，发送 ACK=1，ack=u+1，表示"我收到你的关闭请求了，但我可能还有数据要发"；进入 CLOSE_WAIT
   状态；客户端进入 FIN_WAIT_2 状态（继续等待服务器的 FIN）。
3. 第三次挥手（FIN）：服务器数据发送完毕，发送 FIN=1，seq=v，表示服务器也不再发送数据；进入 LAST_ACK 状态。
4. 第四次挥手（ACK）：客户端收到服务器的 FIN，发送 ACK=1，ack=v+1；进入 TIME_WAIT 状态（等待 2MSL 后真正关闭）；服务器收到 ACK
   后进入 CLOSED 状态。

TIME_WAIT（2MSL 等待）：

- MSL（Maximum Segment Lifetime）：报文段在网络中的最大存活时间，一般 30s-2min。
- 等待 2MSL 的原因：确保第四次 ACK 能到达服务器（如果 ACK 丢失，服务器会重发 FIN，2MSL 足够等到重传的 FIN 并再次
  ACK）；防止旧连接的数据包干扰新连接（2MSL 后旧连接所有报文都消失）。

## 为什么建立连接三次，断开四次

建立连接三次握手：
服务器的 SYN 和 ACK 可以合并为一条消息（SYN+ACK），所以只需要三次交互完成双方序号同步和能力确认。

断开连接四次挥手：
TCP 是全双工协议，双方各自有独立的数据通道，关闭时需要各自关闭自己的发送通道：

- 客户端发 FIN：我没有数据发了，关闭我的发送通道。
- 服务器发 ACK：我知道了。但我还可能有数据要发（处于 CLOSE_WAIT 状态，继续发送剩余数据）。
- 服务器发 FIN：我的数据也发完了，关闭我的发送通道。
- 客户端发 ACK：确认收到。

服务器的 ACK（第二次）和 FIN（第三次）不能合并（不像握手时可以），因为服务器收到客户端 FIN 后可能还有数据要发，不能立即发
FIN，中间有"半关闭"状态（服务器可收不能发→服务器发完后才 FIN），所以必须拆成两步，共四次。

简记：建立时双方序号同步可以合并，断开时双方半关闭状态不对称，无法合并。

## TCP 滑动窗口原理、流量控制

滑动窗口原理：
不采用停等协议（发一条等 ACK），而是允许发送方连续发送多个数据包（不超过窗口大小），不必等待每个包的 ACK，大幅提升链路利用率。

发送窗口（swnd）= min（接收窗口 rwnd，拥塞窗口 cwnd）：

- 接收窗口（rwnd）：接收方在 ACK 中通告，反映接收方缓冲区剩余大小。
- 拥塞窗口（cwnd）：发送方根据网络拥塞状态动态维护，反映网络承载能力。

滑动机制：

- 发送方维护三个指针：已确认位置、已发送未确认位置、可发送位置。
- 每次收到 ACK，窗口整体向右滑动（已确认数据出队），可以继续发送新数据。
- 发送方缓冲区中，已确认的数据可以释放，未确认的数据保留等待超时重传。

流量控制（点对点，端到端）：

- 目的：防止发送速度 > 接收方处理速度，导致接收缓冲区溢出、数据丢失。
- 机制：接收方通过 ACK 中的 Window 字段动态通告自己的可用接收窗口大小，发送方严格控制在窗口内发送。
- 接收窗口为 0 时：发送方停止发送，定时发送探测包（Zero Window Probe），直到接收窗口恢复。

## TCP 拥塞控制：慢启动、拥塞避免、快重传、快恢复

拥塞控制目的：防止网络整体拥塞，控制发送速率使其不超过网络承载能力（与流量控制的区别：流量控制针对接收端，拥塞控制针对网络）。

核心变量：拥塞窗口（cwnd）、慢启动阈值（ssthresh，初始 65535 字节）。

四个阶段：

1. 慢启动（Slow Start）：
    - 初始 cwnd=1 MSS，每收到一个 ACK，cwnd+1（即每个 RTT cwnd 翻倍），指数增长。
    - 不是"慢"，是"从小开始"，避免一开始就打爆网络。
    - 当 cwnd 达到 ssthresh 时，进入拥塞避免。

2. 拥塞避免（Congestion Avoidance）：
    - 每个 RTT，cwnd+1 MSS，线性增长（避免增长过快）。
    - 检测到拥塞（超时或 3 重复 ACK）时，进入对应处理。

3. 快重传（Fast Retransmit）：
    - 收到 3 个相同的重复 ACK，说明某个包丢了但后续包已收到，立即重传丢失包，无需等待超时。
    - 触发条件：3 个重复 ACK（而非超时）。

4. 快恢复（Fast Recovery）：
    - 收到 3 重复 ACK 后（快重传场景）：ssthresh = cwnd/2，cwnd = ssthresh（不像超时那样降到 1），进入拥塞避免，避免速率骤降。
    - 超时场景（更严重的拥塞）：ssthresh = cwnd/2，cwnd 重置为 1，重新慢启动。

## TCP 粘包问题产生原因、解决方案

粘包是什么：TCP 是字节流协议，没有消息边界概念。发送方发送的多条消息，接收方可能在一次 recv 中读到多条合并的数据（粘包），或一条消息被拆成多次读到（拆包）。

产生原因：

1. 发送方 Nagle 算法：为减少小包数量，Nagle 算法会将多个小数据包合并为一个 TCP 包发送，导致接收方一次读到多条消息合并的数据（粘包）。
2. 接收方 TCP 缓冲区：接收方读取速度慢，多次发送的数据积累在接收缓冲区，一次 read 读出多条消息（粘包）。
3. MTU 分片：数据包超过 MTU（最大传输单元，通常 1500 字节）时，会被拆分为多个 TCP 段，接收方收到多段才能还原（拆包）。

解决方案（应用层处理，TCP 本身不解决）：

1. 定长消息（Fixed-Length）：每条消息固定字节数，不足则填充。简单但浪费带宽，不灵活。
2. 分隔符（Delimiter）：消息末尾加特殊分隔符（如换行符 \n、HTTP 用 CRLF），读到分隔符则认为一条消息结束。HTTP/1.1
   的文本协议就用换行分隔头部。缺点：消息内容中出现分隔符需转义处理。
3. 消息头+长度字段（Length-Prefix）：每条消息前加固定长度的头部，头部中包含消息体的长度字段。接收方先读头部，获取长度，再读对应字节数的消息体。最通用、最常用，Netty、RPC
   框架大多采用此方案。
4. 禁用 Nagle 算法：TCP_NODELAY 选项，实时性要求高的场景使用（如键盘输入实时传输），不建议作为通用解决方案。

## TCP 头部重要字段：序号、确认号、标志位

TCP 头部最小 20 字节，最大 60 字节（含选项），重要字段：

序号（Sequence Number，32位）：当前数据段第一个字节的序号，用于标识发送的数据字节流位置，保证接收方能按顺序重组数据。连接建立时使用随机初始序号（ISN），防止历史连接干扰。

确认号（Acknowledgment Number，32位）：期望收到对方下一个字节的序号（即已正确接收到此前所有字节）。只有当 ACK 标志位为 1
时，确认号才有效。

标志位（6个主要标志）：

- SYN（同步）：建立连接时使用，三次握手中的同步请求标志。
- ACK（确认）：确认号有效标志，建立连接后所有数据包都应置 1。
- FIN（终止）：通知对方本端数据发送完毕，请求关闭连接（四次挥手）。
- RST（重置）：强制重置连接，用于异常连接终止（如端口不存在、连接非法）。
- PSH（推送）：指示接收方立即将数据推送给应用层，不要等缓冲区满（用于实时数据）。
- URG（紧急）：紧急指针字段有效，配合紧急指针使用，紧急数据优先处理。

窗口大小（Window Size，16位）：通告接收方的接收窗口大小（最大 65535 字节，可用 Window Scale 选项扩展）。

## UDP 特点、无连接、不可靠、面向数据报

无连接：发送数据前不需要建立连接（无三次握手），减少延迟。发送完毕无需四次挥手。每个数据报独立路由，可能走不同路径到达。

不可靠：

- 无序：数据报到达顺序可能与发送顺序不同（无序号机制）。
- 丢包不重传：网络丢包时 UDP 不感知，不会重传。
- 无流量控制/拥塞控制：发多快就多快，不管接收方是否跟得上，不管网络是否拥塞。

面向数据报：UDP 保留消息边界，每次 send 一个数据报，每次 recv 一个完整数据报，应用层可以区分每条消息（不存在 TCP
的粘包问题）。但每个数据报最大 64KB（受 IP 包大小限制）。

头部简单：仅 8 字节（源端口、目的端口、长度、校验和），开销极小。

校验和（可选）：可检测数据传输错误，但检测到错误只是丢弃数据报，不重传。

UDP 的价值：以上看似都是缺点，但正是这些特性让 UDP 有低延迟、高吞吐的优势。应用层可以在 UDP 基础上按需实现可靠性（如
QUIC），灵活度更高，比 TCP 更容易针对具体场景优化。

## 这块如果还有余力的话，可以学一学什么是QUIC

# kafka

# 基础知识

## Kafka 是什么？核心定位与核心价值是什么？

Kafka 是由 LinkedIn 开源、Apache 维护的分布式流处理平台，本质上是一个高吞吐量、持久化的发布-订阅消息队列。

核心定位：

1. 消息队列（Message Queue）：应用间异步解耦、削峰填谷。
2. 流处理平台（Streaming Platform）：实时数据管道，连接各个数据系统；配合 Kafka Streams/Flink 做实时计算。
3. 日志存储系统：顺序写磁盘+分布式副本，天然适合数据日志归档。

核心价值：

1. 极高吞吐量：顺序磁盘写+零拷贝+批量压缩，单机百万 TPS 级别。
2. 持久化可回溯：消息写入磁盘并保留（默认 7 天），支持按 offset 重放历史数据，这是 RabbitMQ 等传统 MQ 不具备的能力。
3. 水平扩展：分区（Partition）机制天然支持水平扩展，增加 Broker 和分区可线性提升吞吐。
4. 消费者组：支持广播（多个消费者组都收到）和点对点（同组竞争消费）两种模式。
5. 强大的生态：与 Spark、Flink、Hadoop、ES、MySQL 等生态无缝集成。

## Kafka 核心架构组成有哪些？各组件（Producer、Consumer、Broker等）核心作用？

- Producer（生产者）：向 Kafka 的指定 Topic 发送消息。可选择分区策略（指定 key 哈希取模、轮询、自定义）。支持批量发送、消息压缩（gzip/snappy/lz4）和
  ACK 确认机制（acks=0/1/all）。
- Consumer（消费者）：从 Kafka 拉取消息（Pull 模式，而非 Push）。消费者主动控制消费速率和 offset 提交，实现灵活的重试和回放。
- Consumer Group（消费者组）：同一 Topic 的分区按 1:1 或 N:1 分配给组内消费者。同组消费者竞争消费（点对点）；不同组各自消费所有消息（广播）。分区数量决定组内消费者的最大并发数。
- Broker：Kafka 集群中的服务节点，负责消息存储、分发和副本管理。每个 Broker 存储若干分区的 Leader 和 Follower 副本。
- Controller：Broker 中选举出的特殊角色，负责集群元数据管理：分区 Leader 选举、Broker 上下线处理、主题创建/删除等。
- Topic（主题）：消息的逻辑分类，生产者按 Topic 发消息，消费者按 Topic 订阅消费。
- Partition（分区）：Topic 的物理分片，消息在分区内有序（全局无序），实现水平扩展和并行消费。
- Replica（副本）：分区的冗余备份，分 Leader（处理读写）和 Follower（仅同步），保证高可用。
- ZooKeeper / KRaft：旧版本用 ZooKeeper 管理元数据（Controller 选举、Broker 注册、offset 存储）；Kafka 2.8+ 引入 KRaft
  模式，用自身内置的 Raft 共识算法替代 ZooKeeper，消除外部依赖。

## Topic、Partition、Replica 三者的核心关系是什么？各自的作用的是什么？

Topic（主题）：消息的逻辑分类标签，生产者向 Topic 发消息，消费者订阅 Topic 消费消息。Topic 是逻辑概念，不对应具体存储。

Partition（分区）：Topic 的物理分片，一个 Topic 可以有多个 Partition。

- 每个 Partition 是一个有序的、不可变的消息序列（Append-Only Log），消息按写入顺序追加。
- Partition 内消息有序，Partition 间消息无序（全局无序）。
- 作用：实现水平扩展（分区可分布在不同 Broker），支持并行生产和消费，提升吞吐量。
- 消费者组内的消费者数量上限由分区数决定（多余的消费者无分区可消费，闲置）。

Replica（副本）：每个 Partition 的冗余备份，分布在不同 Broker 上，数量由 replication.factor 决定。

- Leader Replica：负责处理该分区的所有读写请求，每个分区有且仅有一个 Leader。
- Follower Replica：仅从 Leader 同步数据，不处理客户端请求；Leader 宕机时，从 ISR 中选新 Leader。
- ISR（In-Sync Replicas）：与 Leader 保持同步的副本集合，只有 ISR 中的副本才有资格成为新 Leader。
- 作用：保证高可用，Broker 宕机时 Follower 顶替 Leader，不丢数据不丢服务。

三者关系：Topic 包含多个 Partition，每个 Partition 有多个 Replica（一主多从），Replica 分布在不同 Broker 上，实现扩展性和高可用的统一。

## Kafka 为什么吞吐量极高？核心优化机制有哪些？

1. 顺序磁盘写（Sequential I/O）：
   Kafka 的消息全部以追加方式写入日志文件（Append-Only Log），顺序写磁盘的速度可达 600MB/s+，远超随机写（约
   100KB/s）。没有随机寻道时间，磁盘 I/O 不是瓶颈。

2. 零拷贝（Zero Copy）：
   消费者读取数据时，Kafka 使用 sendfile() 系统调用（零拷贝），数据直接从 PageCache 通过 DMA 传输到网卡缓冲区，跳过了"
   内核缓冲区 → 用户空间 → 内核 Socket 缓冲区"的两次 CPU 拷贝，减少 CPU 开销和内存带宽消耗。

3. PageCache（操作系统页缓存）：
   Kafka 不维护自己的内存缓存，而是充分利用操作系统的 PageCache。数据写入时先写 PageCache（速度极快），由 OS 异步刷盘；读取时先从
   PageCache 读，缓存命中则无需磁盘 I/O。

4. 批量发送和压缩（Batching & Compression）：
   Producer 将多条消息合并为一个批次（batch）统一发送，减少网络请求次数；支持 gzip/snappy/lz4/zstd 压缩，降低网络传输量，单批次可包含数百条消息。

5. 分区并行（Partitioning）：
   多个 Partition 分布在不同 Broker，Producer 和 Consumer 可并行操作多个分区，线性扩展吞吐量。

6. 消息预取和批量拉取：
   Consumer 每次 poll() 拉取一批消息而非单条，减少网络往返次数。

## Kafka 适用场景与不适用场景分别是什么？

适用场景：

1. 日志采集与传输：各服务的日志统一发到 Kafka，由 Logstash/Fluentd 消费写入 ES，是经典日志管道场景。
2. 用户行为数据采集：APP 端埋点数据、点击流、曝光事件等高频数据流实时写入 Kafka。
3. 消息削峰/异步解耦：上游服务产生峰值流量，通过 Kafka 缓冲，下游服务按自身能力消费，避免雪崩。
4. 实时流处理：配合 Kafka Streams / Flink / Spark Streaming 对实时数据做聚合、过滤、转换，如实时风控、实时推荐。
5. 数据管道（Data Pipeline）：将数据库变更（CDC，如 Debezium 捕获 MySQL binlog）通过 Kafka 同步到数据仓库、搜索引擎。
6. 消息回放：历史数据按 offset 重放，支持数据重处理和修复。

不适用场景：

1. 要求严格消息顺序（全局顺序）：Kafka 只保证单分区有序，多分区无全局顺序，需单分区则吞吐下降。
2. 极低延迟要求（<1ms）：Kafka 批量处理天然有延迟，对超低延迟场景（如 HFT 高频交易）不适合。
3. 复杂路由规则：Kafka 不支持消息过滤、路由等复杂逻辑（RabbitMQ 更擅长），Kafka 的消费者需自己在业务层过滤。
4. 消息 TTL 精确控制：Kafka 的消息保留基于时间或大小，不支持单消息级别的精确延时投递（RocketMQ 的延时消息更合适）。
5. 需要事务性消息：RocketMQ 的事务消息支持 half-message 二阶段机制，Kafka 事务支持相对复杂。

## Kafka 和 RabbitMQ、RocketMQ 全方位对比区别？（吞吐量、可靠性、适用场景）

| 对比维度  | Kafka                  | RocketMQ             | RabbitMQ           |
|-------|------------------------|----------------------|--------------------|
| 吞吐量   | 极高（百万 TPS）             | 高（十万 TPS）            | 中（万级 TPS）          |
| 延迟    | 中（批量处理，ms-秒级）          | 低（ms 级）              | 低（μs-ms 级）         |
| 消息顺序  | 分区内有序，全局无序             | 分区内有序，支持全局顺序队列       | 队列内有序              |
| 消息可靠性 | 高（副本机制+持久化，acks=all）   | 高（同步刷盘+主从复制）         | 高（持久化+镜像队列）        |
| 延时消息  | 不原生支持                  | 支持（18 个延时等级）         | 通过插件/死信队列实现        |
| 事务消息  | 支持（复杂，Exactly-Once 语义） | 支持（half-message 二阶段） | 不原生支持              |
| 消息回放  | 支持（按 offset，保留期内随意回放）  | 支持（消费进度可重置）          | 不支持（消费即删除）         |
| 消息过滤  | 不原生支持（需业务层过滤）          | 支持（Tag/SQL 过滤）       | 支持（路由+binding key） |
| 协议    | 自研协议                   | 自研协议                 | AMQP 标准协议          |
| 语言    | Scala/Java             | Java                 | Erlang             |
| 社区活跃度 | 极高                     | 高（阿里维护）              | 高                  |
| 适用场景  | 大规模日志、流处理、数据管道         | 电商交易、金融支付、延时任务       | 复杂路由、企业集成、RPC      |

选型建议：

- 超大吞吐+数据回放+流处理：Kafka。
- 高可靠+事务+延时消息+电商场景：RocketMQ。
- 复杂路由+多语言支持+低延迟：RabbitMQ。

## Kafka 中的 Broker、Controller 是什么关系？Controller 的核心作用？

关系：Controller 是 Kafka 集群中的一个特殊 Broker。每个 Broker 都可以成为 Controller 候选，集群启动时通过 ZooKeeper 抢锁（或
KRaft 中的 Raft 选举）选出唯一的 Controller，负责集群管理职责。普通 Broker 负责分区数据的存储和读写，Controller Broker
在此基础上还承担集群控制面的职责。

Controller 的核心作用：

1. 分区 Leader 选举：当某个 Broker 宕机时，Controller 负责从 ISR 中为受影响的分区选举新 Leader，并将新的 Leader/ISR 信息通知给所有
   Broker（通过 LeaderAndISR 请求）。
2. Broker 上下线管理：监听 ZooKeeper 的 Broker 节点变化（或 KRaft 中的心跳），感知 Broker 加入和宕机，触发相应的 Leader
   重新选举和分区重新分配。
3. 主题和分区变更：处理主题创建/删除、分区扩容等元数据变更，更新所有 Broker 的元数据缓存。
4. ISR 变更管理：跟踪每个分区的 ISR 变化（Follower 落后则移出 ISR，追上则加回），更新 ZooKeeper 或 KRaft 中的 ISR 记录。

## Kafka 的 Zookeeper 架构与 KRaft 架构区别？为什么新版弃用 Zookeeper？

ZooKeeper 架构（Kafka 2.8 之前）：

- Kafka 依赖外部 ZooKeeper 集群存储元数据（Broker 注册、Topic 分区信息、ISR、Consumer offset 等）。
- Controller 通过 ZooKeeper 选举（抢临时节点），监听 ZooKeeper 节点变化感知集群状态。
- 问题：部署复杂（需要额外维护 ZooKeeper 集群）；ZooKeeper 成为性能瓶颈和单点故障风险；元数据操作需要跨系统通信，延迟高；分区数量受
  ZooKeeper 存储能力限制（百万分区时 ZooKeeper 压力极大）。

KRaft 架构（Kafka 2.8+ 引入，3.0+ 生产可用）：

- 用 Kafka 自身实现的 Raft 共识算法替代 ZooKeeper，元数据存储在 Kafka 内部的元数据主题（__cluster_metadata）中。
- Controller 通过 Raft 选举，多个 Controller 节点形成 Raft 集群（一主多从），主 Controller 处理所有元数据变更。
- 元数据变更以 Kafka 消息形式持久化，其他 Broker 通过消费元数据主题同步最新状态，无需实时向 Controller 请求。

KRaft 的优势：

1. 消除 ZooKeeper 依赖：部署、运维、监控更简单，减少一个中间件。
2. 更高的分区规模支持：去掉 ZooKeeper 限制后，理论支持百万级分区数。
3. 更快的 Controller 切换：Raft 选举比 ZooKeeper 临时节点更快，故障恢复时间从分钟级降到秒级以内。
4. 元数据读取更高效：Broker 本地缓存元数据，无需每次向 ZooKeeper 查询。

# 消息可靠性

## Kafka 消息丢失可能发生在哪些环节？每个环节的丢失原因是什么？

生产者端丢失：

- acks=0：生产者发完不等待任何确认，Broker 未收到或未写入就丢失。
- acks=1：只等 Leader 确认，Leader 写入后立即返回成功，但若 Leader 在 Follower 同步前崩溃，消息丢失。
- 发送失败未重试：网络抖动导致发送失败，若没有开启重试（retries=0）或重试耗尽，消息丢失。
- 消息序列化失败：消息数据异常导致序列化失败，消息被静默丢弃。

Broker 端丢失：

- 异步刷盘（默认）：消息写入 PageCache 后即返回成功，若 Broker 崩溃时 PageCache 中的数据未刷盘，消息丢失。
- 副本未同步时 Leader 宕机：acks=1 时，新选出的 Leader 可能没有最新消息。
- 未配置足够副本数：replication.factor=1 时，Broker 磁盘损坏则数据永久丢失。

消费者端丢失：

- 自动提交 offset（enable.auto.commit=true）：消息拉取后自动提交 offset，但还未处理完业务时，消费者崩溃重启，offset
  已提交，消息"逻辑丢失"（不会重新消费）。
- 消费后处理失败：拉取消息后业务处理抛异常，但 offset 已提交（无论自动还是手动），消息不会重试，业务逻辑上消息丢失。
- 多线程消费乱序提交：并发消费时较小序号的消息处理完先提交，导致较大序号未处理的消息 offset 被跳过。

## 如何全方位保证 Kafka 消息不丢失？生产者、Broker、消费者各环节如何优化？

生产者端：

1. acks=all（-1）：等待所有 ISR 副本确认，最强可靠性保证，Leader 和所有同步副本都写入才确认。
2. retries 设置充足（如 Integer.MAX_VALUE）+ retry.backoff.ms 合理退避，失败后自动重试。
3. 开启幂等生产者（enable.idempotence=true）：Kafka 为每条消息分配 ProducerID + 序列号，Broker
   自动去重，避免重试导致重复，同时保证消息不丢（At-Least-Once → Exactly-Once）。
4. 异步发送回调：不要用 fire-and-forget，在 callback 中处理发送失败，记录日志或触发告警。

Broker 端：

1. replication.factor ≥ 3：至少 3 副本，保证 1-2 个 Broker 宕机数据不丢。
2. min.insync.replicas ≥ 2：acks=all 时要求至少 2 个 ISR 副本写入成功才算成功，防止只有 Leader 在 ISR 时退化为 acks=1。
3. unclean.leader.election.enable=false（默认 false）：禁止非 ISR 副本成为 Leader，避免数据回退（宁可暂时不可用，也不丢消息）。

消费者端：

1. enable.auto.commit=false：关闭自动提交 offset，改为手动提交。
2. 先消费业务、后提交 offset：确保业务逻辑执行成功后再调用 commitSync() 或 commitAsync() 提交 offset。
3. 幂等消费：即使重复消费（消费后 offset 提交前崩溃导致重复），业务逻辑需要幂等处理，防止副作用。

## Kafka 为什么会出现消息重复消费？常见场景有哪些？

Kafka 默认提供 At-Least-Once（至少一次）语义，即消息不会丢，但可能重复消费。根本原因：消费者消费消息与提交 offset
不是原子操作，中间发生异常/重启，就会重复消费。

常见场景：

1. 消费者消费消息后，在提交 offset 之前崩溃重启：重启后从上次提交的 offset 开始消费，已消费的消息再次被消费。
2. 自动提交 offset，但消费处理时间过长超过 auto.commit.interval.ms：下次 poll 时自动提交了 offset，但若消费者在此期间崩溃，重启后仍从之前的
   offset 消费，重复消费。
3. Rebalance（再均衡）导致重复：消费者组发生 rebalance（成员加入/离开、分区变更），新接手分区的消费者从最近提交的 offset
   开始消费，可能覆盖前一消费者已处理但未提交的消息段。
4. 消息处理慢，心跳超时：消费者处理消息时间过长（超过 max.poll.interval.ms），被 Coordinator 认为已宕机，触发
   rebalance，当前消费者被踢出组，该分区被其他消费者重新消费。
5. 生产者重试导致重复：acks=1 时，Broker 写入成功但 ACK 丢失，Producer 重试发送，导致 Broker 收到重复消息，消费者重复消费（开启幂等
   Producer 可解决此问题）。

## 消息重复消费的解决方案是什么？业务层如何实现幂等性？

Kafka 层面的优化（减少重复概率，但无法完全消除）：

1. 手动提交 offset（enable.auto.commit=false）：确保业务处理成功后再提交，减少重复范围。
2. 开启幂等生产者（enable.idempotence=true）：解决生产者重试导致的重复消息。
3. Kafka 事务（Exactly-Once Semantics）：生产者 + 消费者事务保证恰好一次，但实现复杂，性能有损耗，适合对重复敏感的场景。

业务层幂等性实现（最通用、最重要）：
无论 Kafka 如何优化，消费逻辑本身必须设计为幂等的。常见方案：

1. 唯一消息 ID 去重（推荐）：
   每条消息带唯一 ID（消息生产时生成 UUID 或业务 ID）；消费时先查 Redis/DB 是否已处理该 ID，已处理则跳过；处理完后记录
   ID（设置合理 TTL）。适合大多数场景。

2. 数据库唯一约束：
   消费时执行 INSERT IGNORE 或 INSERT ON DUPLICATE KEY UPDATE，利用数据库唯一索引天然去重，重复插入时不报错只忽略。适合写数据库的消费场景。

3. 乐观锁版本号：
   消费更新时携带版本号（UPDATE ... WHERE version = old_version），重复消费时版本不匹配，UPDATE 影响行数为 0，幂等处理。

4. 状态机设计（幂等的状态转换）：
   业务对象的状态只能按特定方向流转（如订单：待支付→已支付），重复触发相同状态转换时检查当前状态，已是目标状态则跳过，防止重复处理副作用。

## Kafka 如何保证消息的顺序性？分区内有序与全局有序的区别？

分区内有序（Kafka 原生支持）：

- 同一分区内的消息严格按照写入顺序排列，Consumer 从同一分区拉取消息的顺序与写入顺序完全一致。
- 保障机制：分区内消息有单调递增的 offset；生产者写同一分区时顺序追加；单分区只有一个 Consumer Group 内的消费者消费。

实现分区内有序的关键：

- 相关消息必须发送到同一分区：Producer 发送时指定相同的 key（如订单 ID），Kafka 按 key 哈希取模路由到固定分区，同一 key
  的消息始终进同一分区。
- 同一分区只有一个消费者线程串行消费：Consumer Group 内一个分区只分配给一个消费者，消费者串行处理（不要多线程并发消费同一分区消息，否则乱序）。
- 生产者幂等+有序（enable.idempotence=true）：开启幂等后，Kafka
  保证同一分区的消息按序写入，不会因重试导致乱序（max.in.flight.requests.per.connection=1 配合幂等）。

分区间无序（全局无序）：
不同分区的消息是并行写入的，Consumer 并行消费不同分区，全局顺序无法保证。

## 为什么多分区无法保证全局有序？如何实现全局有序（特殊场景）？

为什么多分区无法全局有序：

1. 多个分区并行写入：不同 Producer 同时向多个分区写入，各分区消息的全局时间顺序无法确定。
2. 消费者并行消费：多个 Consumer 并发消费不同分区，即使各分区内有序，合并后全局顺序丢失。
3. 网络延迟不同：不同分区的消息可能通过不同 Broker 传输，到达 Consumer 的延迟不同，进一步打乱顺序。

如何实现全局有序（代价是吞吐量大幅下降）：

1. 只使用 1 个分区：所有消息写入单一分区，天然全局有序。但完全失去并行能力，分区对应一个消费者，吞吐量等同于单线程处理，几乎不用于生产（只适合极低并发的特殊场景）。
2. Consumer 端合并排序：多个分区的消息在 Consumer 端按时间戳（或消息序号）合并排序，消费端做重排，复杂度高，且需要等待所有分区的消息到达再排序（增加延迟）。
3. 业务层顺序控制：不依赖 Kafka 保证顺序，而是在业务层通过版本号、时间戳或消息序列号实现乱序容忍和业务顺序保证（最常用的工程实践）。

实际经验：绝大多数业务只需要"同一业务实体的消息有序"（如同一订单的状态变更消息有序），而不是所有消息全局有序，用 key
哈希到固定分区的方案完全满足需求，无需牺牲吞吐量。

## 消息乱序的常见原因是什么？如何避免消息乱序？

常见乱序原因：

1. 消息发送到不同分区：未指定 key 或 key 变化，导致相关消息分散到多个分区，消费时顺序不可控。
2. 生产者重试导致乱序：max.in.flight.requests.per.connection > 1 时，多个 in-flight
   请求中，先发的请求失败重试，后发的请求已成功，造成乱序（先发的消息反而后入分区）。
3. Consumer 多线程并发处理：同一分区的消息被多个线程并发处理，处理顺序不确定。
4. 消费者 Rebalance：Rebalance 后分区被不同消费者接管，新消费者从提交的 offset 开始，可能乱序或重复。

如何避免消息乱序：

1. 相关消息使用固定 key 发送到同一分区（最根本方案）。
2. 开启幂等 Producer + 限制并发飞行数：enable.idempotence=true 同时
   max.in.flight.requests.per.connection=1，确保重试不乱序（但会降低吞吐量）。
3. Consumer 单线程串行消费同一分区（不要多线程并行消费同一分区消息）。
4. 若必须多线程消费：相同 key 的消息通过 hash 分配到同一线程处理（内存队列按 key 分发），保证同 key 有序。

## Kafka 消息积压的常见原因是什么？（消费端、生产端、Broker 端）

消费端原因（最常见，占 90% 以上）：

1. 消费速度太慢：消费者处理逻辑耗时过长（如调用慢接口、复杂计算、数据库慢查询），导致消费 TPS 远低于生产 TPS。
2. 消费者异常宕机：消费者进程崩溃或 OOM，分区暂时无消费者（rebalance 重分配前有延迟）。
3. 消费者数量不足：Consumer Group 中的消费者数量少于分区数，部分分区消费压力集中，处理能力不足。
4. 消费者线程阻塞：消费线程被锁阻塞、数据库连接池耗尽、下游服务超时，消费吞吐下降为 0。
5. 手动提交 offset 频率过低：每次处理大批量消息后才提交，中间出现异常重新消费，造成积压放大。

生产端原因：

1. 流量突增：业务高峰（如秒杀、促销）瞬间产生大量消息，消费端来不及消化。
2. 批量重放：运维或开发将大量历史数据重新发入 Kafka，瞬间产生海量消息。

Broker 端原因：

1. Broker 性能问题：磁盘 I/O 打满、网络带宽耗尽，导致消息存储和传输速度下降，消费者拉取延迟增大。
2. 副本同步慢：ISR 同步压力大，影响 acks=all 的生产 ACK 时间，间接影响消费（实际对消费影响较小）。
3. 分区 Leader 频繁切换：Broker 抖动导致 Leader 频繁选举，Consumer 需要重新发现 Leader，短暂无法消费。

## 线上消息积压的完整排查步骤是什么？如何定位问题根源？

1. 确认积压存在和规模：
    - 监控看板：查看各 Topic 各分区的 Consumer Group Lag（offset 差值），确认哪些分区积压最严重、积压量级（万条还是百万条）。
    - 命令行：kafka-consumer-groups.sh --describe --group <group-id> 查看每个分区的 LAG。

2. 判断积压趋势（增加/稳定/减少）：
    - 持续增加：消费速度 < 生产速度，问题仍在恶化。
    - 稳定但有大量积压：消费速度 ≈ 生产速度，但历史积压无法消化。
    - 正在减少：问题已在自动恢复中。

3. 查看消费者健康状态：
    - 检查 Consumer 进程是否存活，jstack 是否有线程阻塞或死锁。
    - 查看消费者日志，是否有异常抛出（数据库错误、超时、OOM 等）。
    - 查看 GC 日志，是否频繁 Full GC 导致 STW 暂停消费。

4. 分析消费者处理性能：
    - 监控单条消息平均处理时间，找慢操作（慢 SQL、HTTP 接口超时、锁竞争）。
    - 查看消费者的 JVM 资源（CPU、内存）是否达到瓶颈。

5. 检查 Broker 状态：
    - Broker 磁盘 I/O、网络带宽是否打满。
    - 是否有 Broker 宕机/ISR 缩减。

6. 查看生产端是否有异常流量：
    - 是否有批量数据导入或异常重放。

## 解决消息积压的最优方案有哪些？不同场景如何选择？

1. 增加消费者数量（最快见效）：
   水平扩展消费者实例（增加 Pod/服务器），前提是消费者数量 ≤ 分区数，否则多余消费者无法获得分区。适合消费者 CPU/网络资源不足的场景。

2. 增加分区数 + 增加消费者（扩展并发度）：
   当前分区数限制了消费并发，先扩分区，再扩消费者。注意：已有消息不会重新分布到新分区，只有新消息才会路由到新分区，对存量积压消化效果有限。

3. 临时消费者快速清空积压（应急）：
   如果积压的消息允许并行消费（无顺序要求），临时增加多倍消费者实例，快速消化积压；消化完后恢复正常数量。

4. 修复消费者处理慢的根因：
   这是最根本的方案：优化慢 SQL、接入缓存减少 DB 调用、修复下游服务超时、减少消费逻辑复杂度。

5. 消费者内部异步化：
   消费者拉取消息后异步处理（线程池），不等待结果再拉下一批，提升拉取频率；但需注意 offset 提交顺序，防止消息丢失。

6. 降级/跳过不重要消息（极端场景）：
   如果积压消息中有大量低优先级/已过期消息，通过调整 Consumer offset 跳过历史积压，从最新 offset 开始消费（会丢失积压中的消息，慎用）。

选择建议：

- 紧急处理积压：方案 1（快速扩消费者）。
- 系统性能根因：方案 4（修复处理瓶颈）。
- 长期扩展能力：方案 2（扩分区+消费者）。

## 增加消费者数量能解决积压吗？为什么不能超过分区数量？

增加消费者可以解决积压，但有上限：

- 有效：当消费者数量 < 分区数时，增加消费者可以让每个消费者处理更少的分区，提升整体消费并发度，有效加速积压消化。
- 无效（超过分区数后）：当消费者数量 ≥ 分区数时，多余的消费者无法获得任何分区分配（Kafka
  的消费者组分区分配策略不允许一个分区同时被多个消费者消费），它们处于完全闲置状态，对积压消化无任何贡献。

为什么不能超过分区数：
Kafka 的核心设计原则：一个分区在同一个消费者组内，最多只能被一个消费者实例消费。原因：

1. 保证分区内消息的有序消费：如果多个消费者同时消费同一分区，消息并发处理顺序不可控。
2. 避免重复消费：同一条消息只能被组内一个消费者处理。
3. 简化 offset 管理：每个分区有唯一的 committed offset，一个分区对应一个消费者，offset 管理简单清晰。

结论：消费者数量等于分区数时并发度最高，超过无意义。真正需要更高并发，必须先增加分区数。

## 分区数量的设置原则是什么？过多或过少会有什么问题？

设置原则：

1. 根据目标吞吐量估算：分区数 ≈ 目标 TPS / 单分区 TPS（单分区生产或消费的极限 TPS，一般在 10MB/s 左右，根据消息大小和机器配置测试得出）。
2. 不少于消费者实例数：保证每个消费者都能分配到分区，无闲置消费者。
3. 考虑生产并发度：生产者可并发写多个分区，分区数也是生产端的并行度上限。
4. 适当冗余：考虑未来业务增长，可以适当多设一些（Kafka 只支持增加分区，不支持减少）。
5. 常见参考：中小规模业务 6-12 个分区，大规模 50-100+ 分区，根据实际压测调整。

分区过少的问题：

- 消费并发度受限，处理能力上限低。
- 生产端写入 TPS 受限（分区是写入的并行单元）。
- 积压时无法通过增加消费者扩展处理能力。

分区过多的问题：

- 每个分区在 Leader Broker 上有独立的文件句柄，分区过多导致 Broker 文件描述符耗尽（OS 限制）。
- Controller 需要维护所有分区的元数据，分区过多（如百万级）时 Controller 和 ZooKeeper 压力极大（KRaft 模式缓解了这个问题）。
- Leader 选举时，分区越多，Controller 处理的选举任务越多，Broker 宕机后恢复时间越长。
- 消息 Rebalance 时间增加：分区多则 Consumer Group 再平衡时需要处理更多分区分配，耗时增加。
- 端对端延迟增加：分区多时 Producer 批量发送需要管理更多批次缓冲区，内存和延迟开销增加。

## 如何避免消息积压？日常运维需要注意哪些点？

预防措施（架构层面）：

1. 合理设置分区数：根据业务峰值 TPS 预留足够的分区数，保证消费者数量有扩展空间。
2. 消费者设计为无状态+快速处理：消费逻辑尽量轻量（只做数据转换/转发），耗时操作异步化，避免同步等待下游。
3. 压测确定消费 TPS 上限：在上线前通过压测确认消费者的最大 TPS，与生产端的峰值 TPS 对比，确保有足够余量（建议消费 TPS ≥ 生产峰值
   TPS × 2）。
4. 消费者横向扩展能力：架构设计为无状态，可以快速水平扩展消费者实例。

日常监控（运维层面）：

1. 监控 Consumer Lag：为每个关键 Topic + Consumer Group 配置 Lag 报警，超过阈值立即告警（如 Lag > 10000 预警，Lag > 100000
   严重报警）。
2. 监控生产者发送速率 vs 消费者消费速率：对比两者趋势，提前发现差距扩大的趋势。
3. 监控 Broker 健康：磁盘使用率、网络吞吐、ISR 缩减、Leader 选举次数。
4. 定期清理过期消息：确认 retention.ms 和 retention.bytes 配置合理，避免磁盘撑满。
5. 消费失败处理：配置死信队列（DLQ）或重试机制，失败消息不阻塞主流程，定期处理死信队列中的异常消息。
6. Rebalance 监控：频繁 Rebalance 会导致消费暂停，监控 Rebalance 频率，排查原因（消费超时、消费者不稳定）。

# Redis八股

//TODO

# MySQL八股

//TODO


















































































































