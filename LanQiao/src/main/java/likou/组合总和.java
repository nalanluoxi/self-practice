package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合总和
 * @Date：2025/5/4 22:13
 * @Filename：组合总和
 */
public class 组合总和 {
    public static void main(String[] args) {
        int[] nums={2,3,6,7};
        int target=7;
        List<List<Integer>> lists = combinationSum(nums, target);
        for (List<Integer> list : lists) {
            for (Integer integer : list) {
                System.out.print(integer+" ");
            }
            System.out.println();
        }
    }

    static List<List<Integer>> ans;
    static List<Integer> tans;

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        helper(candidates,target,0, candidates.length-1);

        return ans;
    }

    public static void helper(int[] nums,int target,int sum,int index){
        if (sum==target){
            ans.add(new ArrayList<>(tans));
            return;
        }else if (sum>target){
            return;
        }
        for (int i = index; i >=0 ; i--) {
            tans.add(nums[i]);
            helper(nums,target,sum+nums[i],i);
            tans.remove(tans.size()-1);
        }
    }

}
