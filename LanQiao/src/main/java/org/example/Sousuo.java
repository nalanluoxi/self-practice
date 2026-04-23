package org.example;

import java.util.ArrayList;

public class Sousuo {
    public static void main(String[] args) {
      //  int [] nums={5,7,7,8,8,10};
        int [] nums={2,2};
        int target=2;
        int [] n=new int[2];
        searchRange(nums,target);

    }
    public static int[] searchRange(int[] nums, int target) {
        int left=-1,right=-1;
         left=searchleft(nums,target);
        System.out.println("left="+left);
         right=searchright(nums,target);
        System.out.println("right="+right);
         int []n={left,right};
         return n;
    }

    private static int searchleft(int[] nums, int target) {
        int len=nums.length-1;
        int le=0;
        int ri=len;
        int midlle=0;
        for (int i = 0; i <= len; i++) {
            midlle=(ri+le)/2;
            if (nums[i]==target){
                for (int j = i; j >=0; j--) {
                    if (nums[j]!=target){
                        return j+1;
                    }
                    if (j==0)
                        return j;
                }
            }
            else if (nums[i]>target){
                ri=midlle-1;
            }
            else if (nums[i]<target) {
                le=midlle+1;
            }

        }

    return -1;
    }

    private static int searchright(int[] nums, int target) {
        int len=nums.length-1;
        int le=0;
        int ri=len;
        int midlle=0;
        for (int i = 0; i <= len; i++) {
            midlle=(ri+le)/2;
            if (nums[i]==target){
                for (int j = i; j <=len; j++) {
                    if (nums[j]!=target){
                        return j-1;
                    }
                    if (j==len)
                        return j;
                }
            }
            else if (nums[i]>target){
                ri=midlle-1;
            }
            else if (nums[i]<target) {
                le=midlle+1;
            }

        }

    return -1;
    }




 /*   public static int searchInsert(int[] nums, int target) {
        int right=nums.length-1;
        int left=0;
        int midlle=0;
        while (right>=left){
            midlle=(right+left)/2;
            if (nums[midlle]==target){
                return midlle;
            }
            else if (nums[midlle]>target) {
                right=midlle-1;
            }
            else if (nums[midlle]<target) {
                left=midlle+1;
            }

        }
        return right+1;
    }*/

}
