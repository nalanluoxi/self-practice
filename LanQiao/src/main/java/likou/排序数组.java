package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：排序数组
 * @Date：2025/3/20 21:25
 * @Filename：排序数组
 */
public class 排序数组 {
    public static void main(String[] args) {
        //int[] nums = {5,2,3,1};
        int[] nums = {5, 1, 1, 2, 0, 0};
        //int[] nums = {110, 100, 0};
        int[] ints = sortArray(nums);
        for (int anInt : ints) {
            System.out.print(anInt + " ");
        }
    }
    public static int[] sortArray(int[] nums) {
        help(nums,0,nums.length-1);
        return nums;
    }

    public static void help(int[] nums,int left,int right){
        if (left>=right){
            return;
        }
        int mid = (left + right) / 2;
        help(nums,left,mid);
        help(nums,mid+1,right);
        merge(nums,left,mid,right);
    }

    public static void merge(int []nums,int left,int mid,int right){
        int[] temp=new int[right-left+1];
        int i=left;
        int j=mid+1;
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
        for (k=0;k<temp.length;k++){
            nums[k+left]=temp[k];
        }
    }

   /* public static int[] sortArray(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        help(nums, left, right);
        return nums;
    }

    public static void help(int []nums,int left,int right){
        if (left>=right){
            return;
        }
        int quick = quick(nums, left, right);
        help(nums, left, quick - 1);
        help(nums, quick + 1, right);
    }

    public static int quick(int[] nums, int left, int right) {
        int i = left;
        int j = right;
        while (i < j) {
            while (i < j && nums[j] >= nums[left]) {
                j--;
            }
            while (i < j && nums[i] <= nums[left]) {
                i++;
            }
            swap(nums, i, j);
        }
        swap(nums, i, left);
        return i;
    }

    public static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
*/

    //冒泡
   /* public static int[] sortArray(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int base = nums[i];
            int j = i - 1;
            while (j >= 0 && base < nums[j]) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = base;
        }
        return nums;
    }*/

  /*  public static int[] sortArray(int[] nums) {
        for (int i = nums.length-1; i >=0; i--) {
            for (int j = 0; j <i; j++) {
                if (nums[j]>nums[j+1]){
                    int num = nums[j];
                    nums[j]=nums[j+1];
                    nums[j+1]=num;
                }
            }
        }
        return nums;
    }*/


    //冒泡排序
   /* public static int[] sortArray(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i; j < nums.length; j++) {
                if (nums[i]>nums[j]){
                    swap(nums,i,j);
                }
            }
        }
        return nums;
    }

    public static void swap(int[] nums,int i,int j){
        int temp = nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }*/
}
