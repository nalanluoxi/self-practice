package likou.回溯;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.回溯
 * @Project：LanQiaoBei
 * @name：全排列
 * @Date：2025/6/10 14:30
 * @Filename：全排列
 */
public class 全排列 {

    public static void main(String[] args) {

    }
    public static List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        ans=new ArrayList<>();
        num=nums;
        dfs(0);
        return ans;
    }
    static int[] num;
    static List<List<Integer>> ans;
    public static void dfs(int start){
        if(num.length==start){
           // ans.add(new ArrayList<>(num));
            return;
        }
        for(int i=start;i<num.length;i++){
            swap(i,start);
            dfs(i+1);
            swap(i,start);
        }
    }

    public static void swap(int i,int j){
        int t=num[i];
        num[i]=num[j];
        num[j]=num[i];
    }
}
