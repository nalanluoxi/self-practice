package likou.动态规划;

import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.动态规划
 * @Project：LanQiaoBei
 * @name：三角形最小路径和
 * @Date：2025/6/21 22:02
 * @Filename：三角形最小路径和
 */
public class 三角形最小路径和 {


    public static void main(String[] args) {
        List<List<Integer>> triangle = List.of(
                List.of(2),
                List.of(3, 4),
                List.of(6, 5, 7),
                List.of(4, 1, 8, 3)
        );
        System.out.println(minimumTotal(triangle));
    }
    public static int minimumTotal(List<List<Integer>> triangle) {
        int size = triangle.size();
        int [][]dp=new int[size][size];
        for (int i = 0; i < size; i++) {
            dp[size-1][i]=triangle.get(size-1).get(i);
        }
        for (int i = size-2; i >=0; i--) {
            int wei = triangle.get(i).size();
            for (int j = 0; j < wei; j++) {
                dp[i][j]=triangle.get(i).get(j)+Math.min(dp[i+1][j],dp[i+1][j+1]);
            }
        }
        return dp[0][0];
    }
}
