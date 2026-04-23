package 稀土掘金;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：倒排索引
 * @Date：2025/1/14 22:48
 * @Filename：倒排索引
 */
public class 倒排索引 {


    public static void main(String[] args) {
        System.out.println(solution(Arrays.asList(1, 2, 3, 7), Arrays.asList(2, 5, 7)).equals(Arrays.asList(7, 2)));
        System.out.println(solution(Arrays.asList(1, 4, 8, 10), Arrays.asList(2, 4, 8, 10)).equals(Arrays.asList(10, 8, 4)));

    }

    public static List<Integer> solution(List<Integer> a, List<Integer> b) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        Collections.sort(a);
        Collections.sort(b);
        HashMap<Integer,Integer> map = new HashMap<>();
        for (Integer i : a) {
            map.put(i,1);
        }
        for (Integer i : b) {
            if (map.containsKey(i)){
                map.put(i,map.get(i)+1);
            }else {
                map.put(i, 1);
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        map.forEach((k,v)->{
            if (v>1){
                list.add(k);
            }
        });
        Collections.sort(list,new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        return list;
    }



}
