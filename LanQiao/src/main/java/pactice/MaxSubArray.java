package pactice;

public class MaxSubArray {
    public static void main(String[] args) {
        int[] nums={-2,-1};
        maxSubArray(nums);
    }


/*    public static int maxSubArray(int[] nums) {
        if (nums.length==1){
            return nums[0];
        }

        int max=0;
        int mid=0;
        for (int i = 0; i < nums.length; i++) {
            mid+=nums[i];
            max=Math.max(max,mid);
            if (mid<=0){
                mid=0;
            }

        }
        System.out.println(max);
        return max;
    }

    */
    public static int maxSubArray(int[] nums) {
        if (nums.length == 1){
            return nums[0];
        }
       // int sum = Integer.MIN_VALUE;
        int sum = 0;
        int count = 0;
        for (int i = 0; i < nums.length; i++){
            count += nums[i];
            sum = Math.max(sum, count); // 取区间累计的最大值（相当于不断确定最大子序终止位置）
            if (count <= 0){
                count = 0; // 相当于重置最大子序起始位置，因为遇到负数一定是拉低总和
            }
        }
        System.out.println(sum);
        return sum;
    }


}
