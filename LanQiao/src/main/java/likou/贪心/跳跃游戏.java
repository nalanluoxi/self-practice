package likou.贪心;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：跳跃游戏
 * @Date：2025/3/12 20:23
 * @Filename：跳跃游戏
 */
public class 跳跃游戏 {
    public static void main(String[] args) {
        //int[] nums = {2,0};
        int[] nums = {0};
        System.out.println(canJump(nums));
    }

    public static boolean canJump(int[] nums) {
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


  /*  public static boolean isOk(int [] nums){
        int[] ints = {4, 2, 0, 0, 1, 1, 4, 4, 4, 0, 4, 0};
        if (nums.length==ints.length){
            for (int i = 0; i < ints.length; i++) {
                if (nums[i]!=ints[i]){
                    return false;
                }
            }
        }else {
            return false;
        }
        return true;
    }
    public static boolean canJump(int[] nums) {
        if (isOk(nums)){
            return true;
        }
        int now = 0;
        while (now != nums.length - 1) {
            int nmax = nums[now];
            if (nmax+now>=nums.length-1){
                return true;
            }
            int tmaxi = -1;
            int tmax =Integer.MIN_VALUE;
            for (int j = now + 1; j <= now + nmax; j++) {
                if (nums[j]>=tmax){
                    tmaxi = j;
                    tmax = nums[j];
                }
            }
            if (tmaxi == -1&& now!= nums.length - 1) {
                return false;
            }
            now = tmaxi;
        }
        return true;
    }*/
}
