package likou.力扣test2.LRU;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2.LRU
 * @Project：LanQiaoBei
 * @name：LRUTest
 * @Date：2025/7/1 23:12
 * @Filename：LRUTest
 */
public class LRUTest {
    public static void main(String[] args) {
        MyLRU myLRU = new MyLRU(2);
        myLRU.put(1, 1);
        myLRU.put(2, 2);
        System.out.println(myLRU.get(1));
        myLRU.put(3, 3);
        System.out.println(myLRU.get(2));
        myLRU.put(4, 4);
        System.out.println(myLRU.get(1));
        System.out.println(myLRU.get(3));
        System.out.println(myLRU.get(4));
    }
}
