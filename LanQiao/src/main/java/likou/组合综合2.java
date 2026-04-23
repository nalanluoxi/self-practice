package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合综合2
 * @Date：2025/2/12 10:41
 * @Filename：组合综合2
 */
public class 组合综合2 {
    public static void main(String[] args) {
       List<List<Integer>> lists = combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }

    }

    static List<List<Integer>> ans;
    static List<Integer>tans;
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        Arrays.sort(candidates);
        backtracking(candidates,target,0);
        return ans;
    }

    public static void backtracking(int[] candidates,int target,int index){
        if (target==0){
            ans.add(new ArrayList<>(tans));
            return;
        }
        if (target<0){
            return;
        }
        for (int i=index;i<candidates.length;i++){
            if (candidates[i]>target){
                return;
            }
            if (i>index&&candidates[i]==candidates[i-1]){
                continue;
            }
            tans.add(candidates[i]);
            backtracking(candidates,target-candidates[i], i+1);
            tans.remove(tans.size()-1);
        }
    }
}
