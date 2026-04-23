package likou.力扣test2;

import likou.合并k个升序链表;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：Test0923
 * @Date：2025/9/23 23:01
 * @Filename：Test0923
 */
public class Test0923 {
    public static void main(String[] args) {
       /* int[] nums = {2, 0, 2, 1, 1, 0};
        sortColors(nums);
        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }*/

       // int[]nums={3,2,1};
/*        int[]nums={1,5,1};
        nextPermutation(nums);
        for (int num : nums) {
            System.out.println(num);
        }*/

       // int[]nums={3,1,3,4,2};
      /*  int[]nums={3,3,3,3,3};
        int duplicate = findDuplicate(nums);
        System.out.println(duplicate);*/
       /* int[]nums={2,2,1,1,3};
        System.out.println(singleNumber(nums));*/
/*
        int[]nums={2,2,1,1,1,2,2};
        System.out.println(majorityElement(nums));*/

        /*System.out.println(minDistance("horse","ros"));*/

        System.out.println(longestCommonSubsequence("abcde","ace"));
    }


    public static int longestCommonSubsequence(String text1, String text2) {
        int l1 = text1.length();
        int l2 = text2.length();
        if (l1*l2==0){
            return 0;
        }

        int [][]dp=new int[l1+1][l2+1];
        for (int i = 1; i <= l1; i++) {
            for (int j = 1; j <= l2; j++) {
                if (text1.charAt(i-1)==text2.charAt(j-1)){
                    dp[i][j]=dp[i-1][j-1]+1;
                }else {
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        return dp[l1][l2];
    }
    public static int minDistance(String word1, String word2) {
        int l1 = word1.length();
        int l2 = word2.length();
        if (l1*l2==0){
            return l1+l2;
        }
        int[][]arr=new int[l1+1][l2+1];

        for (int i = 1; i <= l1; i++) {
            arr[i][0]=i;
        }
        for (int i = 1; i <= l2; i++) {
            arr[0][i]=i;
        }

        for (int i = 1; i <=l1; i++) {
            for (int j =1; j <=l2; j++) {
                if (word1.charAt(i-1)==word2.charAt(j-1)){
                    arr[i][j]=arr[i-1][j-1];
                }else {
                    arr[i][j]=Math.min(arr[i-1][j-1],Math.min(arr[i][j-1],arr[i-1][j]))+1;
                }
            }
        }
        return arr[l1][l2];
    }

    public static int majorityElement(int[] nums) {
        Map<Integer,Integer>map=new HashMap<>();

        for (int num : nums) {
            map.put(num,map.getOrDefault(num,0)+1);
            if (map.get(num)>nums.length/2){
                return num;
            }
        }
        return -1;
    }
    public static int singleNumber(int[] nums) {
        int ans=0;
        for (int num : nums) {
            ans^=num;
        }

        return ans;
    }

    public static int findDuplicate(int[] nums) {
        int []arr=new int[nums.length];
        for (int num : nums) {
            arr[num]++;
            if (arr[num]>1){
                return num;
            }
        }

        return -1;
    }


    public static void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= i && nums[j] <= nums[i]) {
                j--;
            }
            swap(i, j, nums);
        }
        reverse(nums, i+1);
    }

    public static void reverse(int[] nums, int start) {
        int i = start;
        int j = nums.length - 1;
        while (i <= j) {
            swap(i, j, nums);
            i++;
            j--;
        }
    }

    public static void swap(int i, int j, int[] nums) {
        int num = nums[i];
        nums[i] = nums[j];
        nums[j] = num;
    }


    public static void sortColors(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        int cur = 0;
        while (cur <= r) {
            if (nums[cur] == 0) {
                swap(l, cur, nums);
                l++;
                cur++;
            } else if (nums[cur] == 1) {
                cur++;
            } else if (nums[cur] == 2) {
                swap(cur, r, nums);
                r--;
            }
        }
    }


}
