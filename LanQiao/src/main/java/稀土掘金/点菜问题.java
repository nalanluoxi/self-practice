package 稀土掘金;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：点菜问题
 * @Date：2025/1/15 9:19
 * @Filename：点菜问题
 */
public class 点菜问题 {


    public static void main(String[] args) {
        long solution = solution("100111110010000", new int[]{3, 4, 9, 13, 16, 4, 3, 9, 1, 11, 7, 7, 4, 4, 11}, 1, 4);
        System.out.println(solution);
    }

   /* public static long solution(String s, int[] a, int m, int k) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < a.length; i++) {
            map.put(a[i], Integer.valueOf(s.charAt(i) - 48));
        }
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey());
        int allmoney=0;
        for (Integer i : map.keySet()) {
            if (k==0){
                break;
            }
            k--;
            if (map.get(i)==1){
                m--;
            }
            allmoney+=i;
        }
        System.out.println("k:"+k+" m:"+m+" allmoney:"+allmoney);
        map.forEach((key,v)->{
            System.out.println(key+" "+v);
        });
        if (m<0||k!=0){
            return -1;
        }
        return allmoney;
    }*/


    public static long solution(String s, int[] a, int m, int k) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        Map<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < a.length; i++) {
            map.put(a[i], Integer.valueOf(s.charAt(i) - 48));
        }

        // 将菜品分为两类：含有蘑菇的和不含有蘑菇的
        List<Integer> mushroomPrices = new ArrayList<>();
        List<Integer> nonMushroomPrices = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                mushroomPrices.add(entry.getKey());
            } else {
                nonMushroomPrices.add(entry.getKey());
            }
        }

        // 对两类菜品分别排序
        Collections.sort(mushroomPrices);
        Collections.sort(nonMushroomPrices);

        int allmoney = 0;
        int mushroomCount = 0;

        // 优先选择不含蘑菇的菜品
        for (int price : nonMushroomPrices) {
            if (k == 0) break;
            allmoney += price;
            k--;
        }

        // 如果还需要选择菜品，选择含有蘑菇的菜品
        for (int price : mushroomPrices) {
            if (k == 0) break;
            if (mushroomCount < m) {
                allmoney += price;
                k--;
                mushroomCount++;
            }
        }

        // 如果无法满足条件，返回 -1
        if (k != 0) {
            return -1;
        }

        return allmoney;
    }
}
