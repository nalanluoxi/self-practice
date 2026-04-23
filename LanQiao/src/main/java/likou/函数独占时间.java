package likou;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author 纳兰洛熙
 * @Package：luogu
 * @Project：LanQiaoBei
 * @name：函数独占时间
 * @Date：2025/3/17 16:25
 * @Filename：函数独占时间
 */
public class 函数独占时间 {

    public static void main(String[] args) {
        /*List<String> logs = List.of("0:start:0","1:start:2","1:end:5","0:end:6");
        int[] ints = exclusiveTime(2, logs);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(ints[i]);
        }*/
    }

    public static int[] exclusiveTime(int n, List<String> logs) {
        Deque<Integer> deque=new LinkedList<>();
        int [] ans=new int[n];
        int cur=-1;
        for (int i = 0; i < logs.size(); i++) {
            String string = logs.get(i);
            String[] split = string.split(":");
            int id = Integer.parseInt(split[0]);
            String status = split[1];
            int time = Integer.parseInt(split[2]);
            //System.out.println(id+" : "+status+" : "+time);
            if (status.equals("start")){
                if (!deque.isEmpty()){
                    Integer last = deque.peekLast();
                    ans[last]+=time-cur;
                }
                deque.addLast(id);
                cur=time;
            }else {
                Integer last = deque.pollLast();
                ans[last]+=time-cur+1;
                cur=time+1;
            }
        }
        return ans;
    }
}
