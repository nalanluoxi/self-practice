package likou.LFU算法;

/**
 * @Author 纳兰洛熙
 * @Package：likou.LFU算法
 * @Project：LanQiaoBei
 * @name：MyLFUTest
 * @Date：2025/5/18 17:01
 * @Filename：MyLFUTest
 */
public class MyLFUTest {
    public static void main(String[] args) {
        MyLFU myLFU=new MyLFU(5);
        myLFU.set("1","1");
        myLFU.set("2","2");
        myLFU.set("3","3");
        myLFU.set("4","4");
        myLFU.set("5","5");
        System.out.println(myLFU.get("1"));
        System.out.println(myLFU.get("2"));
        System.out.println(myLFU.get("1"));
        System.out.println(myLFU.get("1"));
        System.out.println(myLFU.get("2"));
        System.out.println(myLFU.get("4"));
        System.out.println(myLFU.get("5"));
    }
}
