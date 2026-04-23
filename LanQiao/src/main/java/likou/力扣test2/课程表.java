package likou.力扣test2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author 纳兰洛熙
 * @Package：likou.力扣test2
 * @Project：LanQiaoBei
 * @name：课程表
 * @Date：2025/7/2 9:55
 * @Filename：课程表
 */
public class 课程表 {


    public static void main(String[] args) {



    }

    /*public static void main(String[] args) {
       // System.out.println(canFinish(2, new int[][]{{1, 0}}));
        System.out.println(canFinish(2, new int[][]{{1,0},{0,1}}));
    }

    static boolean ans;
    static List<List<Integer>> list;
    static int[] visited;
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        ans=true;
        list=new ArrayList<>();
        visited=new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] prerequisite : prerequisites) {
            list.get(prerequisite[1]).add(prerequisite[0]);
        }
        for (int i = 0; i < numCourses; i++) {
            if (visited[i]!=0){
                continue;
            }
            dfs(i);
            if (!ans){
                return ans;
            }
        }
        return ans;
    }

    public static void dfs(int index){
        visited[index]=1;
        for (Integer num : list.get(index)) {
            if (visited[num]==1){
                ans=false;
            } else if (visited[num] == 0) {
                dfs(num);
            }
            if (!ans){
                return;
            }
        }
        visited[index]=2;
    }*/
}
