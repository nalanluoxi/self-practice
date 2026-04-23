package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：子集
 * @Date：2025/2/6 15:54
 * @Filename：子集
 */
public class 子集 {
    public static void main(String[] args) {
        int[] nums={1,2,3};
        List<List<Integer>> subsets = subsets(nums);
        for (List<Integer> subset : subsets) {
            System.out.println(subset);
        }
    }

    static List<List<Integer>> ans;
    static int [] nums;
    static List<Integer> tans;
    public static  List<List<Integer>> subsets(int[] num) {
        ans=new ArrayList<>();
        nums=num;
        tans=new ArrayList<>();
        for (int i = 0; i <= nums.length; i++) {
            dps(i,0);
        }
        return ans;
    }

    public static void dps(int len,int index){
        if (tans.size()==len){
            //System.out.println(tans);
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            tans.add(nums[i]);
            //
            dps(len,i+1);
            tans.remove(tans.size()-1);
        }
        //tans.clear();
    }

}
