package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合综合
 * @Date：2025/2/12 10:32
 * @Filename：组合综合
 */
public class 组合综合 {
    public static void main(String[] args) {
        List<List<Integer>> lists = combinationSum(new int[]{2, 3, 5}, 8);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }
    }


    static List<List<Integer>> ans;
    static List<Integer> tans;
    static int target;
    static int[] nums;
    public static List<List<Integer>> combinationSum(int[] candidates, int t) {
        nums=candidates;
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        target=t;
        backtracking(0,0);
        return ans;
    }

    public static void backtracking(int index,int sum){
        if (sum==target){
            ans.add(new ArrayList<>(tans));
            return;
        }
        if (sum>target){
            return;
        }
        for (int i=index;i<nums.length;i++){
            tans.add(nums[i]);
            backtracking(i,sum+nums[i]);
            tans.remove(tans.size()-1);
        }
    }
}
