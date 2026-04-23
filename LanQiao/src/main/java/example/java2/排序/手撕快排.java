package example.java2.排序;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.排序
 * @Project：LanQiaoBei
 * @name：手撕快排
 * @Date：2025/6/9 9:24
 * @Filename：手撕快排
 */
public class 手撕快排 {

    public static void main(String[] args) {
        int[]nums=new int[]{5,2,3,1};
        int[] ints = sortArray(nums);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
//    public static int[] sortArray(int[] nums) {
//        int len=nums.length;
//        for(int i=1;i<len;i++){
//            int base=nums[i];
//            int j=i-1;
//            while(j>=0 && nums[j]<base){
//                nums[j+1]=nums[j];
//                j--;
//            }
//            nums[j+1]=base;
//        }
//        return nums;
//    }

    public static int[] sortArray(int[] nums) {
        sort(nums,0,nums.length-1);
        return nums;
    }

    public static void sort(int[] nums, int left, int right){
        if(left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums,int left,int mid ,int right){
        int i=left,j=mid+1,l=0;
        int[]temp=new int[right-left+1];
        while(i<=mid && j<=right){
            if(nums[i]<=nums[j]){
                temp[l++]=nums[i++];
            }else{
                temp[l++]=nums[j++];
            }
        }
        while(i<=mid){
            temp[l++]=nums[i++];
        }
        while(j<=right){
            temp[l++]=nums[j++];
        }
        for(int index=0;index<temp.length;index++){
            nums[left+index]=temp[index];
        }
    }
/*    public static int[] sortArray(int[] nums) {
        sort(nums,0,nums.length-1);
        return nums;
    }

    public static void sort(int[]nums,int left,int right){
        if(left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        sort(nums,left,mid);
        sort(nums,mid+1,right);
        addTwo(nums,left,mid,right);
    }

    public static void addTwo(int[]nums,int left,int mid ,int right){
        int i=left,j=mid+1,l=0;
        int[]temp=new int[right-left+1];
        while(i<=mid && j<=right){
            if(nums[i]<=nums[j]){
                temp[l++]=nums[i++];
            }else{
                temp[l++]=nums[j++];
            }
        }
        while(i<=mid){
            temp[l++]=nums[i++];
        }
        while(j<=right){
            temp[l++]=nums[j++];
        }
        for(int index=0;index<temp.length;index++){
            nums[left+index]=temp[index];
        }
    }*/
}
