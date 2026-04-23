package 稀土掘金;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toSet;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：第三大
 * @Date：2025/1/13 20:24
 * @Filename：第三大
 */
public class 第三大 {


    public static void main(String[] args) {
        System.out.println(solution(3, new int[]{3, 2, 1}) == 1);
        System.out.println(solution(2, new int[]{1, 2}) == 2);
        System.out.println(solution(4, new int[]{2, 2, 3, 1}) == 1);


    }


    public static int solution(int n, int[] nums) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        Arrays.sort(nums);
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        if (set.size()<=2){
            Object[] array = set.toArray();
            return (Integer)array[array.length-1];
        }else {
            Object[] array = set.toArray();
            return (Integer)array[array.length-3];
        }
    }
}
