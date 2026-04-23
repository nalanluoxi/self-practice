package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：括号生成2
 * @Date：2025/4/16 20:33
 * @Filename：括号生成2
 */
public class 括号生成2 {
    public static void main(String[] args) {
        System.out.println(generateParenthesis(3));
    }

    static List<String> ans;
    static char[] path;
    static int n;

    public static List<String> generateParenthesis(int num) {
        ans = new ArrayList<>();
        path = new char[num * 2];
        n = num;
        help(0, 0);
        return ans;
    }

    public static void help(int index, int left) {
        if (index == n*2) {
            ans.add(new String(path));
            return;
        }
        if (left < n) {
            path[index] = '(';
            help(index + 1, left + 1);
        }
        if (index - left < left) {
            path[index] = ')';
            help(index + 1, left);
        }
    }

}
