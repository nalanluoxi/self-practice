package likou.贪心;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：likou.贪心
 * @Project：LanQiaoBei
 * @name：分发饼干
 * @Date：2025/3/11 20:24
 * @Filename：分发饼干
 */
public class 分发饼干 {
    public static void main(String[] args) {
        int[] g = {1,2};
        int[] s = {1,2,3};
        System.out.println(findContentChildren(g,s));
    }
    public static   int findContentChildren(int[] g, int[] s) {
        int ans=0;
        Arrays.sort(g);
        Arrays.sort(s);
        for (int i = 0, j=0; i < s.length && j<g.length; i++) {
            if (s[i]>=g[j]){
                ans++;
                j++;
            }
        }
        return ans;
    }

    /*public static int findContentChildren(int[] g, int[] s) {
        int ans=0;
        Arrays.sort(g);
        Arrays.sort(s);
        int i=g.length-1,j= s.length-1;
        while (i>=0&&j>=0){
            if (g[i]<=s[j]){
                ans++;
                i--;
                j--;
            }else {
                i--;
            }
        }
        return ans;
    }*/
}
