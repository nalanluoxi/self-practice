package HuiSu;

import java.util.*;

public class ZuHe2 {

    public static void main(String[] args) {
        int []num={2,2,3,6,7};
        int all=7;
        List<List<Integer>> lists = combinationSum2(num, all);
        System.out.println(lists);
    }


    static LinkedList<Integer> temp=new LinkedList<>();
    static List<List<Integer>> ans=new ArrayList<>();


    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {

        if (target==0||candidates==null){
            return ans;
        }
        Arrays.sort(candidates);
        backing(candidates,target,0,0);

        return ans;

    }

    public static void backing(int[] candidates, int target,int sum,int index){

        if (sum==target){
            ans.add(new ArrayList<>(temp));
            return;
        }

        for (int i = index; i < candidates.length&&sum+candidates[i]<=target; i++) {

            if ( i > index && candidates[i] == candidates[i - 1] ) {
                continue;
            }
            temp.add(candidates[i]);
            backing(candidates,target,sum+candidates[i],i+1);
            temp.removeLast();

        }


    }



                  /*   public List<List<Integer>> combinationSum2(int[] candidates, int target) {
                         List<List<Integer>> res = new ArrayList<>();
                         Arrays.sort(candidates); // 先进行排序
                         backtracking2(res, new ArrayList<>(), candidates, target, 0, 0);
                         return res;
                     }

                     public void backtracking2(List<List<Integer>> res, List<Integer> path, int[] candidates, int target, int sum, int idx) {
                         // 找到了数字和为 target 的组合
                         if (sum == target) {
                             res.add(new ArrayList<>(path));
                             return;
                         }

                         for (int i = idx; i < candidates.length; i++) {
                             // 如果 sum + candidates[i] > target 就终止遍历
                             if (sum + candidates[i] > target) break;
                             path.add(candidates[i]);
                             backtracking2(res, path, candidates, target, sum + candidates[i], i);
                             path.remove(path.size() - 1); // 回溯，移除路径 path 最后一个元素
                         }
                     }*/
}
