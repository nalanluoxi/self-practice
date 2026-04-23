package example.java2.MyBloomFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyBloomFilter
 * @Project：LanQiaoBei
 * @name：BloomFilterTests
 * @Date：2025/5/15 20:10
 * @Filename：BloomFilterTests
 */
public class BloomFilterTests {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        Random random = new Random(l);
        ExpandBloomFilter filter = new ExpandBloomFilter(1000000, 10);
        List<Integer> list = new ArrayList<>();

        int test=10000;
        for (int i = 0; i < test; i++) {
            int temp = random.nextInt();
            filter.add(temp);
            list.add(temp);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            boolean check = filter.check(list.get(i));
            if (check){
                //System.out.println(list.get(i)+"存在");
            }else {
                System.out.println(list.get(i)+"不存在");
            }
        }
        long last = System.currentTimeMillis();
        System.out.println("start: "+start);
        System.out.println("last: "+last);
        System.out.println("time: "+(last-start));

    }
}
