package likou;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：你可以安排的最多任务数目
 * @Date：2025/3/8 21:20
 * @Filename：你可以安排的最多任务数目
 */
public class 你可以安排的最多任务数目 {
    public static void main(String[] args) {
        int maxTaskAssign = maxTaskAssign(new int[]{10,15,30}, new int[]{0,10,10,10,10}, 3, 10);
        System.out.println(maxTaskAssign);
    }

    public static int maxTaskAssign(int[] ta, int[] wo, int pi, int st) {
        Arrays.sort(ta);
        Arrays.sort(wo);
        workers = wo;
        tasks = ta;
        strength = st;
        pills = pi;
        int ans = 0;
        for (int l = 0, r = Math.min(ta.length, wo.length); l <= r; ) {
            int mid = (l + r) >> 1;
            if (check(0, mid - 1, wo.length - mid, wo.length - 1)) {
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }

    static Deque<Integer> deque;

    public static boolean check(int tl, int tr, int wl, int wr) {
        int pnum = 0;
        deque = new LinkedList<>();
        int i,j;
        for ( i = tl, j = wl; j <= wr; j++) {
            int nowwo = workers[j];
            while (i <= tr && nowwo >= tasks[i]) {
                deque.offerLast(i++);
            }
            if (!deque.isEmpty() && nowwo >= tasks[deque.peekFirst()]) {
                deque.pollFirst();
                continue;
            } else {
                nowwo += strength;
                while (i <= tr && nowwo >= tasks[i]) {
                    deque.offerLast(i++);
                }
                if (!deque.isEmpty() && nowwo >= tasks[deque.peekLast()]) {
                    pnum++;
                    deque.pollLast();
                }
            }

        }
        return pnum <= pills&& i>tr &&deque.isEmpty() ;
    }

    static int pills;
    static int[] workers;
    static int[] tasks;
    static int strength;

}
