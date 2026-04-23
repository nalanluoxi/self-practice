package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：组合
 * @Date：2025/2/3 15:59
 * @Filename：组合
 */
public class 组合 {
    public static void main(String[] args) {

        List<List<Integer>> lists = combine(4, 2);
        for (List<Integer> integers : lists) {
            for (Integer integer : integers) {
                System.out.print(integer + " ,");
            }
            System.out.println();
        }
    }


    static List<List<Integer>> res;
    static List<Integer> list;
    static int N;
    static int K;

    public static List<List<Integer>> combine(int n, int k) {
        res = new ArrayList<>();
        if (k <= 1 || n < k) {
            return res;
        }
        N = n;
        K = k;
        list = new ArrayList<>();
        dps(1);
        return res;
    }

    public static void dps(int n) {
        if (list.size() == K) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = n; i <=N-(K-list.size())+1; i++) {
            list.add(i);
            dps(i + 1);
            list.remove(list.size() - 1);
        }
    }


}
