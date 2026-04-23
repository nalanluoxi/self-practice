package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：数组中第K个最大元素
 * @Date：2025/6/6 9:04
 * @Filename：数组中第K个最大元素
 */
public class 数组中第K个最大元素 {
    public static void main(String[] args) {
        int[] arr={-2,3,-5};
        sort(arr,0,2);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        //System.out.println(findKthLargest(new int[]{3,2,1,5,6,4},2));
    }
    public static int findKthLargest(int[] nums, int k) {
        sort(nums, 0, nums.length - 1);
        return nums[nums.length-k];
    }

    public static void sort(int [] nums,int left,int right){
        if (left>=right){
            return ;
        }
        int mid=left+(right-left)/2;
        sort(nums,left,mid);
        sort(nums, mid+1, right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums,int l,int mid,int r){
        int[] temp=new int[r-l+1];
        int i=l,j=mid+1;
        int t=0;
        while (i<=mid && j<=r){
            if (nums[i]<=nums[j]){
                temp[t++]=nums[i++];
            }else {
                temp[t++]=nums[j++];
            }
        }
        while (i<=mid){
            temp[t++]=nums[i++];
        }
        while (j<=r){
            temp[t++]=nums[j++];
        }
        for (int index = 0; index < temp.length; index++) {
            nums[l+index]=temp[index];
        }
    }
}
