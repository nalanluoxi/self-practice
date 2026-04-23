package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：跳跃游戏2
 * @Date：2025/7/10 15:51
 * @Filename：跳跃游戏2
 */
public class 跳跃游戏2 {
    public static void main(String[] args) {

    }

    public static int jump(int[] nums) {
        if (nums.length==1){
            return 0;
        }
        int ans=0;
        int nowmax=0;
        int nextMax=0;
        for (int i = 0; i < nums.length; i++) {
            nextMax=Math.max(nextMax,i+nums[i]);
            if (i==nowmax){
                ans++;
                nowmax=nextMax;
                if (nextMax>= nums.length-1){
                    break;
                }
            }
        }

        return ans;
    }
}
