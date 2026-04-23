package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：摆动序列
 * @Date：2025/3/11 20:41
 * @Filename：摆动序列
 */
public class 摆动序列 {
    public static void main(String[] args) {
        int[] nums = {3,3,3,2,5};
        System.out.println(wiggleMaxLength(nums));
    }

    public static int wiggleMaxLength(int[] nums) {
        if (nums.length <= 1) return nums.length;
        int ans = 1;
        int pre = 0;
        int now = 0;
        for (int i = 1; i < nums.length; i++) {
            now = nums[i] - nums[i - 1];
            if (now*pre<0){
                ans++;
                pre=now;
            }
        }

        return ans;
    }
}
