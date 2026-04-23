package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：课程表
 * @Date：2025/5/13 21:14
 * @Filename：课程表
 */
public class 课程表 {
    public static void main(String[] args) {
        int numCourses = 2;
        //int[][] prerequisites = {{1, 0}, {0, 1}};
        int[][] prerequisites = {{0,1}};
        //int[][] prerequisites = {{1,4},{2,4},{3,1},{3,2}};
        System.out.println(canFinish(numCourses, prerequisites));
    }

    static List<List<Integer>> list;
    static boolean ans;
    static int[] visited;

    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        list = new ArrayList<>();
        visited = new int[numCourses];
        ans = true;
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        for (int i = 0; i < prerequisites.length; i++) {
            int now = prerequisites[i][0];
            int pre = prerequisites[i][1];
            list.get(pre).add(now);
        }
        for (int i = 0; i < numCourses && ans; i++) {
            if (visited[i] != 0) {
                continue;
            }
            dfs(i);
        }
        return ans;
    }

    public static void dfs(int index) {
        visited[index] = 1;

        for (Integer next : list.get(index)) {
            if (visited[next] == 0) {
                dfs(next);
                if (!ans) {
                    return;
                }
            } else if (visited[next] == 1) {
                ans = false;
                return;
            }
        }
        visited[index] = 2;
    }


}
