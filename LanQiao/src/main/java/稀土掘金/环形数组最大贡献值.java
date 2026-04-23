package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：环形数组最大贡献值
 * @Date：2024/12/27 16:28
 * @Filename：环形数组最大贡献值
 */
public class 环形数组最大贡献值 {

    public static void main(String[] args) {
        System.out.println(solution(3, new int[]{1, 2, 3}) == 5);
        System.out.println(solution(4, new int[]{4, 1, 2, 3}) == 12);
        System.out.println(solution(5, new int[]{1, 5, 3, 7, 2}) == 24);
    }


    public static int solution(int n, int[] a) {
        // PLEASE DO NOT MODIFY THE FUNCTION SIGNATURE
        // write code here
        int ans = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                if (i == j) {
                    continue;
                }
                ans = Math.max(ans, (a[i] + a[j]) * dist(i, j, n));
            }
        }
        return ans; // Placeholder return
    }

    public static int dist(int i, int j, int len) {
        int max = Math.max(i, j);
        int min = Math.min(i, j);
        int ans1 = max - min;
        int ans2 = len - max + min;
        return Math.min(ans1, ans2);
    }


}
