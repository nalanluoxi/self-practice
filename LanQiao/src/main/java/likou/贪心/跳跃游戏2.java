package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：跳跃游戏2
 * @Date：2025/3/12 21:03
 * @Filename：跳跃游戏2
 */
public class 跳跃游戏2 {
    public static void main(String[] args) {
        //int[] nums = {2,3,0,1,4};
        int[] nums = {2,0,2,4,6,0,0,3};
        //int[] nums = {2,3,1,1,4};
        System.out.println(jump(nums));
    }


    public static int jump(int[] nums) {
        if (nums.length==1){
            return 0;
        }
        int ans=0;
        int nowMax=0;
        int nextMax=0;

        for (int i = 0; i < nums.length; i++) {
            nextMax=Math.max(nextMax,i+nums[i]);
            if (i==nowMax){
                ans++;
                nowMax=nextMax;
                if (nextMax>=nums.length-1){
                    break;
                }
            }
        }
        return ans;
    }


}
