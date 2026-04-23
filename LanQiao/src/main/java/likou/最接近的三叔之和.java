package likou;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：最接近的三叔之和
 * @Date：2025/7/5 10:30
 * @Filename：最接近的三叔之和
 */
public class 最接近的三叔之和 {
    public static void main(String[] args) {
        int[] ints = {-1, 2, 1, -4};
        System.out.println(threeSumClosest(ints,1));
    }

    public static int threeSumClosest(int[] nums, int target) {
        int ans=Integer.MAX_VALUE;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length-2; i++) {
            if (i>0&&nums[i]==nums[i-1]){
                continue;
            }
            int j=i+1,k=nums.length-1;
            while (j<k){
                int temp = nums[i] + nums[j] + nums[k];
                if (target==temp){
                    return temp;
                }
                if (Math.abs(target-temp)<Math.abs(target-ans)){
                    ans=temp;
                }
                if (temp>target) {
                    k--;
                    while (nums[k] == nums[k + 1] && k > j) {
                        k--;
                    }
                }else{
                    j++;
                    while (nums[j]==nums[j-1]&&j<k){
                        j++;
                    }
                }
            }
        }
        return ans;
    }
   /* public static int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int ans=Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            for (int j = i+1; j < nums.length; j++) {
                int b = nums[j];
                for (int k = j+1; k < nums.length; k++) {
                    int c = nums[k];
                    int t = a + b + c;
                    if (Math.abs(t-target)<Math.abs(ans-target)){
                        ans=t;
                    }
                }
            }
        }
        return ans;
    }*/
}
