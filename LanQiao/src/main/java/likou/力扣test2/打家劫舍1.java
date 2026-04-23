package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：打家劫舍1
 * @Date：2025/6/17 17:41
 * @Filename：打家劫舍1
 */
public class 打家劫舍1 {
    public static void main(String[] args) {
        int [] nums=new int[]{1,2,1,1};
        //int [] nums=new int[]{2,3,2};
       // int [] nums=new int[]{200,3,140,20,10};
        System.out.println(rob(nums));
    }
    public static int rob(int[] nums) {
        int len=nums.length;
        if (len<=0){
            return 0;
        }
        if (len==1){
            return nums[0];
        }
        if (len==2) {
            return Math.max(nums[0], nums[1]);
        }
        return Math.max(help(nums,0,len-2), help(nums,1,len-1));
    }

/*    public static int help(int []nums,int start,int end){
        int first=nums[start],second=nums[start+1];
        int ans=Math.max(first,second);
        for(int i=start+2;i<=end;i++){
            int temp = nums[i] + first;
            ans=Math.max(ans,temp);
            first=second;
            second=temp;
        }
        return ans;
    }*/
    public static int help(int[] nums, int start, int end) {
        int first = nums[start], second = Math.max(nums[start], nums[start + 1]);
        for (int i = start + 2; i <= end; i++) {
            int temp = second;
            second = Math.max(first + nums[i], second);
            first = temp;
        }
        return second;
    }

}
