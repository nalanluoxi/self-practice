package HuiSu;

import java.util.*;
import java.util.function.Consumer;

public class ZiJi {
    public static void main(String[] args) {
        int []num={1,2,3};
        List<List<Integer>> subsets = subsets(num);
        System.out.println(subsets);
    }

    static List<List<Integer>> result=new ArrayList<>();

   /* static HashSet<List<Integer>>ans=new HashSet<>();

    static HashSet<Integer>templist=new HashSet<>();*/

    static LinkedList<Integer> tem=new LinkedList<>();

    public static List<List<Integer>> subsets(int[] nums) {
        Arrays.sort(nums);
        backing(nums,0);
        //copyresult(result,ans);
        return result;
    }

    public static void backing(int[]num ,int start){

        /*if (!result.contains(templist)){
            result.add(new ArrayList<>(templist));
        }*/
        result.add(new ArrayList<>(tem));
        //ans.add(new ArrayList<>(templist));
        if (start==num.length){
            return;
        }

        for (int i = start; i < num.length; i++) {
            tem.add(num[i]);
            backing(num,i+1);
            tem.removeLast();
            //templist.remove(templist.size()-1);
        }
    }




  /*  static List<List<Integer>> result = new ArrayList<>();// 存放符合条件结果的集合
    static LinkedList<Integer> path = new LinkedList<>();// 用来存放符合条件结果
    public static List<List<Integer>> subsets(int[] nums) {
        subsetsHelper(nums, 0);
        return result;
    }

    private static void subsetsHelper(int[] nums, int startIndex){
        result.add(new ArrayList<>(path));//「遍历这个树的时候，把所有节点都记录下来，就是要求的子集集合」。
        if (startIndex >= nums.length){ //终止条件可不加
            return;
        }
        for (int i = startIndex; i < nums.length; i++){
            path.add(nums[i]);
            subsetsHelper(nums, i + 1);
            path.removeLast();
        }
    }*/

}
