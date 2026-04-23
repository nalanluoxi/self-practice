package likou.力扣test2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：组合总数
 * @Date：2025/7/3 22:44
 * @Filename：组合总数
 */
public class 组合总数 {
    public static void main(String[] args) {
        int[]num={10,1,2,7,6,1,5};
        List<List<Integer>> lists = combinationSum2(num, 8);
        for (List<Integer> list : lists) {
            for (Integer i : list) {
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }

    static List<List<Integer>>ans;
    static List<Integer> tans;
    static int[]nums;
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {

        ans=new ArrayList<>();
        tans=new ArrayList<>();
        Arrays.sort(candidates);
        nums=candidates;
        dfs(target,0);
        return ans;
    }

    public static void dfs(int target,int index){
        if (target==0){
            ans.add(new ArrayList<>(tans));
            return;
        }
        if (target<0||index>nums.length){
            return;
        }
        for (int i = index; i <nums.length; i++) {
            if (i > index && nums[i] == nums[i - 1]) continue;
            tans.add(nums[i]);
            dfs(target-nums[i],i+1);
            tans.remove(tans.size()-1);
        }
    }
}
