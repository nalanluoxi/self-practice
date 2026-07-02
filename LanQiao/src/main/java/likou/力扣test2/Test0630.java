package likou.力扣test2;

import java.util.Arrays;

public class Test0630 {


    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4},2));
    }

    public static int findKthLargest(int[] nums, int k) {
        sort(nums,0,nums.length-1);
        return nums[nums.length-k];
    }

    public static void sort(int[]nums,int left,int right){
        if (left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }



    public static void addTwo(int[]nums,int left,int mid,int right){
        int[]temp=new int[right-left+1];
        int i=left,j=mid+1;
        int k=0;
        while (i<=mid && j<=right){
            if (nums[i]<=nums[j]){
                temp[k++]=nums[i++];
            }else {
                temp[k++]=nums[j++];
            }
        }

        while (i<=mid){
            temp[k++]=nums[i++];
        }
        while (j<=right){
            temp[k++]=nums[j++];
        }

        for (int l = 0; l < temp.length; l++) {
            nums[l+left]=temp[l];
        }
    }
}
