package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：小E的怪物挑战
 * @Date：2024/12/22 19:54
 * @Filename：小E的怪物挑战
 */
public class 小E的怪物挑战 {


    public static void main(String[] args) {
        System.out.println(solution(11, 14, 17, new int[]{14,16,2,16,3,10,9,6,3,6,10}, new int[]{14,9,11,10,7,1,12,2,10,17,17}));
    }

  /*  public static int solution(int n, int H, int A, int[] h, int[] a) {
        // write code here
        int[] pass = new int[n];
        for (int i = 0; i < n; i++) {
            if (H > h[i] && A > a[i]) {
                pass[i]++;
            }
        }
        for (int i = 1; i < pass.length; i++) {
            if (pass[i] != 0 && a[i] > a[i - 1] && h[i] > h[i - 1]) {
                pass[i] = pass[i] + pass[i - 1];
            }
        }
        int maxCount = 0;
        for (int i = 0; i < pass.length; i++) {
            maxCount = Math.max(maxCount, pass[i]);
        }
        return maxCount;
    }*/


    public static int solution(int n, int H, int A, int[] h, int[] a) {
        // 定义一个二维数组 dp，dp[i] 表示以第 i 个怪物结尾的序列中，最多能击败的怪物数量
        int[] dp = new int[n];

        // 初始化 dp 数组
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // 每个怪物至少可以击败自己
        }

        // 遍历每个怪物
        for (int i = 1; i < n; i++) {
            // 遍历前面的怪物，找到满足条件的怪物 k
            for (int k = 0; k < i; k++) {
                // 如果当前怪物的血量和攻击力大于前面的怪物 k，并且小E的初始血量和攻击力大于当前怪物的血量和攻击力
                if (h[i] > h[k] && a[i] > a[k] && H > h[i] && A > a[i]) {
                    // 更新 dp[i]
                    dp[i] = Math.max(dp[i], dp[k] + 1);
                }
            }
        }

        // 找到最大的 dp[i] 值
        int maxCount = 0;
        for (int i = 0; i < n; i++) {
            maxCount = Math.max(maxCount, dp[i]);
        }

        return maxCount;
    }
}
