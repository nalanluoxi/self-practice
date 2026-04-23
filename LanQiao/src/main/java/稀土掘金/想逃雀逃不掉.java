package 稀土掘金;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：想逃雀逃不掉
 * @Date：2024/12/30 12:32
 * @Filename：想逃雀逃不掉
 */
public class 想逃雀逃不掉 {

    public static void main(String[] args) {
        // Add your test cases here
        char[][] pattern = {
                {'.', '.', '.', '.', '.'},
                {'.', 'R', 'R', 'D', '.'},
                {'.', 'U', '.', 'D', 'R'},
                {'.', 'U', 'L', 'L', '.'},
                {'.', '.', '.', '.', 'O'}
        };
        System.out.println(solution(5, 5, pattern) == 10);
    }


    public static int solution(int N, int M, char[][] data) {
        // 初始化 reachable 数组
        boolean[][] reachable = new boolean[N][M];
        Queue<int[]> queue = new LinkedList<>();

        // 找到所有出口位置，并标记为 reachable
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (data[i][j] == 'O') {
                    reachable[i][j] = true;
                    queue.offer(new int[]{i, j});
                }
            }
        }

        // 逆向追踪，标记所有可以从出口到达的位置
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0], y = pos[1];

            // 检查上方的位置
            if (x > 0 && (data[x - 1][y] == 'D' || data[x - 1][y] == '.') && !reachable[x - 1][y]) {
                reachable[x - 1][y] = true;
                queue.offer(new int[]{x - 1, y});
            }
            // 检查下方的位置
            if (x < N - 1 && (data[x + 1][y] == 'U' || data[x + 1][y] == '.') && !reachable[x + 1][y]) {
                reachable[x + 1][y] = true;
                queue.offer(new int[]{x + 1, y});
            }
            // 检查左方的位置
            if (y > 0 && (data[x][y - 1] == 'R' || data[x][y-1] == '.') && !reachable[x][y - 1]) {
                reachable[x][y - 1] = true;
                queue.offer(new int[]{x, y - 1});
            }
            // 检查右方的位置
            if (y < M - 1 && (data[x][y + 1] == 'L' || data[x][y+1] == '.') && !reachable[x][y + 1]) {
                reachable[x][y + 1] = true;
                queue.offer(new int[]{x, y + 1});
            }
        }

        // 统计危险位置的数量
        int dangerCount = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (!reachable[i][j]) {
                    dangerCount++;
                }
            }
        }

        return dangerCount;
    }

}
