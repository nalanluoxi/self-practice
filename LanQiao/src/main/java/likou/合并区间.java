package likou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：合并区间
 * @Date：2025/3/31 17:36
 * @Filename：合并区间
 */
public class 合并区间 {
    public static void main(String[] args) {
       // int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] intervals = {{2,3},{4,5},{6,7},{8,9},{1,10}};
        int[][] merge = merge(intervals);
        for (int i = 0; i < merge.length; i++) {
            for (int j = 0; j < merge[i].length; j++) {
                System.out.print(merge[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static int[][] merge(int[][] intervals) {
        if (intervals==null||intervals.length==0){
            return new int[0][0];
        }
        List<Integer[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            return o1[0] - o2[0];
        });
        for (int i = 0; i < intervals.length; i++) {
            int[] temp = intervals[i];
            int start = temp[0];
            int end = temp[1];
            boolean flag=true;
            for (int j = 0; j < ans.size(); j++) {
                Integer[] antemp = ans.get(j);
                int tStart = antemp[0];
                int tEnd = antemp[1];
                if (start>tEnd||end<tStart){
                    continue;
                } else {
                    flag=false;
                    ans.set(j, new Integer[]{Math.min(start, tStart), Math.max(end, tEnd)});
                    break;
                }
            }
            if (flag){
                ans.add(new Integer[]{start,end});
            }
        }
        int[][] ints = new int[ans.size()][2];
        for (int i = 0; i < ans.size(); i++) {
            ints[i][0]=ans.get(i)[0];
            ints[i][1]=ans.get(i)[1];
        }
        return ints;
    }
}
