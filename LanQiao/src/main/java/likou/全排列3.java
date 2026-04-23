package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：全排列3
 * @Date：2025/3/24 11:42
 * @Filename：全排列3
 */
public class 全排列3 {
    public static void main(String[] args) {
        int[] nums={1,2,3};
        List<List<Integer>> permute = permute(nums);
        for (List<Integer> list : permute) {
            System.out.print("[ ");
            for (Integer last : list) {
                System.out.print(list+" ");
            }
            System.out.println("]");
        }
    }


    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans=new ArrayList<>();
        ArrayList<Integer> tans = new ArrayList<>();
        for (int num : nums) {
            tans.add(num);
        }
        dps(ans,tans,0);
        return ans;
    }

    public static void dps(List<List<Integer>> ans,List<Integer>tans,int index){
        if (index==tans.size()){
            /*if (ans.contains(tans)){
                return;
            }*/
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i < tans.size(); i++) {
            swa(tans,index,i);
            dps(ans,tans,index+1);
            swa(tans,index,i);
        }
    }

    public static void swa(List<Integer> tans,int x,int y){
        Integer last = tans.get(x);
        tans.set(x,tans.get(y));
        tans.set(y,last);
    }
}
