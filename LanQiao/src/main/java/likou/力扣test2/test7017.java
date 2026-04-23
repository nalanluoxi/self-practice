package likou.力扣test2;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：test7017
 * @Date：2025/7/17 21:12
 * @Filename：test7017
 */
public class test7017 {
    public static void main(String[] args) {
/*        int[][] nums = {
                {9, 9, 4},
                {6, 6, 8},
                {2, 1, 1}
        };
        System.out.println(longestIncreasingPath(nums));*/
    }

    static int[] visited;
    static boolean b;
    static int[]anslist;
    static int index;

    static List<List<Integer>>list;
    public int[] findOrder(int n, int[][] nums) {
        b=true;
        list=new ArrayList<>();
        anslist=new int[n];
        visited=new int[n];
        index=n-1;
        for (int i = 0; i < n; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] num : nums) {
            list.get(num[1]).add(num[0]);
        }
        for (int i = 0; i < n && b; i++) {
            if (visited[i]==0){
                dfs(i);
            }
        }
        if (!b){
            return new int[0];
        }
        return anslist;
    }

    public static void dfs(int u){
        visited[u]=1;
        for (Integer i : list.get(u)) {
            if (visited[i]==0){
                dfs(i);
                if (!b){
                    return;
                }
            } else if (visited[i] == 1) {
                b=false;
                return;
            }
        }
        visited[u]=2;
        anslist[index--]=u;

    }

    public static int findKthNumber(int n, int k) {
        int  cur=1;
        k--;
        while (k>0){
            int step = getStep(cur, n);
            if (step<=k){
                cur++;
                k-=step;
            }else {
                cur=cur*10;
                k--;
            }
        }
        return cur;
    }

    public static int getStep(int cur,int n){
        int step=0;
        long first=cur;
        long last=cur;
        while (first<=n){
            step+=Math.min(last,n)-first+1;
            first=first*10;
            last=last*10+9;
        }
        return step;
    }

    public static int longestIncreasingPath(int[][] nums) {
        int[][] dir = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        Deque<int[]> deque = new LinkedList<>();

        int n = nums.length;
        int m = nums[0].length;
        int[][] dp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] d : dir) {
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni >= 0 && ni < n && nj >= 0 && nj < m && nums[i][j] < nums[ni][nj]) {
                        dp[i][j]++;
                    }
                }
                if (dp[i][j] == 0) {
                    deque.offerLast(new int[]{i, j});
                }
            }
        }
        int ans = 0;
        while (!deque.isEmpty()) {
            int size = deque.size();
            ans++;
            for (int i = 0; i < size; i++) {
                int[] ints = deque.pollLast();

                for (int[] d : dir) {
                    int x = ints[0] + d[0];
                    int y = ints[1] + d[1];
                    if (x >= 0 && x < n && y >= 0 && y < m && nums[ints[0]][ints[1]] > nums[x][y]) {
                        if (--dp[x][y] == 0) {
                            deque.addFirst(new int[]{x, y});
                        }
                    }
                }

            }
        }
        return ans;
    }

    public static int maxScore(int[] cardPoints, int k) {
        int winSize = cardPoints.length - k;
        int winSum = 0;
        for (int i = 0; i < winSize; i++) {
            winSum += cardPoints[i];
        }
        int minSum = winSum;
        for (int i = winSize; i < cardPoints.length; i++) {
            winSum = winSum + cardPoints[i] - cardPoints[i - winSize];
            minSum = Math.min(minSum, winSum);
        }
        int t = 0;
        for (int m : cardPoints) {
            t += m;
        }
        return t - minSum;
    }
}
