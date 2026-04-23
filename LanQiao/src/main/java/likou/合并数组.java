package likou;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并数组
 * @Date：2025/1/21 21:04
 * @Filename：合并数组
 */
public class 合并数组 {

    public static void main(String[] args) {
        int[] num1 = {4, 5, 6, 0, 0, 0};
        int[] num2 = {1, 2, 3};
        merge(num1, 3, num2, 3);
        for (int i : num1) {
            System.out.print(" " + i + " ,");
        }
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = m,j=0; i < nums1.length; i++,j++) {
            nums1[i]=nums2[j];
        }
        Arrays.sort(nums1);
    }
}
