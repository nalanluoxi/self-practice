package 蓝桥杯真题.决赛13届;

import java.util.Scanner;

/**
 * @Author 纳兰洛熙
 * @Package：蓝桥杯真题.决赛13届
 * @Project：LanQiaoBei
 * @name：斐波那契
 * @Date：2025/4/8 17:31
 * @Filename：斐波那契
 */
public class 斐波那契 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }
        helper(nums);

    }

    static int[] dp;

    public static void helper(int[] n) {
        int len = n.length;
        int ans = 0;
        if (len < 0) {
            System.out.println(0);
            return;
        }
        if (n[0] != 1) {
            ans++;
        }
        if (n[1] != 1) {
            ans++;
        }
        dp = new int[len];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i < len; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
            if (dp[i] != n[i]){
                ans++;
            }
        }
        System.out.println(ans);
    }
}
