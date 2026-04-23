package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：搜索旋转排列数组
 * @Date：2025/6/7 20:20
 * @Filename：搜索旋转排列数组
 */
public class 搜索旋转排列数组 {

    public static void main(String[] args) {
        //int[] nums={4,5,6,7,0,1,2};
        int[] nums1={1};
        int[] nums={5,1,3};
        int target=3;
        System.out.println(search(nums, target));
    }
    public static int search(int[] nums, int target) {
        int l=0,r=nums.length-1;

        while (l<=r){
            int mid=l+(r-l)/2;
            if (nums[mid]==target){
                return mid;
            }
            if (nums[0]<=nums[mid]){
                if (target<nums[mid]&&target>=nums[0]){
                    r=mid-1;
                }else {
                    l=mid+1;
                }
            }else {
                if (target>nums[mid]&&target<=nums[nums.length-1]){
                    l=mid+1;
                }else {
                    r=mid-1;
                }
            }

        }
        if (l>=nums.length||nums[l]!=target){
            return -1;
        }
        return nums[l];
    }

}
