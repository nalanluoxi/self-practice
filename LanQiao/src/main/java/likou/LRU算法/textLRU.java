package likou.LRU算法;

/**
 * @Author 纳兰洛熙
 * @Package：likou.LRU算法
 * @Project：LanQiaoBei
 * @name：textLRU
 * @Date：2025/5/12 17:18
 * @Filename：textLRU
 */
public class textLRU {
    public static void main(String[] args) {
        MyLRU myLRU=new MyLRU(5);
        myLRU.put(1,1);
        myLRU.put(2,2);
        myLRU.put(3,3);
        myLRU.put(4,4);
        myLRU.put(5,5);
        System.out.println(myLRU.get(1));
        System.out.println(myLRU.get(2));
    }
}
