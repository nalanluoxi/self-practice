package likou.力扣test2;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：矩阵中的最长递增路径
 * @Date：2025/7/14 11:38
 * @Filename：矩阵中的最长递增路径
 */
public class 矩阵中的最长递增路径 {
    public static void main(String[] args) {
        int[][]nums={
                {9,9,6},
                {6,6,8},
                {2,1,1}
        };
        System.out.println(longestIncreasingPath(nums));
    }

    static int[][]dir ={{1,0},{-1,0},{0,1},{0,-1}};

    public static int longestIncreasingPath(int[][] nums) {
        int n = nums.length;
        int m = nums[0].length;
        int[][]dp=new int[n][m];
        Deque<int[]>deque=new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] d : dir) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni>=0&&ni<n&&nj>=0&&nj<m&&nums[i][j]<nums[ni][nj]){
                        dp[i][j]++;
                    }
                }
                if (dp[i][j]==0){
                    deque.offerLast(new int[]{i,j});
                }
            }
        }
        int ans=0;
        while (!deque.isEmpty()){
            int size = deque.size();
            ans++;
            for (int i = 0; i < size; i++) {

                int[] ints = deque.pollLast();
                int x = ints[0];
                int y = ints[1];
                for (int[] d : dir) {
                    int nx = d[0] + x;
                    int ny = d[1] + y;
                    if (nx>=0&&nx<n&&ny>=0&&ny<m&&nums[x][y] >nums[nx][ny]){
                        dp[nx][ny]--;
                        if (dp[nx][ny]==0){
                            deque.offerFirst(new int[]{nx,ny});
                        }
                    }
                }
            }
        }
        return ans;
    }
}
