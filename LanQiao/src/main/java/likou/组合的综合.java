package likou;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合的综合
 * @Date：2025/2/11 21:55
 * @Filename：组合的综合
 */
public class 组合的综合 {
    public static void main(String[] args) {
        List<List<Integer>> lists = combinationSum3(3, 7);
        for (List<Integer> list : lists) {
            System.out.println(list);
        }
    }


    static List<List<Integer>> ans;
    static List<Integer> tans;
    static  int k;
    static int n;
    public static List<List<Integer>> combinationSum3(int K, int N) {
        k= K;
        n=N;
        ans=new ArrayList<>();
        tans=new ArrayList<>();
        dps(1,0);
        return ans;
    }
    public static void dps(int index,int sum){
        if (tans.size()==k&&sum==n){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i = index; i <=9 ; i++) {
            if (sum+1>n){
                return;
            }
            tans.add(i);
            dps(i+1,sum+i);
            tans.remove(tans.size()-1);
        }
    }


}
