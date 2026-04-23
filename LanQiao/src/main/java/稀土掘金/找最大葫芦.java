package 稀土掘金;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：找最大葫芦
 * @Date：2024/12/21 21:37
 * @Filename：找最大葫芦
 */


public class 找最大葫芦 {

    public static int[] solution(int n, int max, int[] array) {
        // Edit your code here
        int[] nums = new int[14];
        Arrays.stream(array).forEach(x -> nums[x]++);
        List<Integer> overThree = new ArrayList<>();
        List<Integer> two = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= 3) {
                overThree.add(i);
                if (!two.contains(i)) {
                    two.add(i);
                }
            }
            if (nums[i] == 2) {
                if (!two.contains(i)) {
                    two.add(i);
                }
            }
        }
        int[] res = {0, 0};
        for (int i = 0; i < overThree.size(); i++) {
            Integer three = overThree.get(i);
            for (int j = 0; j < two.size(); j++) {
                Integer tw = two.get(j);
                if (tw == three) {
                    continue;
                }
                if (tw * 2 + three * 3 > max) {
                    continue;
                }
                res = getMax(new int[]{three, tw}, res);
            }
        }
        return res;
    }

    public static int[] getMax(int[] num, int[] res) {
        int n1 = num[0];
        int n2 = num[1];

        int r1 = res[0];
        int r2 = res[1];

        if (n1 == 1) {
            n1 = 20;
        }
        if (n2 == 1) {
            n2 = 20;
        }
        if (r1 == 1) {
            r1 = 20;
        }
        if (r2 == 1) {
            r2 = 20;
        }


        if (n1 > r1) {
            return num;
        }

        if (n1 < r1) {
            return res;
        }

        if (n1 == r1 && n2 > r2) {
            return num;
        }

        if (n1 == r1 && n2 < r2) {
            return res;
        }

        return null;
    }

    public static void main(String[] args) {
        // Add your test cases here

        System.out.println(java.util.Arrays.equals(solution(9, 37, new int[]{9, 9, 9, 9, 6, 6, 6, 6, 13}), new int[]{8, 5}));
        //  System.out.println(java.util.Arrays.equals(solution(9, 37, new int[]{9, 9, 9, 9, 6, 6, 6, 6, 13}), new int[]{6, 9}));
        // System.out.println(java.util.Arrays.equals(solution(9, 40, new int[]{1, 11, 13, 12, 7, 8, 11, 5, 6}), new int[]{0, 0}));
    }

}
