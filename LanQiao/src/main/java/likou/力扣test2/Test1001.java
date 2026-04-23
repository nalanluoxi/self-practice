package likou.力扣test2;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test1001
 * @Date：2025/10/1 20:49
 * @Filename：Test1001
 */
public class Test1001 {
    public static void main(String[] args) {
        int[] nums = {4,5,6,7,0,1,2};

        System.out.println(search(nums, 5));
        //System.out.println(searchInsert(nums, 7));
    }

    public static int findMin(int[] nums) {
        int l=0;
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (nums[i]<nums[l]){
                l=i;
            }
        }
        return nums[l];
    }

    public static int search(int[] nums, int target) {
        int len = nums.length;
        int l=0;
        int r=nums.length-1;
        while (l<=r){
            int mid=l+(r-l)/2;
            if (nums[mid]==target){
                return mid;
            } else if (nums[mid] >= nums[l]) {
                if (nums[mid]>=target && nums[l]<=target){
                    r=mid-1;
                }else {
                    l=mid+1;
                }
            } else  {
                if (nums[mid]<=target && nums[r]>= target){
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

        int r = nums.length - 1;
        int l = 0;
        int mid=l;
        while (l <= r) {
            mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                break;
            } else if (nums[mid] > target) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        if (nums[mid]!=target){
            return new int[]{-1,-1};
        }
        int[] ans = new int[2];
        int t = mid;
        while (t >= 0 && nums[t] == target) {
            t--;
        }
        ans[0] = t + 1;
        t = mid;
        while (t < nums.length && nums[t] == target) {
            t++;
        }
        ans[1] = t - 1;
        return ans;
    }


    public boolean searchMatrix(int[][] nums, int target) {
        int n = nums.length - 1;
        int m = nums[0].length - 1;
        if (target < nums[0][0] || target > nums[n][m]) {
            return false;
        }
        int x = 0;
        int y = m;
        while (x <= n && y >= 0) {
            if (nums[x][y] == target) {
                return true;
            } else if (nums[x][y] > target) {
                y--;
            } else {
                x++;
            }
        }
        return false;
    }

    public static int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
