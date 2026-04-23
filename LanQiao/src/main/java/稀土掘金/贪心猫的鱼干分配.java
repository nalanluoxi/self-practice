package 稀土掘金;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：贪心猫的鱼干分配
 * @Date：2025/2/2 15:40
 * @Filename：贪心猫的鱼干分配
 */
public class 贪心猫的鱼干分配 {


    public static void main(String[] args) {
        List<Integer> catsLevels1 = new ArrayList<>();
        catsLevels1.add(1);
        catsLevels1.add(2);
        catsLevels1.add(2);

        List<Integer> catsLevels2 = new ArrayList<>();
        catsLevels2.add(6);
        catsLevels2.add(5);
        catsLevels2.add(4);
        catsLevels2.add(3);
        catsLevels2.add(2);
        catsLevels2.add(16);

        List<Integer> catsLevels3 = new ArrayList<>();
        catsLevels3.add(1);
        catsLevels3.add(2);
        catsLevels3.add(2);
        catsLevels3.add(3);
        catsLevels3.add(3);
        catsLevels3.add(20);
        catsLevels3.add(1);
        catsLevels3.add(2);
        catsLevels3.add(3);
        catsLevels3.add(3);
        catsLevels3.add(2);
        catsLevels3.add(1);
        catsLevels3.add(5);
        catsLevels3.add(6);
        catsLevels3.add(6);
        catsLevels3.add(5);
        catsLevels3.add(5);
        catsLevels3.add(7);
        catsLevels3.add(7);
        catsLevels3.add(4);

        // System.out.println(solution(3, catsLevels1) == 4);
        System.out.println(solution(6, catsLevels2) == 17);
        // System.out.println(solution(20, catsLevels3) == 35);
    }


    public static int solution(int n, List<Integer> list) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            if (list.get(i) > list.get(i - 1)) {
                dp[i] = dp[i-1] + 1;
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            if (list.get(i) > list.get(i + 1)) {
                dp[i] = Math.max(dp[i], dp[i+1] + 1);
            }
        }


        int count = 0;
        for (int i = 0; i < n; i++) {
            count += dp[i];
        }
        return count;
    }

}
