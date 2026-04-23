package pactice;

public class CanJump {

    public static void main(String[] args) {

        int[]nums={3,2,1,0,4};
        canJump(nums);
    }

    public static boolean canJump(int[] nums) {
       int cave=0;
       if (nums.length==1){
           System.out.println("true");
           return true;
       }

        for (int i = 0; i <= cave; i++) {
            cave=Math.max(cave,i+nums[i]);
            if (cave>=nums.length-1) {
                System.out.println("true");
                return true;
            }
        }
        System.out.println("false");
        return false;

    }




}
