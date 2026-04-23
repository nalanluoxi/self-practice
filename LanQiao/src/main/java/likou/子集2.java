package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：子集2
 * @Date：2025/2/6 18:35
 * @Filename：子集2
 */
public class 子集2 {

    public static void main(String[] args) {
        List<List<Integer>> lists = subsetsWithDup(new int[]{1, 2, 2});
        for (List<Integer> list : lists) {
            System.out.println(list);
        }

    }


    static List<List<Integer>> ans;
    static int[] nums;
    static List<Integer> tans;
    public static List<List<Integer>> subsetsWithDup(int[] num) {
        Arrays.sort(num);
        nums=num;
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        for (int i = 0; i <= num.length; i++) {
            dps(i, 0);
        }
        return ans;
    }

    public static void dps(int len,int index){
        if (tans.size()==len){
            if (ans.contains(tans)){
                return;
            }
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            if (i>index&&nums[i]==nums[i-1]){
                continue;
            }
            tans.add(nums[i]);
            dps(len,i+1);
            tans.remove(tans.size()-1);
        }
    }
}
