package Test2;

public class Test {
    public static void main(String[] args) {
        Integer i1 = 100;
        Integer i2 = 100;
        System.out.println(i1 == i2); // true 缓存复用

        Integer i3 = 200;
        Integer i4 = 200;
        System.out.println(i3 == i4); // false 新建对象
    }
}
