package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：移动零
 * @Date：2025/5/28 22:09
 * @Filename：移动零
 */
public class 移动零 {

    public static void main(String[] args) {
        int[] nums={0,1,0,3,12};
        moveZeroes(nums);
        for (int i : nums) {
            System.out.println(i);
        }
    }
    public static void moveZeroes(int[] nums) {
        int len=nums.length;
        int left=0,right=0;
        while (right<len){
            if (nums[right]!=0){
                swap(nums,left,right);
                left++;
            }
            right++;
        }
    }
    public static void moveZeroes1(int[] nums) {
        int len = nums.length;
        int i=0;
        while (i<len-1){
            if (nums[i]!=0){
                i++;
                continue;
            }else {
                int j=i+1;
                while (j<len&&nums[j]==0){
                    j++;
                }
                if (j==len){
                    break;
                }
                swap(nums,i,j);
                i++;
            }
        }
    }
    public static void swap(int[] nums,int i,int j){
        int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
}
