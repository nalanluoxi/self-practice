package likou.二分;

/**
 * @Author 纳兰洛熙
 * @Package：likou.二分
 * @Project：LanQiaoBei
 * @name：搜索旋转排序数组
 * @Date：2025/6/24 19:34
 * @Filename：搜索旋转排序数组
 */
public class 搜索旋转排序数组 {

    public static void main(String[] args) {
        int[] nums = {5,1,3};
        int target = 1;
        System.out.println(search(nums, target));
    }
    public static int search(int[] nums, int target) {
        int l=0,r=nums.length-1;
        int mid=0;
        while (l<=r){
            mid=l+(r-l)/2;
            if (nums[mid]==target){
                return mid;
            }
            if(nums[mid]>=nums[l]){
                if (target<=nums[mid]&&target>=nums[l]){
                    r=mid-1;
                }else {
                    l=mid+1;
                }
            }else{
                if (target>=nums[mid]&&target<=nums[r]){
                    l=mid+1;
                }else{
                    r=mid-1;
                }
            }
        }
        if (l<nums.length&&nums[l]==target){
            return l;
        }
        return -1;
    }
}
