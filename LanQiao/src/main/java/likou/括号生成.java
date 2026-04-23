package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：括号生成
 * @Date：2025/2/3 17:12
 * @Filename：括号生成
 */
public class 括号生成 {
    public static void main(String[] args) {
        List<String> strings = generateParenthesis(3);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    /*static List<String>res;
    static char[] chars;
    static int N;
    public static List<String> generateParenthesis(int n) {
        res=new ArrayList<>();
        chars=new char[n*2];
        N=n;
        dps(0,0);
        return res;
    }

    public static void dps(int index,int left){
       if (index==N*2){
           res.add(new String(chars));
           System.out.println(new String(chars));
           return;
       }
       if (index<N){
           chars[index]='(';
           dps(index+1,left+1);
       }
       if (index-left<left){
           chars[index]=')';
           dps(index+1,left);
       }
    }*/

    private static int n;
    private static final List<String> ans = new ArrayList<>();
    private  static char[] path;

    public static List<String> generateParenthesis(int k) {
        n = k;
        path = new char[n * 2]; // 所有括号长度都是一样的 n*2
        dfs(0, 0);
        return ans;
    }

    // i = 目前填了多少个括号
    // open = 左括号个数，i-open = 右括号个数
    private static void dfs(int i, int open) {
        //System.out.println(new String(path));
        if (i == n * 2) { // 括号构造完毕
            ans.add(new String(path)); // 加入答案
            System.out.println("添加一次答案");
            System.out.println(new String(path));
            return;
        }
        if (open < n) { // 可以填左括号
            path[i] = '('; // 直接覆盖
            System.out.println(new String(path));
            dfs(i + 1, open + 1); // 多了一个左括号
        }
        if (i - open < open) { // 可以填右括号
            path[i] = ')'; // 直接覆盖
            System.out.println(new String(path));
            dfs(i + 1, open);
        }
    }
}
