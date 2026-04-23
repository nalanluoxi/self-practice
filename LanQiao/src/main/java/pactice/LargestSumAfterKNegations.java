package pactice;

import java.util.*;

public class LargestSumAfterKNegations {

    public static void main(String[] args) {
        int[]nums={2,-3,-1,5,-4};
        int k =2;
        System.out.println(largestSumAfterKNegations(nums,k));

    }


    public static int largestSumAfterKNegations(int[] nums, int k) {
        Arrays.sort(nums);
        int count=0;
        for (int i: nums) {
            if (i<0){
                count++;//计数负数个数
            }
        }
        if (count>=k){//如果负数个数大于k，从前往后一次*-1塞回去
            for (int i = 0; i < k; i++) {
                nums[i]=nums[i]*-1;
            }
        }else {//k大于负数个数
            for (int i = 0; i < count; i++) {
                nums[i]=nums[i]*-1;
                k--;
            }//从前往后把负数变正，k递减
            if (k%2==0){
                return addsum(nums);
            }else {
                Arrays.sort(nums);
                nums[0]=nums[0]*-1;
                return addsum(nums);
            }

        }
        return addsum(nums);//求和

    }

    public static int addsum(int[]nums ){
        int sum=0;
        for (int i = 0; i < nums.length; i++) {
            sum+=nums[i];
        }
        return sum;
    }



}
