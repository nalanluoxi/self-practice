package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：搜索旋转排序数组
 * @Date：2025/3/24 11:33
 * @Filename：搜索旋转排序数组
 */
public class 搜索旋转排序数组 {
    public static void main(String[] args) {

    }

    public static int search(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) {
            return -1;
        }
        if (n == 1) {
            return nums[0] == target ? 0 : -1;
        }
        int l=0;
        int r=nums.length-1;
        while (l<=r){
            int mid = l+(r-l)/2;
            if (nums[mid]==target){
                return mid;
            }
            if (nums[0]<=nums[mid]){
                if (nums[0]<=target && target<=nums[mid]){
                    r=mid-1;
                }else {
                    l=mid+1;
                }
            }else {
                if (nums[mid]<=target && target<=nums[n-1]){
                    l=mid+1;
                }else {
                    r=mid-1;
                }
            }
        }
        return -1;
    }
}
