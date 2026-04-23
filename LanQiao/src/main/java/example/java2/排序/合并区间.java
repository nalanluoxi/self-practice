package example.java2.排序;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：example.java2.排序
 * @Project：LanQiaoBei
 * @name：合并区间
 * @Date：2025/6/9 10:15
 * @Filename：合并区间
 */
public class 合并区间 {

    public static void main(String[] args) {
        int[][] merge = merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        for (int[] ints : merge) {
            System.out.println(Arrays.toString(ints));
        }
    }
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals,new Comparator<>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });
        List<int[]> ans=new ArrayList<>();
        ans.add(new int[]{intervals[0][0],intervals[0][1]});
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0]<=ans.get(ans.size()-1)[1]){
                int[] remove = ans.remove(ans.size() - 1);
                ans.add(new int[]{Math.min(remove[0],intervals[i][0]),Math.max(intervals[i][1],remove[1])});
            }else {
                ans.add(new int[]{intervals[i][0],intervals[i][1]});
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }
}
