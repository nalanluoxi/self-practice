package 蓝桥杯真题.十四届国赛;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.十四届国赛
 * @Project：LanQiaoBei
 * @name：基因组合
 * @Date：2025/6/14 22:00
 * @Filename：基因组合
 */
public class 基因组合 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        String string = scanner.nextLine();
        System.out.println(string);
        String[] split = string.split(" ");
        /*String[] split = scanner.nextLine().split(" ");*/
        int[] nums = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            nums[i] = Integer.parseInt(split[i]);
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int i = nums.length - 1;
        for (int j = 0; j < nums.length-1; j++) {
            int i1 = nums[i] ^ nums[j];
            //System.out.println("i: " + nums[i] + "  j: " + nums[j] + "  " + i1);
            min = Math.min(min, i1);
            max = Math.max(max, i1);
        }

        System.out.println(min + " " + max);
    }
}
