package likou;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：在排序数组中查找元素的第一个和最后一个位置
 * @Date：2025/5/3 21:10
 * @Filename：在排序数组中查找元素的第一个和最后一个位置
 */
public class 在排序数组中查找元素的第一个和最后一个位置 {

    public static void main(String[] args) {
      /*  int[] nums={5,7,7,8,8,10};
        int target=8;
        System.out.println(searchRange(nums,target));*/
        int[] nums = {1};
        int[] ints = searchRange(nums, 1);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }

    public static int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }
        int l = 0;
        int r = nums.length - 1;
        int mid = 0;
        while (l <= r) {
            mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                break;
            } else if (nums[mid] > target) {
                r = mid - 1;
            } else if (nums[mid] < target) {
                l = mid + 1;
            }
        }
        int start = mid;
        int end = mid;
        if (nums[mid] == target) {
            while (start > 0 && nums[start] == target) {
                start--;
            }
            if (nums[start] != target) {
                start++;
            }
        } else {
            start = -1;
        }
        if (nums[end] == target) {
            while (end < nums.length - 1 && nums[end] == target) {
                end++;
            }
            if (nums[end] != target) {
                end--;
            }
        } else {
            end = -1;
        }
        return new int[]{start, end};
    }


}
