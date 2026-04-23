package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0818
 * @Date：2025/8/18 22:14
 * @Filename：Test0818
 */
public class Test0818 {
    public static void main(String[] args) {
        int[] ints = searchRange(new int[]{1, 4}, 4);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }
    }


    public static int search(int[] nums, int target) {
        int r = nums.length;
        int l=0;
        int mid=0;
        while (l<=r){
            mid=l+(r-l)/2;
            if (target==nums[mid]){
                return mid;
            }
            if (nums[mid]>=nums[l]){
                //左侧有序
                if (target<=nums[mid] && target>=nums[l]){
                    r=mid-1;
                }else {
                    l=mid+1;
                }
            }else {
                //右侧有序
                if (target>=nums[mid] &&target<= nums[r]){
                    l=mid+1;
                }else {
                    r=mid-1;
                }
            }
        }
        if (l<nums.length&&nums[l]==target){
            return l;
        }
        return -1;
    }


    public static int[] searchRange(int[] nums, int target) {
        if (nums.length==0||nums==null){
            return new int[]{-1,-1};
        }
        int l=0;
        int right=nums.length-1;
        int mid=l;
        while (l<=right){
            mid = l + (right - l) / 2;
            if (nums[mid]==target){
                break;
            }
            if (nums[mid]>target){
                right=mid-1;
            }else {
                l=mid+1;
            }
        }
        if (nums[mid]!=target){
            return new int[]{-1,-1};
        }
        int start=mid;
        int end=mid;
        while (start-1>=0 && nums[start-1]==target){
            start--;
        }
        while (end+1<nums.length &&nums[end+1]==target){
            end++;
        }
        return new int[]{start,end};
    }
    public static boolean searchMatrix(int[][] nums, int target) {
        if (nums == null || nums.length == 0 || nums[0].length == 0) return false;
        int n = nums.length;
        int m = nums[0].length;
        int x=n-1;
        int y=0;
        if (nums[0][0]>target || nums[n][m]<target){
            return false;
        }
        while (x>=0 &&y<m){
            if (nums[x][y]==target){
                return true;
            }
            if (nums[x][y]<target){
                y++;
            }else {
                x--;
            }
        }
        return false;
    }
}
