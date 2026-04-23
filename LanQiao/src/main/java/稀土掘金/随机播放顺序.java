package 稀土掘金;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：随机播放顺序
 * @Date：2024/12/28 17:25
 * @Filename：随机播放顺序
 */
public class 随机播放顺序 {


    public static void main(String[] args) {
        System.out.println(Arrays.equals(solution(5, new int[]{5, 3, 2, 1, 4}), new int[]{5, 2, 4, 1, 3}));
        System.out.println(Arrays.equals(solution(4, new int[]{4, 1, 3, 2}), new int[]{4, 3, 1, 2}));
        System.out.println(Arrays.equals(solution(6, new int[]{1, 2, 3, 4, 5, 6}), new int[]{1, 3, 5, 2, 6, 4}));
    }


    public static  int[] solution(int n, int[] a) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        ArrayList<Integer> alist = new ArrayList<>();
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            alist.add(a[i]);
        }
        for (int i = 0; i < n; i++) {
            res.add(alist.get(0));
            alist.remove(0);
            if (i+1!=n){
                alist.add(alist.get(0));
                alist.remove(0);
            }
        }
        int []ans=new int[n];
        for (int i = 0; i < n; i++) {
            ans[i]= res.get(i);
        }
        return ans;
    }
}
