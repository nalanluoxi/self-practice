package likou;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：课程表2
 * @Date：2025/7/6 12:29
 * @Filename：课程表2
 */
public class 课程表2 {

    public static void main(String[] args) {

    }
    static List<List<Integer>> list;
    static int[] visisted;
    static int[] result;
    static boolean b;
    static int index;

    public static int[] findOrder(int numCourses, int[][] nums) {
        list = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        b = true;
        visisted = new int[numCourses];
        index = numCourses - 1;
        result = new int[numCourses];
        for (int[] num : nums) {
            list.get(num[1]).add(num[0]);
        }
        for (int i = 0; i < numCourses && b; i++) {
            if (visisted[i]==0){
                dfs(i);
            }
        }
        if (!b){
            return new int[0];
        }
        return result;
    }

    public static void dfs(int u){
        visisted[u]=1;
        for (Integer i : list.get(u)) {
            if (visisted[i]==0){
                dfs(i);
                if (!b){
                    return;
                }
            } else if (visisted[i] == 1) {
                b=false;
                return;
            }
        }
        visisted[u]=2;
        result[index--]=u;
    }

}
