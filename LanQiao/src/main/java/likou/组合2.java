package likou;

import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合2
 * @Date：2025/2/11 20:59
 * @Filename：组合2
 */
public class 组合2 {

    public static void main(String[] args) {
        List<List<Integer>> combine = combine(4, 2);
        for (List<Integer> integers : combine) {
            System.out.println(integers);
        }
    }

    static List<List<Integer>> ans;
    static List<Integer> tans;
    static int k;
    static int n;
    public static List<List<Integer>> combine(int N, int K) {
        tans=new ArrayList<>();
        ans=new ArrayList<>();
        k=K;
        n= N;
        dfs(1);
        return ans;
    }

    public static void dfs(int index){
        if (tans.size()==k){
            ans.add(new ArrayList<>(tans));
            return;
        }
        for (int i=index;i<=(n-k+tans.size()+1);i++){
            tans.add(i);
            dfs(i+1);
            tans.remove(tans.size()-1);
        }
    }
}
