package likou.力扣test2;

import java.util.*;

/**
 * @author nalan_luoxi
 * @version 1.0
 * @email adrian0304@qq.com
 * @project self-practice
 * @package likou.力扣test2
 * @date 2026-06-03 10:33
 */
public class Test0603 {

    public static void main(String[] args) {
       /* int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println(threeSum(nums));*/
        /*int[]nums={1,3,6,7,9,4,10,5,6};
        System.out.println(lengthOfLIS(nums));*/
        int[] nums = {5, 1, 1, 2, 0, 0};
        sortArray(nums);
        System.out.println(Arrays.toString(nums));
    }

    public static int[] sortArray(int[] nums) {
        sort(nums, 0, nums.length - 1);
        return nums;
    }

    public static void sort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        sort(nums, left, mid);
        sort(nums, mid + 1, right);
        addTwo(nums, left, mid, right);
    }

    public static void addTwo(int[] nums, int left, int mid, int right) {
        int len = right - left + 1;
        int[] temp = new int[len];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (nums[i] < nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        for (int l = 0; l < len; l++) {
            nums[l + left] = temp[l];
        }
    }


    public static int lengthOfLIS(int[] nums) {
        if (nums.length <= 1) {
            return nums.length;
        }
        int[] dp = new int[nums.length];
        dp[nums.length - 1] = 1;
        int ans = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            dp[i] = 1;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] > nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new LinkedList<>();
        if (nums.length < 3) {
            return ans;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            if (i != 0 && a == nums[i - 1]) {
                continue;
            }
            if (a > 0) {
                break;
            }
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {
                if (nums[l] + nums[r] + a == 0) {
                    List<Integer> temp = List.of(a, nums[l], nums[r]);
                    ans.add(temp);
                    while (l < r && nums[l] == nums[l + 1]) {
                        l++;
                    }
                    while (l < r && nums[r] == nums[r - 1]) {
                        r--;
                    }
                    l++;
                    r--;
                } else if (nums[l] + nums[r] + a < 0) {
                    l++;
                } else {
                    r--;
                }
            }
        }
        return ans;
    }

    public static int[][] merge(int[][] nums) {
        if (nums.length == 0) {
            return new int[][]{};
        }
        Deque<int[]> ans = new LinkedList<>();
        Arrays.sort(nums, (a, b) -> a[0] - b[0]);
        for (int[] num : nums) {
            if (ans.isEmpty()) {
                ans.add(num);
            } else {
                int[] last = ans.pollLast();
                if (last[1] < num[0]) {
                    ans.addLast(last);
                    ans.add(num);
                } else {
                    int[] temp = {Math.min(last[0], num[0]), Math.max(last[1], num[1])};
                    ans.addLast(temp);
                }
            }
        }
        return ans.toArray(new int[ans.size()][2]);
    }

}
