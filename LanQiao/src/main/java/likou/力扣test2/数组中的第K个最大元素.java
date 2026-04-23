package likou.力扣test2;

import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：数组中的第K个最大元素
 * @Date：2025/5/21 7:59
 * @Filename：数组中的第K个最大元素
 */
public class 数组中的第K个最大元素 {
    public static void main(String[] args) {

        //System.out.println(findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
    }


    public static int findKthLargest1(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    public static int findKthLargest2(int[] nums, int k) {
        int[] quick = quick(nums);
        return quick[quick.length - k];
    }

    public static int[] quick(int[] nums) {
        int left = 0, right = nums.length - 1;
        help(nums, left, right);
        return nums;
    }

    public static void help(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int partition = partition(nums, left, right);
        help(nums, left, partition - 1);
        help(nums, partition + 1, right);
    }

    public static void swap(int[] nums, int l, int j) {
        int num = nums[l];
        nums[l] = nums[j];
        nums[j] = num;
    }

    public static int partition(int[] nums, int left, int right) {
        int i = left, j = right;
        while (i < j) {
            while (i < j && nums[j] >= nums[left]) {
                j--;
            }
            while (i < j && nums[i] <= nums[left]) {
                i++;
            }
            swap(nums, i, j);
        }
        swap(nums, left, i);
        return i;
    }

    public static int findKthLargest3(int[] nums, int k) {
        insert(nums);
        return nums[nums.length-k];
    }

    public static void insert(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int base = nums[i];
            int j = i - 1;
            while (j >= 0 && base < nums[j]) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = base;
        }
    }


    public static int findKthLargest4(int[] nums, int k) {
        merge(nums);
        return nums[nums.length-k];
    }

    public static void merge(int[] nums){
        mergeHelp(nums,0,nums.length-1);
    }
    public static void mergeHelp(int[]nums ,int left,int right){
        if (left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        mergeHelp(nums,left,mid);
        mergeHelp(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[] nums,int left,int mid,int right){
        int[] temp=new int[right-left+1];
        int i=left,j=mid+1;
        int index=0;
        while (i<=mid && j<=right){
            if (nums[i]<=nums[j]){
                temp[index++]=nums[i++];
            } else  {
                temp[index++]=nums[j++];
            }
        }
        while (i<=mid){
            temp[index++]=nums[i++];
        }
        while (j<=right){
            temp[index++]=nums[j++];
        }
        for (int l = 0; l < temp.length; l++) {
            nums[left + l] = temp[l];
        }
    }


    public static int findKthLargest(int[] nums, int k) {
        hear(nums);
        return nums[nums.length-k];
    }

    public static void hear(int[]nums){
        for (int i=nums.length/2-1;i>=0;i--){
            Dui(nums,nums.length,i);
        }
        for (int i=nums.length-1;i>0;i--){
            swap(nums,0,i);
            Dui(nums,i,0);
        }
    }

    public static void Dui(int[] nums,int end,int start){
        while (true){
            int l=start*2+1;
            int r=start*2+2;
            int max=start;
            if (l<end && nums[l]>nums[max]){
                max=l;
            }
            if (r<end && nums[r]>nums[max]){
                max=r;
            }
            if (max==start){
                break;
            }
            swap(nums,max,start);
            start=max;
        }
    }

}
