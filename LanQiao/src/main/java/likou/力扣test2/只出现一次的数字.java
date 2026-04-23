package likou.力扣test2;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：只出现一次的数字
 * @Date：2025/7/3 21:11
 * @Filename：只出现一次的数字
 */
public class 只出现一次的数字 {
    public static void main(String[] args) {
        int[]num={4,1,2,1,2};
        System.out.println(2^2);
        System.out.println(2|2);
        System.out.println();

        System.out.println(singleNumber(num));
    }
    public static int singleNumber(int[] nums) {
        int ans=0;
        for (int num : nums) {
            ans^=num;
        }
        return ans;
    }


}
