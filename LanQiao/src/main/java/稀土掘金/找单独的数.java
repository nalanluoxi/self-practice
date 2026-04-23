package 稀土掘金;

import java.util.HashMap;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：找单独的数
 * @Date：2024/12/19 16:15
 * @Filename：找单独的数
 */
public class 找单独的数 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 1, 2, 2, 3, 3, 4, 5, 5}) == 4);
    }

    public static int solution(int[] cards) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int temp : cards) {
            if (map.containsKey(temp)){
                map.put(temp, map.get(temp)+1);
            }
            map.put(temp,1);
        }
        for (Integer i : map.keySet()) {
            if (map.get(i)==1){
                return i;
            }
        }
        return -1;
    }




}
