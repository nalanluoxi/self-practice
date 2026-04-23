package likou.力扣test2;


import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：可获得最大点数
 * @Date：2025/7/14 10:22
 * @Filename：可获得最大点数
 */
public class 可获得最大点数 {

    public static int maxScore(int[] nums, int k) {
        int winsize=nums.length-k;
        int winsum=0;
        for (int i = 0; i < winsize; i++) {
            winsum+=nums[i];
        }
        int minsum=winsum;
        for (int i = winsize; i < nums.length; i++) {
            winsum=winsum+nums[i]-nums[i-winsize];
            minsum=Math.min(minsum,winsum);
        }
        int sum=0;
        for (int num : nums) {
            sum+=num;
        }
        return sum-minsum;
    }
}
