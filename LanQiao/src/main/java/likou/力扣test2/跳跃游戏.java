package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：跳跃游戏
 * @Date：2025/7/3 21:48
 * @Filename：跳跃游戏
 */
public class 跳跃游戏 {
    public static void main(String[] args) {
        int[]nums={3,2,1,0,4};
        System.out.println(canJump(nums));
        System.out.println(canJump2(nums));
    }
    public static boolean canJump2(int[] nums) {
        int cover=0;
        if (nums.length==1){
            return true;
        }
        for (int i = 0; i <= cover; i++) {
            cover=Math.max(cover,i+nums[i]);
            if (cover>=nums.length-1){
                return true;
            }
        }
        return false;
    }
    public static boolean canJump(int[] nums) {

        int c=0;
        for (int i = 0; i <=c; i++) {
            c=Math.max(c,i+nums[i]);
            if (c>=nums.length-1){
                return true;
            }
        }
        return false;
    }


}
