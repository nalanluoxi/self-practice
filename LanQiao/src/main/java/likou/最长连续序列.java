package likou;

import java.util.Arrays;

public class 最长连续序列 {
    public static void main(String[] args) {
        int[] nums = {0,3,7,2,5,8,4,6,0,1};
        System.out.println(longestConsecutive(nums));
    }

    public static int longestConsecutive(int[] nums) {
        if (nums.length==0){
            return 0;
        }
        Arrays.sort(nums);
        int maxlen = 0;
        int temlen = 1;
        for (int i = 1; i < nums.length; i++) {
            int temnum = nums[i];
            if (temnum == nums[i - 1]+1) {
                temlen++;
            } else if (temnum==nums[i-1]) {
                continue;
            } else {
                if (temlen > maxlen) {
                    maxlen = temlen;
                }
                temlen = 1;
            }
        }
        if (temlen > maxlen) {
            maxlen = temlen;
        }
        return maxlen;
    }


}
