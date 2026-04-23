package likou;

import java.util.*;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：矩阵中的最长递增路径
 * @Date：2025/7/9 12:04
 * @Filename：矩阵中的最长递增路径
 */
public class 矩阵中的最长递增路径 {
    public static void main(String[] args) {
        int[][] num = {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}};
        int i = longestIncreasingPath(num);
        System.out.println(i);
    }

    public List<Integer> findDuplicates(int[] nums) {
        int[]visited=new int[nums.length];
        List<Integer> ans=new ArrayList<>();
        for (int num : nums) {
            if (visited[num-1]==0){
                visited[num-1]++;
            }else {
                ans.add(num);
            }
        }
        return ans;
    }
    public List<Integer> findDuplicates1(int[] nums) {
        Set<Integer> set=new HashSet<>();
        List<Integer> list=new ArrayList<>();
        for(int i:nums){
            if(set.contains(i)){
                list.add(i);
            }else{
                set.add(i);
            }
        }
        return list;
    }

    static int[][] dir = {{1, 0}, {0, 1}, {0, -1}, {-1, 0}};

    public static int longestIncreasingPath(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] dp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] ints : dir) {
                    int ni = ints[0] + i;
                    int nj = ints[1] + j;
                    if (ni >= 0 && ni < n && nj >= 0 && nj < m && matrix[ni][nj] > matrix[i][j]) {
                        dp[i][j]++;
                    }
                }
            }
        }

        Deque<int[]> deque = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (dp[i][j] == 0) {
                    deque.offerLast(new int[]{i, j});
                }
            }
        }

        int ans = 0;
        while (!deque.isEmpty()) {
            ans++;
            int size = deque.size();
            for (int c = 0; c < size; c++) {
                int[] ints = deque.pollLast();
                int i = ints[0];
                int j = ints[1];
                for (int[] ints1 : dir) {
                    int pi = i + ints1[0];
                    int pj = ints1[1] + j;
                    if (pi >= 0 && pi < n && pj >= 0 && pj < m && matrix[pi][pj] < matrix[i][j]) {
                        if (--dp[pi][pj] == 0) {
                            deque.offerFirst(new int[]{pi, pj});
                        }
                    }
                }
            }
        }


        return ans;
    }
}
