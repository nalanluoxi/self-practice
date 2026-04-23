package example.java2.MyHashMapTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.MyHashMapTest
 * @Project：LanQiaoBei
 * @name：TestHashMap
 * @Date：2025/4/20 17:40
 * @Filename：TestHashMap
 */
public class TestHashMap {
    public static void main(String[] args) {
        MyHashMap<Integer,Integer> map = new MyHashMap<>();
        map.put(12,12);
        map.put(16, 16);
        map.put(13, 16);
        map.put(14, 16);
        map.put(15, 16);
        map.put(17, 16);
        map.put(18, 16);
        map.put(19, 16);
        map.put(10, 16);
        map.put(11, 16);
        map.put(1, 16);
        map.put(2, 16);
        map.put(3, 16);
        map.put(4, 16);
        map.put(5, 16);
        System.out.println(map.size());
        Set<Integer> set = new HashSet<>();
        Map<Integer,Integer> map1 = new HashMap<>();
    }
}
