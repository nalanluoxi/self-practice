package luogu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：三数之和
 * @Date：2025/3/19 11:13
 * @Filename：三数之和
 */
public class 三数之和 {
    public static void main(String[] args) {
        List<List<Integer>> lists = threeSum(new int[]{-1, 0, 1, 2, -1, -4});
        for (List<Integer> list : lists) {
            for (Integer last : list) {
                System.out.print(last + " ,");
            }
            System.out.println();
        }
    }

    public static List<List<Integer>> threeSum(int[] nums) {
      //  List<List<Integer>> ans = new ArrayList<>();
        HashSet<List<Integer>> ans=new HashSet<>();
        List<Integer> tan = new ArrayList<>();
        Arrays.sort(nums);
        int a, b, c;
        for (int i = 0; i < nums.length; i++) {
            a = nums[i];
            tan.add(a);
            for (int j = i + 1; j < nums.length; j++) {
                b = nums[j];
                tan.add(b);
                for (int k = nums.length - 1; k > j; k--) {
                    c = nums[k];
                    if (c < -(a + b)) {
                        break;
                    }
                    if (c == -(a + b)) {
                        tan.add(c);
                        ans.add(new ArrayList<>(tan));
                        tan.remove(tan.size()-1);
                        break;
                    }
                }
                tan.remove(tan.size()-1);
            }
            tan.remove(tan.size()-1);
        }
        List<List<Integer>> res=new ArrayList<>();
        for (List<Integer> an : ans) {
            res.add(an);
        }
        return res;
    }
}
